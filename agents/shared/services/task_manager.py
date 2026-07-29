import asyncio
import time
from typing import Dict, Any, List, Optional
from agents.shared.models.schemas import ExecutionStatus, ExecutionLog, StatusResponse


class TaskRecord:
    def __init__(self, task_id: str, execution_id: str, description: str, context: Optional[Dict[str, Any]] = None):
        self.task_id = task_id
        self.execution_id = execution_id
        self.description = description
        self.context = context or {}
        self.status = ExecutionStatus.QUEUED
        self.progress = 0.0
        self.logs: List[ExecutionLog] = []
        self.output: Optional[Any] = None
        self.error: Optional[str] = None
        self.start_time: float = time.time()
        self.completed_at: Optional[float] = None

    def add_log(self, message: str, level: str = "INFO", metadata: Optional[Dict[str, Any]] = None):
        log_entry = ExecutionLog(timestamp=time.time(), level=level, message=message, metadata=metadata)
        self.logs.append(log_entry)

    def to_status_response(self) -> StatusResponse:
        return StatusResponse(
            taskId=self.task_id,
            executionId=self.execution_id,
            status=self.status,
            progress=self.progress,
            logs=self.logs,
            output=self.output,
            startTime=self.start_time,
            completedAt=self.completed_at,
            error=self.error,
        )


class TaskManager:
    """
    In-memory task manager tracking task execution lifecycle.
    """

    def __init__(self):
        self._tasks: Dict[str, TaskRecord] = {}
        self._lock = asyncio.Lock()

    async def create_task(
        self, task_id: str, execution_id: str, description: str, context: Optional[Dict[str, Any]] = None
    ) -> TaskRecord:
        async with self._lock:
            record = TaskRecord(task_id=task_id, execution_id=execution_id, description=description, context=context)
            record.add_log("Task queued for execution")
            self._tasks[task_id] = record
            return record

    async def update_status(
        self,
        task_id: str,
        status: ExecutionStatus,
        progress: Optional[float] = None,
        output: Optional[Any] = None,
        error: Optional[str] = None,
        log_message: Optional[str] = None,
    ) -> Optional[TaskRecord]:
        async with self._lock:
            record = self._tasks.get(task_id)
            if not record:
                return None
            record.status = status
            if progress is not None:
                record.progress = progress
            if output is not None:
                record.output = output
            if error is not None:
                record.error = error
            if status in (ExecutionStatus.COMPLETED, ExecutionStatus.FAILED, ExecutionStatus.CANCELLED):
                record.completed_at = time.time()
            if log_message:
                record.add_log(log_message, level="ERROR" if status == ExecutionStatus.FAILED else "INFO")
            return record

    async def get_task(self, task_id: str) -> Optional[TaskRecord]:
        async with self._lock:
            return self._tasks.get(task_id)

    async def add_task_log(self, task_id: str, message: str, level: str = "INFO", metadata: Optional[Dict[str, Any]] = None):
        async with self._lock:
            record = self._tasks.get(task_id)
            if record:
                record.add_log(message=message, level=level, metadata=metadata)

    async def count_by_status(self, status: ExecutionStatus) -> int:
        async with self._lock:
            return sum(1 for task in self._tasks.values() if task.status == status)

    async def get_stats(self) -> Dict[str, int]:
        async with self._lock:
            running = sum(1 for task in self._tasks.values() if task.status == ExecutionStatus.RUNNING)
            queued = sum(1 for task in self._tasks.values() if task.status == ExecutionStatus.QUEUED)
            completed = sum(1 for task in self._tasks.values() if task.status == ExecutionStatus.COMPLETED)
            failed = sum(1 for task in self._tasks.values() if task.status == ExecutionStatus.FAILED)
            return {
                "running": running,
                "queued": queued,
                "completed": completed,
                "failed": failed,
                "total": len(self._tasks),
            }
