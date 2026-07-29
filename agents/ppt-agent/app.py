import os
import time
import random
from typing import Dict, Any, List, Optional
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

app = FastAPI(title="Pitch Deck & Strategy Agent", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

tasks_db: Dict[str, Dict[str, Any]] = {}

class QuoteRequest(BaseModel):
    taskId: str
    taskType: str
    description: str
    prompt: Optional[str] = None
    estimatedComplexity: Optional[str] = "MEDIUM"

class QuoteResponse(BaseModel):
    agentId: str = "agent-ppt-04"
    agentName: str = "Pitch Deck & Strategy Agent"
    price: float
    estimatedTime: int
    confidence: float
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
        "agentId": "agent-ppt-04",
        "agentName": "Pitch Deck & Strategy Agent",
        "service": "PptAgentService",
        "uptimeSeconds": 3600
    }

@app.post("/quote", response_model=QuoteResponse)
def get_quote(request: QuoteRequest):
    complexity_multiplier = 1.0
    if request.estimatedComplexity == "HIGH":
        complexity_multiplier = 1.35
    elif request.estimatedComplexity == "LOW":
        complexity_multiplier = 0.8

    base_price = 55.0 * complexity_multiplier
    estimated_time = int(11 * complexity_multiplier)
    
    return QuoteResponse(
        agentId="agent-ppt-04",
        agentName="Pitch Deck & Strategy Agent",
        price=round(base_price + random.uniform(-3, 5), 2),
        estimatedTime=estimated_time,
        confidence=95.5,
        successRate=94.5,
        rating=4.75,
        supportedCapabilities=["PRESENTATION", "PITCH_DECK", "BUSINESS_PLAN", "SLIDE_GENERATION"]
    )

@app.post("/execute")
def execute_task(request: ExecuteRequest):
    task_id = request.taskId
    tasks_db[task_id] = {"status": "RUNNING", "startTime": time.time(), "output": None}
    
    title = request.description or request.prompt or "AgentMesh Pitch Deck"
    
    slides = [
        {"slide": 1, "title": f"{title} - The Autonomous Future", "type": "TITLE", "content": "Multi-agent orchestration backed by Algorand Atomic Payments"},
        {"slide": 2, "title": "The Problem", "type": "PROBLEM", "content": "AI agents operate in silos with high payment friction, zero inter-agent trust, and slow legacy payouts."},
        {"slide": 3, "title": "The AgentMesh Solution", "type": "SOLUTION", "content": "Data-driven router matching prompt DAGs to specialized agents with Algorand Escrow & Atomic Group Transfers."},
        {"slide": 4, "title": "Business Model & Unit Economics", "type": "FINANCIALS", "content": "1.5% routing protocol fee per atomic transaction group. Projected $12M ARR by Year 2."},
        {"slide": 5, "title": "Execution Roadmap & Next Milestones", "type": "ROADMAP", "content": "Q3: Mainnet Escrow Deployment. Q4: Cross-chain micro-settlements & SDK releases."}
    ]
    
    output_content = (
        f"# Startup Pitch Deck Strategy: {title}\n\n"
        f"```json\n"
        f"{{\n"
        f'  "deckTitle": "{title}",\n'
        f'  "slideCount": {len(slides)},\n'
        f'  "slides": [\n'
    )
    for idx, slide in enumerate(slides):
        comma = "," if idx < len(slides)-1 else ""
        output_content += f'    {{\n      "slideNumber": {slide["slide"]},\n      "title": "{slide["title"]}",\n      "type": "{slide["type"]}",\n      "content": "{slide["content"]}"\n    }}{comma}\n'
    output_content += "  ]\n}\n```"
    
    tasks_db[task_id] = {"status": "COMPLETED", "completedAt": time.time(), "output": output_content}
    return {"taskId": task_id, "status": "COMPLETED", "agentId": "agent-ppt-04", "output": output_content}

@app.get("/status/{taskId}")
def get_task_status(taskId: str):
    if taskId not in tasks_db:
        raise HTTPException(status_code=404, detail="Task not found")
    return tasks_db[taskId]

if __name__ == "__main__":
    import uvicorn
    port = int(os.environ.get("PORT", 8004))
    uvicorn.run(app, host="0.0.0.0", port=port)
