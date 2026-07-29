import pytest
from fastapi.testclient import TestClient
from agent import ResearchAgent

@pytest.fixture
def client():
    agent = ResearchAgent()
    return TestClient(agent.app)

def test_quote_default(client):
    payload = {
        "description": "Market size for autonomous AI agents",
        "estimatedComplexity": "MEDIUM",
        "priority": "MEDIUM",
    }
    response = client.post("/quote", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "price" in data
    assert data["price"] == 45.0  # 45 * 1.0 * 1.0
    assert data["capability"] == "RESEARCH"
    assert data["agentId"] == "agent-research-01"

def test_quote_high_complexity_and_priority(client):
    payload = {
        "description": "Deep competitor analysis for high latency AI systems",
        "estimatedComplexity": "HIGH",
        "priority": "HIGH",
        "taskType": "Competitor Analysis",
    }
    response = client.post("/quote", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["price"] > 45.0
    assert data["estimatedTime"] > 10
    assert data["confidence"] <= 95.0
