import os
import asyncio
import uvicorn
from abc import ABC, abstractmethod
from typing import Optional, Dict, Any, List
from contextlib import asynccontextmanager
from fastapi import FastAPI, HTTPException, Request, status as http_status
from fastapi.responses import JSONResponse
from fastapi.exceptions import RequestValidationError
from fastapi.middleware.cors import CORSMiddleware

from agents.shared.config.settings import AgentConfig, load_config
from agents.shared.models.schemas import (
    AgentInfo,
    QuoteRequest,
    QuoteResponse,
    ExecuteRequest,
    ExecuteResponse,
    StatusResponse,
    CapabilitiesResponse,
    HealthResponse,
    Capability,
    AgentErrorResponse,
    BazaarDiscoveryManifest,
)
from agents.shared.services.task_manager import TaskManager
from agents.shared.services.pricing_strategy import BasePricingStrategy, DefaultPricingStrategy
from agents.shared.services.health_monitor import HealthMonitor
from agents.shared.services.execution_engine import TaskExecutionEngine
from agents.shared.services.registry_client import RegistryClient
from agents.shared.utils.logging import setup_logger, logging_middleware, logger


class BaseAgent(ABC):
    """
    Generic Abstract Base Class for all AI Agents in AgentMesh.
    Provides standard REST endpoints, health monitoring, capability discovery,
    quote generation, execution orchestration, task tracking, logging, and automatic registry integration.
    """

    def __init__(
        self,
        config_path: Optional[str] = None,
        pricing_strategy: Optional[BasePricingStrategy] = None,
    ):
        self.config_path = config_path
        self.config: AgentConfig = load_config(config_path)
        setup_logger()

        self.agent_info = AgentInfo(
            agentId=self.config.agent.id,
            name=self.config.agent.name,
            version=self.config.agent.version,
            port=self.config.agent.port,
            capabilities=self.config.agent.capabilities,
            reputation=self.config.agent.reputation,
            wallet=self.config.agent.wallet,
        )

        self.task_manager = TaskManager()
        self.pricing_strategy = pricing_strategy or DefaultPricingStrategy(self.config.pricing)
        self.health_monitor = HealthMonitor(self.agent_info, self.task_manager)
        self.execution_engine = TaskExecutionEngine(self.task_manager)
        self.registry_client = RegistryClient(self.agent_info, self.config, self.health_monitor)

        @asynccontextmanager
        async def lifespan(app: FastAPI):
            # Startup: Auto-register and start heartbeat background scheduler
            asyncio.create_task(self.registry_client.register())
            self.registry_client.start_heartbeat_loop(interval_seconds=30)
            yield
            # Shutdown: Auto-deregister
            await self.registry_client.deregister()

        self.app = FastAPI(
            title=self.agent_info.name,
            version=self.agent_info.version,
            description=f"AgentMesh AI Agent - {self.agent_info.name}",
            lifespan=lifespan,
        )

        self._setup_middleware()
        self._setup_exception_handlers()
        self._register_routes()
        self.initialize()

    def initialize(self) -> None:
        """
        Hook for initializing agent resources during startup.
        Can be overridden by subclass agents.
        """
        logger.info(f"Initialized agent '{self.agent_info.name}' (v{self.agent_info.version})")

    def _setup_middleware(self) -> None:
        self.app.add_middleware(
            CORSMiddleware,
            allow_origins=["*"],
            allow_credentials=True,
            allow_methods=["*"],
            allow_headers=["*"],
        )
        self.app.middleware("http")(logging_middleware)

    def _setup_exception_handlers(self) -> None:
        @self.app.exception_handler(RequestValidationError)
        async def validation_exception_handler(request: Request, exc: RequestValidationError):
            logger.warning(f"Validation error for {request.method} {request.url.path}: {exc}")
            return JSONResponse(
                status_code=http_status.HTTP_422_UNPROCESSABLE_ENTITY,
                content=AgentErrorResponse(
                    error="Validation Error",
                    detail=str(exc),
                    code="VALIDATION_ERROR",
                ).model_dump(),
            )

        @self.app.exception_handler(HTTPException)
        async def http_exception_handler(request: Request, exc: HTTPException):
            return JSONResponse(
                status_code=exc.status_code,
                content=AgentErrorResponse(
                    error=exc.detail if isinstance(exc.detail, str) else "HTTP Exception",
                    detail=str(exc.detail),
                    code=f"HTTP_{exc.status_code}",
                ).model_dump(),
            )

        @self.app.exception_handler(Exception)
        async def generic_exception_handler(request: Request, exc: Exception):
            logger.error(f"Unhandled Exception for {request.method} {request.url.path}: {exc}")
            return JSONResponse(
                status_code=http_status.HTTP_500_INTERNAL_SERVER_ERROR,
                content=AgentErrorResponse(
                    error="Internal Server Error",
                    detail=str(exc),
                    code="INTERNAL_ERROR",
                ).model_dump(),
            )

    def _register_routes(self) -> None:
        @self.app.get("/health", response_model=HealthResponse)
        async def get_health():
            return await self.health()

        @self.app.get("/capabilities", response_model=CapabilitiesResponse)
        async def get_capabilities():
            return await self.capabilities()

        @self.app.post("/quote", response_model=QuoteResponse)
        async def post_quote(request: QuoteRequest):
            return await self.quote(request)

        @self.app.post("/execute", response_model=ExecuteResponse)
        async def post_execute(request: ExecuteRequest):
            return await self.execute(request)

        @self.app.get("/status/{taskId}", response_model=StatusResponse)
        async def get_status(taskId: str):
            return await self.status(taskId)

        @self.app.get("/.well-known/x402-bazaar.json", response_model=BazaarDiscoveryManifest)
        @self.app.get("/bazaar/discover", response_model=BazaarDiscoveryManifest)
        async def get_bazaar_manifest():
            return await self.bazaar_manifest()

    # Standard overridable methods
    async def health(self) -> HealthResponse:
        """
        Returns real-time health and telemetry metrics.
        """
        return await self.health_monitor.get_health()

    async def capabilities(self) -> CapabilitiesResponse:
        """
        Returns supported capabilities, task types, concurrency, and metrics.
        """
        capability_models = [
            Capability(
                name=cap,
                description=f"{cap} capability for {self.agent_info.name}",
                supportedTaskTypes=self.config.agent.task_types,
                maxConcurrency=self.config.agent.max_concurrency,
                averageExecutionTime=self.config.agent.base_execution_time,
                averagePrice=self.config.agent.base_price,
            )
            for cap in self.agent_info.capabilities
        ]

        return CapabilitiesResponse(
            supportedCapabilities=self.agent_info.capabilities,
            maxConcurrency=self.config.agent.max_concurrency,
            supportedTaskTypes=self.config.agent.task_types,
            averageExecutionTime=self.config.agent.base_execution_time,
            averagePrice=self.config.agent.base_price,
            capabilities=capability_models,
        )

    async def bazaar_manifest(self) -> BazaarDiscoveryManifest:
        """
        Returns x402 Bazaar Discovery protocol self-publishing manifest.
        """
        return BazaarDiscoveryManifest(
            name=self.agent_info.name,
            description=f"Decentralized AI Agent: {self.agent_info.name}",
            endpoint=f"http://{self.config.agent.host}:{self.config.agent.port}",
            capabilities=self.agent_info.capabilities,
            supportedModels=["gpt-4o", "claude-3-5-sonnet", "deepseek-r1", "gemini-2.5-flash"],
            pricing={
                "base_price_usdc": self.config.agent.base_price,
                "token_price_usdc": 0.0001,
            },
            averageLatency=self.config.agent.base_execution_time * 1000.0,
            reputation=self.agent_info.reputation * 20.0,
            jsonSchema={
                "input": ExecuteRequest.model_json_schema(),
                "output": ExecuteResponse.model_json_schema(),
            },
            version=self.agent_info.version,
        )

    async def quote(self, request: QuoteRequest) -> QuoteResponse:
        """
        Generates dynamic price quote based on pricing strategy.
        """
        logger.info(f"Quote requested for task type: '{request.taskType or request.task}'")
        return self.pricing_strategy.calculate_quote(request, self.agent_info)

    async def execute(self, request: ExecuteRequest) -> ExecuteResponse:
        """
        Orchestrates task execution using TaskExecutionEngine and self.process_task.
        """
        return await self.execution_engine.run_task(request, self.process_task)

    async def status(self, taskId: str) -> StatusResponse:
        """
        Fetches current execution status, progress, logs, and output for a task.
        """
        record = await self.task_manager.get_task(taskId)
        if not record:
            raise HTTPException(status_code=404, detail=f"Task '{taskId}' not found")
        return record.to_status_response()

    @abstractmethod
    async def process_task(self, request: ExecuteRequest) -> Any:
        """
        Abstract method to be implemented by subclass agents.
        Contains the core domain AI/business logic for processing a task.
        """
        pass

    def run(self) -> None:
        """
        Starts the Uvicorn ASGI server with loaded configuration.
        """
        host = self.config.agent.host
        port = self.config.agent.port
        logger.info(f"Starting {self.agent_info.name} server on {host}:{port}")
        uvicorn.run(self.app, host=host, port=port)
