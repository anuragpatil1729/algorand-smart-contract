import time
import os
import psutil
from typing import List, Optional
from agents.shared.models.schemas import HealthResponse, AgentInfo
from agents.shared.services.task_manager import TaskManager, ExecutionStatus


class HealthMonitor:
    """
    Service for monitoring system resources, process uptime, and agent execution state.
    """

    def __init__(self, agent_info: AgentInfo, task_manager: TaskManager):
        self.agent_info = agent_info
        self.task_manager = task_manager
        self.start_time = time.time()
        self.process = psutil.Process(os.getpid())

    async def get_health(self) -> HealthResponse:
        start_request = time.time()
        uptime_seconds = time.time() - self.start_time

        # System metrics
        try:
            cpu_percent = psutil.cpu_percent(interval=None)
            mem_info = self.process.memory_info()
            mem_mb = mem_info.rss / (1024 * 1024)
        except Exception:
            cpu_percent = 0.0
            mem_mb = 0.0

        stats = await self.task_manager.get_stats()
        response_time_ms = round((time.time() - start_request) * 1000, 3)

        return HealthResponse(
            status="HEALTHY",
            version=self.agent_info.version,
            capabilities=self.agent_info.capabilities,
            responseTimeMs=response_time_ms,
            uptimeSeconds=round(uptime_seconds, 2),
            cpuUsagePercent=cpu_percent,
            memoryUsageMb=round(mem_mb, 2),
            runningTasks=stats["running"],
            queueSize=stats["queued"],
            agentId=self.agent_info.agentId,
            agentName=self.agent_info.name,
        )
