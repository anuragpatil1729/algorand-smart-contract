import pytest
from fastapi.testclient import TestClient
from agent import ResearchAgent

@pytest.fixture
def client():
    agent = ResearchAgent()
    return TestClient(agent.app)

def test_health_endpoint(client):
    response = client.get("/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "HEALTHY"
    assert data["version"] == "1.0.0"
    assert "RESEARCH" in data["capabilities"]
    assert "uptimeSeconds" in data
    assert "cpuUsagePercent" in data
    assert "memoryUsageMb" in data
    assert "runningTasks" in data
    assert "queueSize" in data
    assert data["agentId"] == "agent-research-01"

def test_capabilities_endpoint(client):
    response = client.get("/capabilities")
    assert response.status_code == 200
    data = response.json()
    assert "RESEARCH" in data["supportedCapabilities"]
    assert data["maxConcurrency"] == 5
    assert "Market Research" in data["supportedTaskTypes"]
    assert data["averagePrice"] == 45.0
