# AgentMesh Generic AI Agent Framework & Research Agent

This directory contains the **Generic AI Agent Framework** (`agents/shared`) and the reference **Research Agent** (`agents/research-agent`) implementation for **AgentMesh**.

---

## 🏗️ Architecture Overview

The framework provides a reusable, modular foundation for building standardized AI agents in Python using FastAPI, Pydantic v2, Loguru, and PyYAML.

Future agents (e.g. Coding Agent, Image Agent, Presentation Agent, Testing Agent) can be built by simply extending `BaseAgent` and implementing the abstract `process_task` method.

### Directory Structure

```text
agents/
├── shared/                     # Generic Agent Framework (Reusable Infrastructure)
│   ├── base/
│   │   └── agent.py            # Abstract BaseAgent & FastAPI Application Builder
│   ├── models/
│   │   └── schemas.py          # Standard Pydantic v2 Models & Schemas
│   ├── config/
│   │   └── settings.py         # YAML Config Loader & Pydantic Validation
│   ├── services/
│   │   ├── health_monitor.py   # CPU, Memory, Uptime & Queue Telemetry
│   │   ├── task_manager.py     # Thread-safe In-Memory Task State Tracker
│   │   ├── pricing_strategy.py # Pluggable Pricing Interface & Default Implementation
│   │   └── execution_engine.py # Task Execution Engine & Lifecycle Orchestration
│   ├── utils/
│   │   └── logging.py          # Loguru Logger & FastAPI Request Middleware
│   └── requirements.txt
│
└── research-agent/             # Research & Market Intelligence Agent Instance
    ├── config/
    │   └── agent.yaml          # YAML Agent Configuration
    ├── agent.py                # ResearchAgent Subclassing BaseAgent
    ├── main.py                 # CLI/Process Entrypoint
    ├── app.py                  # ASGI Compatibility Bridge
    ├── Dockerfile              # Docker Container Definition
    ├── docker-compose.yml      # Multi-container Deployment Example
    ├── requirements.txt        # Agent Dependencies
    └── tests/                  # Pytest Unit & Integration Test Suite
        ├── conftest.py
        ├── test_health.py
        ├── test_capabilities.py
        ├── test_quote.py
        ├── test_execution.py
        ├── test_config.py
        └── test_base_agent.py
```

---

## 🚀 Standard REST Endpoints

Every agent automatically exposes identical REST endpoints:

| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/health` | `GET` | System telemetry (CPU %, Memory MB, Uptime, Active Tasks, Queue Depth) |
| `/capabilities` | `GET` | Capability discovery, max concurrency, supported task types, avg price/time |
| `/quote` | `POST` | Dynamic pricing calculation based on complexity, priority, and duration |
| `/execute` | `POST` | Task execution orchestration, lifecycle state management |
| `/status/{taskId}` | `GET` | Task status, execution logs, progress percentage (0-100), output |

---

## 🛠️ How to Implement a New Agent

To build a new agent (e.g. `CodingAgent`):

1. **Subclass `BaseAgent`**:
   ```python
   from agents.shared import BaseAgent, ExecuteRequest

   class CodingAgent(BaseAgent):
       def __init__(self, config_path: str = "config/agent.yaml"):
           super().__init__(config_path=config_path)

       async def process_task(self, request: ExecuteRequest):
           # Custom domain logic here
           return {"code": "def hello(): pass"}
   ```

2. **Define `config/agent.yaml`**:
   ```yaml
   agent:
     id: agent-coding-01
     name: Code Generation Agent
     port: 8002
     capabilities:
       - CODE_GENERATION
     taskTypes:
       - Refactoring
       - Code Synthesis
   ```

3. **Run Agent**:
   ```python
   if __name__ == "__main__":
       agent = CodingAgent()
       agent.run()
   ```

---

## 🧪 Running Tests

Execute pytest from the root directory:

```bash
PYTHONPATH=. python3 -m pytest agents/research-agent/tests -v
```

---

## 🐳 Docker Deployment

Build and run the Research Agent using Docker Compose:

```bash
docker-compose -f agents/research-agent/docker-compose.yml up --build
```
