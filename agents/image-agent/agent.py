import os
from typing import Any, Dict, Optional
from agents.shared.base.agent import BaseAgent
from agents.shared.models.schemas import ExecuteRequest
from agents.shared.utils.logging import logger


class ImageAgent(BaseAgent):
    """
    Brand & Visual Graphics Agent implementation extending BaseAgent.
    Generates standalone SVG vector graphics, logos, color palettes, and visual design assets.
    """

    def __init__(self, config_path: Optional[str] = None):
        default_config = config_path or os.path.join(os.path.dirname(__file__), "config", "agent.yaml")
        super().__init__(config_path=default_config)

    async def process_task(self, request: ExecuteRequest) -> Dict[str, Any]:
        brand_name = request.description or request.prompt or "AgentMesh Venture"
        task_type = (request.taskType or "Logo Generation").strip()

        logger.info(f"Image Agent processing '{task_type}' for: '{brand_name}'")

        svg_code = (
            f'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 400 400" width="100%" height="100%">\n'
            f'  <defs>\n'
            f'    <linearGradient id="meshGrad" x1="0%" y1="0%" x2="100%" y2="100%">\n'
            f'      <stop offset="0%" stop-color="#06b6d4" />\n'
            f'      <stop offset="50%" stop-color="#6366f1" />\n'
            f'      <stop offset="100%" stop-color="#a855f7" />\n'
            f'    </linearGradient>\n'
            f'    <filter id="glow" x="-20%" y="-20%" width="140%" height="140%">\n'
            f'      <feGaussianBlur stdDeviation="8" result="blur" />\n'
            f'      <feComposite in="SourceGraphic" in2="blur" operator="over" />\n'
            f'    </filter>\n'
            f'  </defs>\n'
            f'  <rect width="400" height="400" rx="32" fill="#0f172a" />\n'
            f'  <g filter="url(#glow)">\n'
            f'    <polygon points="200,60 310,130 310,270 200,340 90,270 90,130" fill="none" stroke="url(#meshGrad)" stroke-width="8" />\n'
            f'    <circle cx="200" cy="200" r="28" fill="url(#meshGrad)" />\n'
            f'  </g>\n'
            f'  <text x="200" y="375" text-anchor="middle" fill="#f8fafc" font-family="sans-serif" font-size="18" font-weight="bold">{brand_name.upper()}</text>\n'
            f'</svg>'
        )

        return {
            "brand_name": brand_name,
            "task_type": task_type,
            "svg_artifact": svg_code,
            "color_palette": {
                "primary": "#06b6d4",
                "secondary": "#6366f1",
                "accent": "#a855f7",
                "background": "#0f172a",
            },
            "typography": {
                "header_font": "Plus Jakarta Sans",
                "body_font": "Inter",
            },
            "status": "Vector SVG Generated Successfully",
        }
