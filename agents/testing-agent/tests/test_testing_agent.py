import pytest
from fastapi.testclient import TestClient
from agent import TestingAgent

@pytest.fixture
def client():
    agent = TestingAgent()
    return TestClient(agent.app)

def test_health(client):
    res = client.get("/health")
    assert res.status_code == 200
    assert res.json()["agentId"] == "agent-testing-05"

def test_capabilities(client):
    res = client.get("/capabilities")
    assert res.status_code == 200
    assert "TESTING" in res.json()["supportedCapabilities"]

def test_quote(client):
    res = client.post("/quote", json={"description": "Audit Smart Contract", "estimatedComplexity": "MEDIUM"})
    assert res.status_code == 200
    assert res.json()["price"] == 35.0

def test_execute(client):
    res = client.post("/execute", json={"description": "Algorand Escrow Contract Audit", "taskType": "Security Vulnerability Audit"})
    assert res.status_code == 200
    assert res.json()["status"] == "COMPLETED"
    assert res.json()["output"]["certification"]["auditStatus"] == "PASSED"
