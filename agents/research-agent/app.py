import os
import time
import random
import uuid
from typing import Dict, Any, List, Optional
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

app = FastAPI(title="Research & Market Intelligence Agent", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# In-memory task tracking
tasks_db: Dict[str, Dict[str, Any]] = {}

class QuoteRequest(BaseModel):
    taskId: str
    taskType: str
    description: str
    prompt: Optional[str] = None
    estimatedComplexity: Optional[str] = "MEDIUM"

class QuoteResponse(BaseModel):
    agentId: str = "agent-research-01"
    agentName: str = "Research & Market Intelligence Agent"
    price: float
    estimatedTime: int # in seconds
    confidence: float # percentage 0-100
    successRate: float
    rating: float
    supportedCapabilities: List[str]

class ExecuteRequest(BaseModel):
    taskId: str
    taskType: str
    description: str
    prompt: Optional[str] = None
    inputContext: Optional[Dict[str, Any]] = None

@app.get("/health")
def health_check():
    return {
        "status": "UP",
        "agentId": "agent-research-01",
        "agentName": "Research & Market Intelligence Agent",
        "service": "ResearchAgentService",
        "uptimeSeconds": 3600
    }

@app.post("/quote", response_model=QuoteResponse)
def get_quote(request: QuoteRequest):
    complexity_multiplier = 1.0
    if request.estimatedComplexity == "HIGH":
        complexity_multiplier = 1.4
    elif request.estimatedComplexity == "LOW":
        complexity_multiplier = 0.8

    base_price = 45.0 * complexity_multiplier
    estimated_time = int(12 * complexity_multiplier)
    
    return QuoteResponse(
        agentId="agent-research-01",
        agentName="Research & Market Intelligence Agent",
        price=round(base_price + random.uniform(-2, 5), 2),
        estimatedTime=estimated_time,
        confidence=96.5,
        successRate=98.5,
        rating=4.9,
        supportedCapabilities=["RESEARCH", "MARKET_ANALYSIS", "COMPETITOR_RESEARCH", "SUMMARY"]
    )

@app.post("/execute")
def execute_task(request: ExecuteRequest):
    task_id = request.taskId
    tasks_db[task_id] = {
        "status": "RUNNING",
        "startTime": time.time(),
        "output": None
    }
    
    # Simulate intelligent research synthesis
    topic = request.description or request.prompt or "Target Domain"
    output_content = (
        f"# Executive Research Report: {topic}\n\n"
        f"## 1. Industry Landscape & Market Opportunity\n"
        f"- **Total Addressable Market (TAM)**: $42.5 Billion globally with a 18.4% CAGR.\n"
        f"- **Key Market Drivers**: Autonomous agent workflows, micro-transaction settlement via Algorand, decentralized service discovery.\n"
        f"- **User Persona**: Enterprise innovation teams, Web3 developers, and autonomous DAO treasuries.\n\n"
        f"## 2. Competitive Matrix & Differentiation\n"
        f"- **Competitor A (Legacy Routers)**: High latency, central payment escrow, 15% platform fees.\n"
        f"- **Competitor B (Basic AI Agents)**: Single-agent silos, no atomic payment guarantees.\n"
        f"- **AgentMesh Value Proposition**: Instant atomic settlement on Algorand, dynamic quality scoring, zero counterparty risk.\n\n"
        f"## 3. Strategic Action Plan\n"
        f"1. Target early-adopter Web3 projects requiring verifiable agent execution.\n"
        f"2. Utilize Algorand's 3.3s finality for instant multi-agent fee distribution.\n"
        f"3. Expand capability marketplace to support custom containerized microservices."
    )
    
    tasks_db[task_id] = {
        "status": "COMPLETED",
        "completedAt": time.time(),
        "output": output_content
    }
    
    return {
        "taskId": task_id,
        "status": "COMPLETED",
        "agentId": "agent-research-01",
        "output": output_content
    }

@app.get("/status/{taskId}")
def get_task_status(taskId: str):
    if taskId not in tasks_db:
        raise HTTPException(status_code=404, detail="Task not found")
    return tasks_db[taskId]

if __name__ == "__main__":
    import uvicorn
    port = int(os.environ.get("PORT", 8001))
    uvicorn.run(app, host="0.0.0.0", port=port)
