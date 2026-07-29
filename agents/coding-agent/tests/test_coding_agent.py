import pytest
from fastapi.testclient import TestClient
from agent import CodingAgent

@pytest.fixture
def client():
    agent = CodingAgent()
    return TestClient(agent.app)

def test_health(client):
    res = client.get("/health")
    assert res.status_code == 200
    assert res.json()["agentId"] == "agent-code-02"

def test_capabilities(client):
    res = client.get("/capabilities")
    assert res.status_code == 200
    assert "CODING" in res.json()["supportedCapabilities"]

def test_quote(client):
    res = client.post("/quote", json={"description": "Build React UI", "estimatedComplexity": "HIGH"})
    assert res.status_code == 200
    assert res.json()["price"] > 80.0

def test_execute(client):
    res = client.post("/execute", json={"description": "React Dashboard Component", "taskType": "Frontend Component"})
    assert res.status_code == 200
    assert res.json()["status"] == "COMPLETED"
    assert "React" in res.json()["output"]["language"]
