from abc import ABC, abstractmethod
from typing import Dict, Any, Optional
from agents.shared.models.schemas import QuoteRequest, QuoteResponse, AgentInfo
from agents.shared.config.settings import PricingConfigDetails


class BasePricingStrategy(ABC):
    """
    Abstract interface for agent pricing strategies.
    """

    @abstractmethod
    def calculate_quote(self, request: QuoteRequest, agent_info: AgentInfo) -> QuoteResponse:
        """
        Calculates a dynamic quote based on task request and agent information.
        """
        pass


class DefaultPricingStrategy(BasePricingStrategy):
    """
    Default pricing strategy based on task complexity, duration, and priority.
    """

    def __init__(self, pricing_config: Optional[PricingConfigDetails] = None):
        self.config = pricing_config or PricingConfigDetails()

    def calculate_quote(self, request: QuoteRequest, agent_info: AgentInfo) -> QuoteResponse:
        complexity = (request.estimatedComplexity or "MEDIUM").upper()
        priority = (request.priority or "MEDIUM").upper()

        complexity_mult = self.config.complexity_multipliers.get(complexity, 1.0)
        priority_mult = self.config.priority_multipliers.get(priority, 1.0)

        base_price = self.config.base_price
        calculated_price = round(base_price * complexity_mult * priority_mult, 2)

        # Estimate execution time (base 10s modified by complexity)
        base_time = 10
        estimated_time = int(base_time * complexity_mult)

        confidence = 95.0
        if complexity == "HIGH":
            confidence = 90.0
        elif complexity == "CRITICAL":
            confidence = 85.0

        primary_capability = agent_info.capabilities[0] if agent_info.capabilities else "GENERAL"

        return QuoteResponse(
            price=calculated_price,
            estimatedTime=estimated_time,
            confidence=confidence,
            reputation=agent_info.reputation,
            capability=primary_capability,
            agentId=agent_info.agentId,
            agentName=agent_info.name,
            supportedCapabilities=agent_info.capabilities,
        )
