import sys
import time
from typing import Callable
from loguru import logger
from fastapi import Request, Response


def setup_logger(log_level: str = "INFO"):
    """
    Configures Loguru logger format and output.
    """
    logger.remove()  # Remove default handler
    logger.add(
        sys.stdout,
        level=log_level.upper(),
        format=(
            "<green>{time:YYYY-MM-DD HH:mm:ss.SSS}</green> | "
            "<level>{level: <8}</level> | "
            "<cyan>{name}</cyan>:<cyan>{function}</cyan>:<cyan>{line}</cyan> - "
            "<level>{message}</level>"
        ),
        enqueue=True,
    )
    return logger


async def logging_middleware(request: Request, call_next: Callable) -> Response:
    """
    FastAPI middleware to log incoming HTTP requests and execution duration.
    """
    start_time = time.time()
    method = request.method
    path = request.url.path
    logger.info(f"Incoming Request: {method} {path}")

    try:
        response = await call_next(request)
        process_time = (time.time() - start_time) * 1000
        logger.info(
            f"Completed Request: {method} {path} - Status: {response.status_code} - Duration: {process_time:.2f}ms"
        )
        return response
    except Exception as exc:
        process_time = (time.time() - start_time) * 1000
        logger.error(
            f"Failed Request: {method} {path} - Error: {exc} - Duration: {process_time:.2f}ms"
        )
        raise exc
