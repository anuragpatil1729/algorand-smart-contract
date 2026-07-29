import pytest
from fastapi.testclient import TestClient
from agent import ResearchAgent

@pytest.fixture
def client():
    agent = ResearchAgent()
    return TestClient(agent.app)

def test_execute_market_research(client):
    payload = {
        "taskId": "task-test-01",
        "taskType": "Market Research",
        "description": "DeFi AI micro-transactions market size",
    }
    response = client.post("/execute", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["taskId"] == "task-test-01"
    assert data["status"] == "COMPLETED"
    assert "executionId" in data
    assert data["executionTime"] >= 0.0
    assert "tam_sam_som" in data["output"]

    # Verify status endpoint
    status_response = client.get("/status/task-test-01")
    assert status_response.status_code == 200
    status_data = status_response.json()
    assert status_data["status"] == "COMPLETED"
    assert status_data["progress"] == 100.0
    assert len(status_data["logs"]) > 0

def test_execute_competitor_analysis(client):
    payload = {
        "taskType": "Competitor Analysis",
        "description": "Autonomous smart contract execution platforms",
    }
    response = client.post("/execute", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "COMPLETED"
    assert "competitors" in data["output"]

def test_status_not_found(client):
    response = client.get("/status/non-existent-task-id")
    assert response.status_code == 404
    assert "Task 'non-existent-task-id' not found" in response.json()["error"]
