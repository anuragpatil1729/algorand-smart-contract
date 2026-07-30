# AgentMesh

**AgentMesh** is a multi-agent AI service platform that discovers, prices, routes, and pays specialized AI agents for sub-tasks of a larger workflow — with payouts settled on **Algorand** through an atomic escrow smart contract.

A user submits a prompt → the router breaks it into tasks → specialized agents (research, coding, image, presentation, testing) bid on those tasks → the best-scoring quotes are selected → the workflow executes → and on completion, an Algorand atomic transaction group releases funds from escrow to every agent's wallet in a single indivisible transfer.

---

## 🏗️ Architecture

```text
                        ┌───────────────────────┐
                        │       Frontend        │
                        │   React + TypeScript  │
                        │   (Vite, Tailwind)     │
                        └───────────┬───────────┘
                                    │ REST / WebSocket
                                    ▼
                        ┌───────────────────────┐
                        │     Router Service     │
                        │   Spring Boot (Java)   │
                        │  Discovery · Quoting ·  │
                        │  Workflow Orchestration │
                        │      · Payments         │
                        └───┬───────────────┬────┘
                            │               │
                 ┌──────────┘               └──────────┐
                 ▼                                     ▼
        ┌──────────────────┐                 ┌───────────────────────┐
        │ PostgreSQL + Redis│                 │  Algorand Smart       │
        │  (state, quotes,  │                 │  Contract (PyTeal)    │
        │  workflows, tasks)│                 │  Escrow + Atomic      │
        └──────────────────┘                 │  Agent Payouts        │
                                              └───────────────────────┘
                            │
        ┌───────────┬───────────┬───────────┬────────────┐
        ▼           ▼           ▼           ▼            ▼
   ┌─────────┐ ┌──────────┐ ┌─────────┐ ┌──────────┐ ┌───────────┐
   │Research │ │  Coding  │ │  Image  │ │   PPT    │ │  Testing  │
   │ Agent   │ │  Agent   │ │  Agent  │ │  Agent   │ │  Agent    │
   │FastAPI  │ │ FastAPI  │ │FastAPI  │ │ FastAPI  │ │ FastAPI   │
   └─────────┘ └──────────┘ └─────────┘ └──────────┘ └───────────┘
```

Each agent shares a common Python framework (`agents/shared`) exposing a standard REST contract (`/health`, `/capabilities`, `/quote`, `/execute`, `/status/{taskId}`), so new agent types can be added by subclassing `BaseAgent`.

---

## ✨ Key Features

- **Workflow decomposition & routing** — the router service splits an incoming prompt into tasks and dispatches them to registered agents.
- **Competitive quoting** — agents return price, confidence, and time estimates; the router scores and selects quotes per task.
- **Algorand escrow payments** — funds are locked in a PyTeal-based escrow contract and released to agents only via an atomic transaction group, guaranteeing all-or-nothing payouts.
- **Agent health & discovery** — a registry tracks agent uptime, success rate, and rating for scoring future quotes.
- **Live dashboard** — a React frontend for submitting workflows, tracking task graphs, and viewing payment/analytics data.
- **Pluggable agent framework** — a shared base class + config-driven capabilities so new agent types are quick to bootstrap.

---

## 🛠️ Tech Stack

| Layer | Technology |
| --- | --- |
| Frontend | React 18, TypeScript, Vite, Tailwind CSS, React Query, React Flow, Recharts |
| Router / Orchestration | Java 17+, Spring Boot 3 (Web, Data JPA, Validation, Actuator, WebSocket), springdoc-openapi |
| Agents | Python, FastAPI, Pydantic v2, Uvicorn, Loguru |
| Data | PostgreSQL 15, Redis 7 |
| Blockchain | Algorand, PyTeal, `py-algorand-sdk` |
| Infra | Docker, Docker Compose, Kubernetes manifests |

---

## 📂 Project Structure

```text
algorand-smart-contract/
├── agents/                  # Python FastAPI agents + shared framework
│   ├── shared/              # BaseAgent, config, models, services
│   ├── research-agent/
│   ├── coding-agent/
│   ├── image-agent/
│   ├── ppt-agent/
│   └── testing-agent/
├── router-service/          # Spring Boot orchestration & payments API
├── smart-contract/          # PyTeal escrow contract + Algorand SDK deploy script
│   ├── contracts/           # escrow.py / compiled escrow.teal
│   └── scripts/             # deploy_escrow.py (atomic group builder)
├── frontend/                # React + TypeScript dashboard
├── database/
│   ├── schema/              # PostgreSQL schema (agents, workflows, tasks, quotes, payments)
│   └── seeds/                # Seed data for agents
├── docs/
│   ├── api/                 # OpenAPI spec
│   └── architecture/        # System design notes
├── infrastructure/          # Docker & Kubernetes configs
├── scripts/                 # run / seed / build / stop helper scripts
└── docker-compose.yml       # Full local stack
```

---

## 🔗 Algorand Escrow Contract

The escrow contract (`smart-contract/contracts/escrow.py`, compiled to `escrow.teal`, PyTeal v6, `Mode.Signature`) enforces a simple but critical rule: payments may only leave the escrow address as part of an **atomic transaction group of size ≥ 2**, preventing any single payout from being executed in isolation.

`smart-contract/scripts/deploy_escrow.py` uses `py-algorand-sdk` to:
1. Build a protocol-fee transaction (router's cut) plus one payment transaction per agent.
2. Compute the Algorand atomic group ID for the full transaction set.
3. Return transaction receipts for the router service / frontend to display.

Contract dependencies:
```text
pyteal>=0.24.0
py-algorand-sdk>=2.0.0
```

---

## 🚀 Getting Started

### Prerequisites
- Docker & Docker Compose
- (Optional, for local dev outside containers) Java 17+, Node 18+, Python 3.11+

### Run the full stack

```bash
git clone https://github.com/anuragpatil1729/algorand-smart-contract.git
cd algorand-smart-contract
./scripts/run.sh
```

This builds and starts Postgres, Redis, the router service, all five agents, and the frontend via `docker-compose.yml`.

| Service | URL |
| --- | --- |
| Frontend | http://localhost:3000 |
| Router API | http://localhost:8080 |
| Research Agent | http://localhost:8001 |
| Coding Agent | http://localhost:8002 |
| Image Agent | http://localhost:8003 |
| PPT Agent | http://localhost:8004 |
| Testing Agent | http://localhost:8005 |

### Seed the database

```bash
./scripts/seed.sh
```

### Compile / inspect the smart contract

```bash
cd smart-contract
pip install -r requirements.txt
python contracts/escrow.py        # regenerates escrow.teal
python scripts/deploy_escrow.py   # simulates an atomic payout group
```

### Run an agent standalone

```bash
cd agents/research-agent
pip install -r requirements.txt
python main.py
```

### Run the frontend standalone

```bash
cd frontend
npm install
npm run dev
```

---

## 📖 API Documentation

- OpenAPI spec: `docs/api/openapi.yaml`
- Interactive Swagger UI (when the router service is running): `http://localhost:8080/swagger-ui.html`

---

## 🧪 Testing

```bash
# Agent framework tests
PYTHONPATH=. python3 -m pytest agents/research-agent/tests -v

# Router service tests
cd router-service && ./mvnw test
```

---

## 📄 License

See [LICENSE](./LICENSE).