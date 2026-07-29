import os
import time
import random
from typing import Dict, Any, List, Optional
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

app = FastAPI(title="Brand & Visual Graphics Agent", version="1.0.0")

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
    agentId: str = "agent-image-03"
    agentName: str = "Brand & Visual Graphics Agent"
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
        "agentId": "agent-image-03",
        "agentName": "Brand & Visual Graphics Agent",
        "service": "ImageAgentService",
        "uptimeSeconds": 3600
    }

@app.post("/quote", response_model=QuoteResponse)
def get_quote(request: QuoteRequest):
    complexity_multiplier = 1.0
    if request.estimatedComplexity == "HIGH":
        complexity_multiplier = 1.3
    elif request.estimatedComplexity == "LOW":
        complexity_multiplier = 0.85

    base_price = 60.0 * complexity_multiplier
    estimated_time = int(10 * complexity_multiplier)
    
    return QuoteResponse(
        agentId="agent-image-03",
        agentName="Brand & Visual Graphics Agent",
        price=round(base_price + random.uniform(-2, 4), 2),
        estimatedTime=estimated_time,
        confidence=98.0,
        successRate=99.0,
        rating=4.95,
        supportedCapabilities=["LOGO_DESIGN", "BRANDING", "UI_UX", "GRAPHICS", "SVG_GENERATION"]
    )

@app.post("/execute")
def execute_task(request: ExecuteRequest):
    task_id = request.taskId
    tasks_db[task_id] = {"status": "RUNNING", "startTime": time.time(), "output": None}
    
    brand_name = request.description or request.prompt or "AgentMesh Venture"
    
    # Generate standalone SVG logo
    svg_code = (
        f'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 400 400" width="100%" height="100%">\n'
        f'  <defs>\n'
        f'    <linearGradient id="meshGrad" x1="0%" y1="0%" x2="100%" y2="100%">\n'
        f'      <stop offset="0%" stop-color="#06b6d4" />\n'
        f'      <stop offset="50%" stop-color="#6366f1" />\n'
        f'      <stop offset="100%" stop-color="#a855f7" />\n'
        f'    </linearGradient>\n'
        f'    <filter id="glow" x="-20%" y="-20%" width="140%" height="140%">\n'
        f'      <feGaussianBlur stdDeviation="8" result="blur" />\n'
        f'      <feComposite in="SourceGraphic" in2="blur" operator="over" />\n'
        f'    </filter>\n'
        f'  </defs>\n'
        f'  <rect width="400" height="400" rx="32" fill="#0f172a" />\n'
        f'  <g filter="url(#glow)">\n'
        f'    <polygon points="200,60 310,130 310,270 200,340 90,270 90,130" fill="none" stroke="url(#meshGrad)" stroke-width="8" />\n'
        f'    <circle cx="200" cy="60" r="12" fill="#06b6d4" />\n'
        f'    <circle cx="310" cy="130" r="12" fill="#6366f1" />\n'
        f'    <circle cx="310" cy="270" r="12" fill="#a855f7" />\n'
        f'    <circle cx="200" cy="340" r="12" fill="#ec4899" />\n'
        f'    <circle cx="90" cy="270" r="12" fill="#3b82f6" />\n'
        f'    <circle cx="90" cy="130" r="12" fill="#14b8a6" />\n'
        f'    <circle cx="200" cy="200" r="28" fill="url(#meshGrad)" />\n'
        f'    <line x1="200" y1="200" x2="200" y2="60" stroke="url(#meshGrad)" stroke-width="3" opacity="0.7" />\n'
        f'    <line x1="200" y1="200" x2="310" y2="130" stroke="url(#meshGrad)" stroke-width="3" opacity="0.7" />\n'
        f'    <line x1="200" y1="200" x2="310" y2="270" stroke="url(#meshGrad)" stroke-width="3" opacity="0.7" />\n'
        f'    <line x1="200" y1="200" x2="200" y2="340" stroke="url(#meshGrad)" stroke-width="3" opacity="0.7" />\n'
        f'    <line x1="200" y1="200" x2="90" y2="270" stroke="url(#meshGrad)" stroke-width="3" opacity="0.7" />\n'
        f'    <line x1="200" y1="200" x2="90" y2="130" stroke="url(#meshGrad)" stroke-width="3" opacity="0.7" />\n'
        f'  </g>\n'
        f'  <text x="200" y="375" text-anchor="middle" fill="#f8fafc" font-family="sans-serif" font-size="20" font-weight="bold" letter-spacing="2">AGENTMESH BRAND</text>\n'
        f'</svg>'
    )
    
    output_content = (
        f"# Brand Identity Package: {brand_name}\n\n"
        f"## 1. Vector Logo Artifact (SVG)\n```xml\n{svg_code}\n```\n\n"
        f"## 2. Color Palette & Aesthetics\n"
        f"- **Primary Accent**: Electric Cyan (`#06b6d4`)\n"
        f"- **Secondary Accent**: Deep Indigo (`#6366f1`)\n"
        f"- **Highlight**: Radiant Purple (`#a855f7`)\n"
        f"- **Background Canvas**: Slate Dark (`#0f172a`)\n\n"
        f"## 3. Typography & Micro-Interactions\n"
        f"- **Header Font**: Plus Jakarta Sans / Inter SemiBold\n"
        f"- **Body Font**: Roboto / JetBrains Mono (for code & contract hashes)\n"
        f"- **Visual Styling**: Glassmorphic borders, soft backdrops (`backdrop-blur-md`), 3.3s pulse effects indicating active Algorand consensus."
    )
    
    tasks_db[task_id] = {"status": "COMPLETED", "completedAt": time.time(), "output": output_content}
    return {"taskId": task_id, "status": "COMPLETED", "agentId": "agent-image-03", "output": output_content}

@app.get("/status/{taskId}")
def get_task_status(taskId: str):
    if taskId not in tasks_db:
        raise HTTPException(status_code=404, detail="Task not found")
    return tasks_db[taskId]

if __name__ == "__main__":
    import uvicorn
    port = int(os.environ.get("PORT", 8003))
    uvicorn.run(app, host="0.0.0.0", port=port)
