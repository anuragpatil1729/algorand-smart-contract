import os
import time
import asyncio
import httpx
from typing import Optional
from agents.shared.models.schemas import AgentInfo
from agents.shared.config.settings import AgentConfig
from agents.shared.services.health_monitor import HealthMonitor
from agents.shared.utils.logging import logger


class RegistryClient:
    """
    Background client for dynamic agent registration, heartbeat transmission, and graceful deregistration.
    Communicates with AgentMesh Router Registry API.
    """

    def __init__(self, agent_info: AgentInfo, config: AgentConfig, health_monitor: HealthMonitor):
        self.agent_info = agent_info
        self.config = config
        self.health_monitor = health_monitor

        self.registry_url = os.getenv("REGISTRY_URL", "http://localhost:8080").rstrip("/")
        self._heartbeat_task: Optional[asyncio.Task] = None
        self._running = False

    async def register(self) -> bool:
        """
        Sends POST /api/registry/register payload to the registry server.
        """
        url = f"{self.registry_url}/api/registry/register"
        endpoint = f"http://localhost:{self.config.agent.port}"

        payload = {
            "id": self.agent_info.agentId,
            "name": self.agent_info.name,
            "description": f"{self.agent_info.name} v{self.agent_info.version}",
            "endpoint": endpoint,
            "walletAddress": self.agent_info.wallet,
            "version": self.agent_info.version,
            "capabilities": self.agent_info.capabilities,
            "supportedTaskTypes": self.config.agent.task_types,
            "maxConcurrency": self.config.agent.max_concurrency,
            "basePrice": self.config.agent.base_price,
        }

        try:
            async with httpx.AsyncClient(timeout=5.0) as client:
                response = await client.post(url, json=payload)
                if response.status_code in (200, 201):
                    logger.info(f"RegistryClient: Successfully registered agent '{self.agent_info.agentId}' with registry at {self.registry_url}")
                    return True
                else:
                    logger.warning(f"RegistryClient: Registration failed with status {response.status_code}: {response.text}")
                    return False
        except Exception as exc:
            logger.warning(f"RegistryClient: Unreachable registry at {url} ({exc}). Agent running in standalone mode.")
            return False

    async def deregister(self) -> bool:
        """
        Sends POST /api/registry/deregister payload to notify registry of agent shutdown.
        """
        self.stop_heartbeat_loop()
        url = f"{self.registry_url}/api/registry/deregister"
        payload = {"agentId": self.agent_info.agentId}

        try:
            async with httpx.AsyncClient(timeout=3.0) as client:
                response = await client.post(url, json=payload)
                if response.status_code == 200:
                    logger.info(f"RegistryClient: Successfully deregistered agent '{self.agent_info.agentId}'")
                    return True
        except Exception as exc:
            logger.warning(f"RegistryClient: Failed to deregister agent '{self.agent_info.agentId}' ({exc})")
        return False

    async def send_heartbeat(self) -> bool:
        """
        Gathers system telemetry metrics and posts to /api/registry/heartbeat.
        """
        url = f"{self.registry_url}/api/registry/heartbeat"
        health = await self.health_monitor.get_health()

        payload = {
            "agentId": self.agent_info.agentId,
            "cpuUsagePercent": health.cpuUsagePercent,
            "memoryUsageMb": health.memoryUsageMb,
            "runningTasks": health.runningTasks,
            "queueSize": health.queueSize,
            "timestamp": int(time.time()),
            "lastExecutionTime": health.responseTimeMs,
            "status": "ONLINE",
        }

        try:
            async with httpx.AsyncClient(timeout=3.0) as client:
                response = await client.post(url, json=payload)
                if response.status_code == 200:
                    logger.debug(f"RegistryClient: Heartbeat sent for {self.agent_info.agentId}")
                    return True
        except Exception as exc:
            logger.debug(f"RegistryClient: Heartbeat failed for {self.agent_info.agentId}: {exc}")
        return False

    def start_heartbeat_loop(self, interval_seconds: int = 30):
        """
        Starts the background asyncio task for periodic heartbeats.
        """
        if self._running:
            return
        self._running = True
        self._heartbeat_task = asyncio.create_task(self._heartbeat_loop(interval_seconds))
        logger.info(f"RegistryClient: Started heartbeat background task (interval: {interval_seconds}s)")

    def stop_heartbeat_loop(self):
        """
        Stops the heartbeat background task.
        """
        self._running = False
        if self._heartbeat_task and not self._heartbeat_task.done():
            self._heartbeat_task.cancel()
            self._heartbeat_task = None

    async def _heartbeat_loop(self, interval_seconds: int):
        while self._running:
            await asyncio.sleep(interval_seconds)
            if self._running:
                await self.send_heartbeat()
