import os
from typing import Any, Dict, Optional
from agents.shared.base.agent import BaseAgent
from agents.shared.models.schemas import ExecuteRequest
from agents.shared.utils.logging import logger


class PptAgent(BaseAgent):
    """
    Pitch Deck & Strategy Agent implementation extending BaseAgent.
    Generates structured slide decks, executive summaries, and business model presentations.
    """

    def __init__(self, config_path: Optional[str] = None):
        default_config = config_path or os.path.join(os.path.dirname(__file__), "config", "agent.yaml")
        super().__init__(config_path=default_config)

    async def process_task(self, request: ExecuteRequest) -> Dict[str, Any]:
        title = request.description or request.prompt or "AgentMesh Pitch Deck"
        task_type = (request.taskType or "Startup Pitch Deck").strip()

        logger.info(f"PPT Agent processing '{task_type}' for: '{title}'")

        slides = [
            {
                "slideNumber": 1,
                "title": f"{title} - The Autonomous Future",
                "type": "TITLE",
                "content": "Multi-agent orchestration backed by Algorand Atomic Payments",
            },
            {
                "slideNumber": 2,
                "title": "The Problem",
                "type": "PROBLEM",
                "content": "AI agents operate in silos with high payment friction, zero inter-agent trust, and slow legacy payouts.",
            },
            {
                "slideNumber": 3,
                "title": "The AgentMesh Solution",
                "type": "SOLUTION",
                "content": "Data-driven router matching prompt DAGs to specialized agents with Algorand Escrow & Atomic Group Transfers.",
            },
            {
                "slideNumber": 4,
                "title": "Business Model & Unit Economics",
                "type": "FINANCIALS",
                "content": "1.5% routing protocol fee per atomic transaction group. Projected $12M ARR by Year 2.",
            },
            {
                "slideNumber": 5,
                "title": "Execution Roadmap",
                "type": "ROADMAP",
                "content": "Q3: Mainnet Escrow Deployment. Q4: Cross-chain micro-settlements & SDK releases.",
            },
        ]

        return {
            "deckTitle": title,
            "task_type": task_type,
            "slideCount": len(slides),
            "slides": slides,
            "exportFormats": ["JSON", "Markdown", "PPTX Ready"],
        }
