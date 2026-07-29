import pytest
from fastapi.testclient import TestClient
from agent import PptAgent

@pytest.fixture
def client():
    agent = PptAgent()
    return TestClient(agent.app)

def test_health(client):
    res = client.get("/health")
    assert res.status_code == 200
    assert res.json()["agentId"] == "agent-ppt-04"

def test_capabilities(client):
    res = client.get("/capabilities")
    assert res.status_code == 200
    assert "PRESENTATION" in res.json()["supportedCapabilities"]

def test_quote(client):
    res = client.post("/quote", json={"description": "Create Pitch Deck", "estimatedComplexity": "HIGH"})
    assert res.status_code == 200
    assert res.json()["price"] > 55.0

def test_execute(client):
    res = client.post("/execute", json={"description": "AgentMesh Series A Pitch Deck", "taskType": "Startup Pitch Deck"})
    assert res.status_code == 200
    assert res.json()["status"] == "COMPLETED"
    assert res.json()["output"]["slideCount"] == 5
