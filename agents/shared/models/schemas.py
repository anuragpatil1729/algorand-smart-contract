import time
from enum import Enum
from typing import Dict, Any, List, Optional
from pydantic import BaseModel, Field, ConfigDict, AliasChoices


class ExecutionStatus(str, Enum):
    QUEUED = "QUEUED"
    RUNNING = "RUNNING"
    COMPLETED = "COMPLETED"
    FAILED = "FAILED"
    CANCELLED = "CANCELLED"


class Capability(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    name: str
    description: Optional[str] = None
    supportedTaskTypes: List[str] = Field(
        default_factory=list,
        validation_alias=AliasChoices("supportedTaskTypes", "supported_task_types"),
    )
    maxConcurrency: int = Field(
        default=5,
        validation_alias=AliasChoices("maxConcurrency", "max_concurrency"),
    )
    averageExecutionTime: float = Field(
        default=10.0,
        validation_alias=AliasChoices("averageExecutionTime", "average_execution_time"),
    )
    averagePrice: float = Field(
        default=10.0,
        validation_alias=AliasChoices("averagePrice", "average_price"),
    )


class AgentInfo(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    agentId: str = Field(
        default="agent-01",
        validation_alias=AliasChoices("agentId", "agent_id"),
    )
    name: str = "Base Agent"
    version: str = "1.0.0"
    port: int = 8000
    capabilities: List[str] = Field(default_factory=list)
    reputation: float = 5.0
    wallet: Optional[str] = ""


class QuoteRequest(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    task: Optional[str] = None
    description: str
    priority: str = Field(default="MEDIUM", description="LOW, MEDIUM, HIGH, CRITICAL")
    budget: Optional[float] = None
    estimatedComplexity: Optional[str] = Field(
        default="MEDIUM",
        validation_alias=AliasChoices("estimatedComplexity", "estimated_complexity"),
    )
    taskType: Optional[str] = Field(
        default=None,
        validation_alias=AliasChoices("taskType", "task_type"),
    )


class QuoteResponse(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    price: float
    estimatedTime: int = Field(
        description="Estimated execution time in seconds",
        validation_alias=AliasChoices("estimatedTime", "estimated_time"),
    )
    confidence: float = Field(description="Confidence percentage 0.0 to 100.0")
    reputation: float = 5.0
    capability: str
    agentId: Optional[str] = Field(
        default=None,
        validation_alias=AliasChoices("agentId", "agent_id"),
    )
    agentName: Optional[str] = Field(
        default=None,
        validation_alias=AliasChoices("agentName", "agent_name"),
    )
    supportedCapabilities: Optional[List[str]] = Field(
        default_factory=list,
        validation_alias=AliasChoices("supportedCapabilities", "supported_capabilities"),
    )


class ExecuteRequest(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    taskId: Optional[str] = Field(
        default=None,
        validation_alias=AliasChoices("taskId", "task_id"),
    )
    description: str
    context: Optional[Dict[str, Any]] = Field(default_factory=dict)
    workflowId: Optional[str] = Field(
        default=None,
        validation_alias=AliasChoices("workflowId", "workflow_id"),
    )
    taskType: Optional[str] = Field(
        default=None,
        validation_alias=AliasChoices("taskType", "task_type"),
    )
    prompt: Optional[str] = None


# Alias TaskRequest to ExecuteRequest for compatibility
TaskRequest = ExecuteRequest


class ExecuteResponse(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    executionId: str = Field(
        validation_alias=AliasChoices("executionId", "execution_id"),
    )
    taskId: str = Field(
        validation_alias=AliasChoices("taskId", "task_id"),
    )
    status: ExecutionStatus
    output: Optional[Any] = None
    executionTime: float = Field(
        default=0.0,
        validation_alias=AliasChoices("executionTime", "execution_time"),
    )
    error: Optional[str] = None


# Alias TaskResponse to ExecuteResponse for compatibility
TaskResponse = ExecuteResponse


class ExecutionLog(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    timestamp: float = Field(default_factory=time.time)
    level: str = "INFO"
    message: str
    metadata: Optional[Dict[str, Any]] = None


class StatusResponse(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    taskId: str = Field(
        validation_alias=AliasChoices("taskId", "task_id"),
    )
    executionId: Optional[str] = Field(
        default=None,
        validation_alias=AliasChoices("executionId", "execution_id"),
    )
    status: ExecutionStatus
    progress: float = Field(default=0.0, description="Progress from 0.0 to 100.0")
    logs: List[ExecutionLog] = Field(default_factory=list)
    output: Optional[Any] = None
    startTime: Optional[float] = Field(
        default=None,
        validation_alias=AliasChoices("startTime", "start_time"),
    )
    completedAt: Optional[float] = Field(
        default=None,
        validation_alias=AliasChoices("completedAt", "completed_at"),
    )
    error: Optional[str] = None


class CapabilitiesResponse(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    supportedCapabilities: List[str] = Field(
        default_factory=list,
        validation_alias=AliasChoices("supportedCapabilities", "supported_capabilities"),
    )
    maxConcurrency: int = Field(
        default=5,
        validation_alias=AliasChoices("maxConcurrency", "max_concurrency"),
    )
    supportedTaskTypes: List[str] = Field(
        default_factory=list,
        validation_alias=AliasChoices("supportedTaskTypes", "supported_task_types"),
    )
    averageExecutionTime: float = Field(
        default=10.0,
        validation_alias=AliasChoices("averageExecutionTime", "average_execution_time"),
    )
    averagePrice: float = Field(
        default=10.0,
        validation_alias=AliasChoices("averagePrice", "average_price"),
    )
    capabilities: List[Capability] = Field(default_factory=list)


class HealthResponse(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    status: str = "HEALTHY"
    version: str = "1.0.0"
    capabilities: List[str] = Field(default_factory=list)
    responseTimeMs: float = Field(
        default=0.0,
        validation_alias=AliasChoices("responseTimeMs", "response_time_ms"),
    )
    uptimeSeconds: float = Field(
        default=0.0,
        validation_alias=AliasChoices("uptimeSeconds", "uptime_seconds"),
    )
    cpuUsagePercent: float = Field(
        default=0.0,
        validation_alias=AliasChoices("cpuUsagePercent", "cpu_usage_percent"),
    )
    memoryUsageMb: float = Field(
        default=0.0,
        validation_alias=AliasChoices("memoryUsageMb", "memory_usage_mb"),
    )
    runningTasks: int = Field(
        default=0,
        validation_alias=AliasChoices("runningTasks", "running_tasks"),
    )
    queueSize: int = Field(
        default=0,
        validation_alias=AliasChoices("queueSize", "queue_size"),
    )
    agentId: Optional[str] = Field(
        default=None,
        validation_alias=AliasChoices("agentId", "agent_id"),
    )
    agentName: Optional[str] = Field(
        default=None,
        validation_alias=AliasChoices("agentName", "agent_name"),
    )


class AgentErrorResponse(BaseModel):
    error: str
    detail: Optional[str] = None
    code: str = "AGENT_ERROR"


class BazaarDiscoveryManifest(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    name: str
    description: str
    endpoint: str
    capabilities: List[str] = Field(default_factory=list)
    supportedModels: List[str] = Field(
        default_factory=lambda: ["gpt-4o", "claude-3-5-sonnet", "deepseek-r1", "gemini-2.5-flash"],
        validation_alias=AliasChoices("supportedModels", "supported_models"),
    )
    pricing: Dict[str, float] = Field(default_factory=dict)
    averageLatency: float = Field(
        default=450.0,
        validation_alias=AliasChoices("averageLatency", "average_latency"),
    )
    reputation: float = 98.0
    jsonSchema: Dict[str, Any] = Field(
        default_factory=dict,
        validation_alias=AliasChoices("jsonSchema", "schema"),
    )
    version: str = "1.0.0"

