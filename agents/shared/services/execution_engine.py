import time
import uuid
from typing import Callable, Awaitable, Any, Optional
from agents.shared.models.schemas import ExecuteRequest, ExecuteResponse, ExecutionStatus
from agents.shared.services.task_manager import TaskManager
from agents.shared.utils.logging import logger


class TaskExecutionEngine:
    """
    Orchestration engine for validating, queuing, executing, and tracking agent tasks.
    The base execution engine handles lifecycle and orchestration without agent business logic.
    """

    def __init__(self, task_manager: TaskManager):
        self.task_manager = task_manager

    async def run_task(
        self,
        request: ExecuteRequest,
        handler: Callable[[ExecuteRequest], Awaitable[Any]],
    ) -> ExecuteResponse:
        start_time = time.time()
        task_id = request.taskId or f"task-{uuid.uuid4().hex[:8]}"
        execution_id = f"exec-{uuid.uuid4().hex[:8]}"

        logger.info(f"Task Engine: Received task {task_id} (exec_id: {execution_id})")

        # 1. Queued
        record = await self.task_manager.create_task(
            task_id=task_id,
            execution_id=execution_id,
            description=request.description,
            context=request.context,
        )

        # 2. Running
        await self.task_manager.update_status(
            task_id=task_id,
            status=ExecutionStatus.RUNNING,
            progress=10.0,
            log_message="Execution started",
        )

        try:
            # 3. Execute agent specific async handler
            output = await handler(request)
            exec_duration = time.time() - start_time

            # 4. Completed
            await self.task_manager.update_status(
                task_id=task_id,
                status=ExecutionStatus.COMPLETED,
                progress=100.0,
                output=output,
                log_message=f"Task completed successfully in {exec_duration:.2f}s",
            )
            logger.info(f"Task Engine: Task {task_id} completed successfully in {exec_duration:.2f}s")

            return ExecuteResponse(
                executionId=execution_id,
                taskId=task_id,
                status=ExecutionStatus.COMPLETED,
                output=output,
                executionTime=round(exec_duration, 4),
            )

        except Exception as exc:
            exec_duration = time.time() - start_time
            error_msg = str(exc)
            logger.error(f"Task Engine: Task {task_id} failed with error: {error_msg}")

            await self.task_manager.update_status(
                task_id=task_id,
                status=ExecutionStatus.FAILED,
                progress=0.0,
                error=error_msg,
                log_message=f"Task execution failed: {error_msg}",
            )

            return ExecuteResponse(
                executionId=execution_id,
                taskId=task_id,
                status=ExecutionStatus.FAILED,
                error=error_msg,
                executionTime=round(exec_duration, 4),
            )
