import pytest
from typing import Any
from fastapi.testclient import TestClient
from agents.shared.base.agent import BaseAgent
from agents.shared.models.schemas import ExecuteRequest, QuoteRequest, QuoteResponse, AgentInfo
from agents.shared.services.pricing_strategy import BasePricingStrategy

class CustomPricingStrategy(BasePricingStrategy):
    def calculate_quote(self, request: QuoteRequest, agent_info: AgentInfo) -> QuoteResponse:
        return QuoteResponse(
            price=123.45,
            estimatedTime=5,
            confidence=99.9,
            reputation=5.0,
            capability="CUSTOM",
            agentId=agent_info.agentId,
            agentName=agent_info.name,
        )

class DummyTestAgent(BaseAgent):
    def __init__(self):
        super().__init__(pricing_strategy=CustomPricingStrategy())

    async def process_task(self, request: ExecuteRequest) -> Any:
        if "error" in request.description.lower():
            raise ValueError("Simulated domain error")
        return {"result": "success", "echo": request.description}

@pytest.fixture
def dummy_client():
    agent = DummyTestAgent()
    return TestClient(agent.app)

def test_custom_pricing_strategy(dummy_client):
    response = dummy_client.post("/quote", json={"description": "Test task"})
    assert response.status_code == 200
    data = response.json()
    assert data["price"] == 123.45
    assert data["capability"] == "CUSTOM"

def test_execution_failure_handling(dummy_client):
    payload = {"description": "Trigger Error Task"}
    response = dummy_client.post("/execute", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "FAILED"
    assert "Simulated domain error" in data["error"]
