import pytest
from fastapi.testclient import TestClient
from agent import ImageAgent

@pytest.fixture
def client():
    agent = ImageAgent()
    return TestClient(agent.app)

def test_health(client):
    res = client.get("/health")
    assert res.status_code == 200
    assert res.json()["agentId"] == "agent-image-03"

def test_capabilities(client):
    res = client.get("/capabilities")
    assert res.status_code == 200
    assert "IMAGE" in res.json()["supportedCapabilities"]

def test_quote(client):
    res = client.post("/quote", json={"description": "Design Logo", "estimatedComplexity": "MEDIUM"})
    assert res.status_code == 200
    assert res.json()["price"] == 60.0

def test_execute(client):
    res = client.post("/execute", json={"description": "Algorand Agent Logo", "taskType": "Logo Generation"})
    assert res.status_code == 200
    assert res.json()["status"] == "COMPLETED"
    assert "<svg" in res.json()["output"]["svg_artifact"]
