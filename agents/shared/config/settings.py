import os
from typing import Dict, Any, List, Optional
import yaml
from pydantic import BaseModel, Field, ConfigDict


class AgentConfigDetails(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    id: str = "agent-01"
    name: str = "Base Agent"
    port: int = 8000
    host: str = "0.0.0.0"
    version: str = "1.0.0"
    wallet: Optional[str] = ""
    reputation: float = 5.0
    capabilities: List[str] = Field(default_factory=list)
    task_types: List[str] = Field(default_factory=list, alias="taskTypes")
    max_concurrency: int = Field(default=5, alias="maxConcurrency")
    base_price: float = Field(default=10.0, alias="basePrice")
    base_execution_time: float = Field(default=10.0, alias="baseExecutionTime")


class PricingConfigDetails(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    base_price: float = Field(default=10.0, alias="basePrice")
    complexity_multipliers: Dict[str, float] = Field(
        default_factory=lambda: {
            "LOW": 0.8,
            "MEDIUM": 1.0,
            "HIGH": 1.4,
            "CRITICAL": 2.0,
        }
    )
    priority_multipliers: Dict[str, float] = Field(
        default_factory=lambda: {
            "LOW": 0.9,
            "MEDIUM": 1.0,
            "HIGH": 1.3,
            "CRITICAL": 1.8,
        }
    )


class AgentConfig(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    agent: AgentConfigDetails = Field(default_factory=AgentConfigDetails)
    pricing: PricingConfigDetails = Field(default_factory=PricingConfigDetails)
    custom: Dict[str, Any] = Field(default_factory=dict)


def load_config(config_path: Optional[str] = None) -> AgentConfig:
    """
    Loads agent configuration from a YAML file or environment variables.
    """
    if config_path and os.path.exists(config_path):
        with open(config_path, "r", encoding="utf-8") as f:
            raw_data = yaml.safe_load(f) or {}
    else:
        raw_data = {}

    # Environment variable overrides
    if os.getenv("AGENT_NAME"):
        raw_data.setdefault("agent", {})["name"] = os.getenv("AGENT_NAME")
    if os.getenv("AGENT_PORT"):
        try:
            raw_data.setdefault("agent", {})["port"] = int(os.getenv("AGENT_PORT"))
        except ValueError:
            pass
    if os.getenv("AGENT_HOST"):
        raw_data.setdefault("agent", {})["host"] = os.getenv("AGENT_HOST")
    if os.getenv("AGENT_VERSION"):
        raw_data.setdefault("agent", {})["version"] = os.getenv("AGENT_VERSION")
    if os.getenv("AGENT_WALLET"):
        raw_data.setdefault("agent", {})["wallet"] = os.getenv("AGENT_WALLET")

    return AgentConfig.model_validate(raw_data)
