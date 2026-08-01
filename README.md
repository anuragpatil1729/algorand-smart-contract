# AgentMesh

**AgentMesh** is a pay-per-use, multi-agent AI orchestration platform. It takes a single natural-language prompt, breaks it into sub-tasks, lets specialized AI microservices bid on those tasks, settles payment on **Algorand Testnet** using the **x402 protocol**, executes the winning agents in parallel, and releases funds atomically through a **PyTeal escrow smart contract**.

> Ask for *"a market intelligence report + a landing page + a pitch deck + automated QA"* and AgentMesh decomposes that into tasks, shops them out to the Research, Coding, Image, PPT, and Testing agents, collects competitive quotes, verifies an on-chain USDC payment, runs everything, and pays every agent out of a single atomic Algorand transaction group.

---

## How it works

1. **Decompose** — a prompt hits `POST /api/demo/run` (or `/api/workflows/execute`) and the router splits it into a task DAG.
2. **Discover & quote** — registered agents are discovered and asked for a price, confidence, and time estimate for each task.
3. **Score & assign** — a multi-criteria scoring engine picks the best quote per task and builds an assignment plan.
4. **Pay (x402)** — protected endpoints return an HTTP 402 challenge; the client settles in USDC on Algorand Testnet, and the proof is verified against the official x402 Facilitator (`https://facilitator.goplausible.xyz`).
5. **Execute** — the winning agents run their tasks in parallel following the DAG, streaming status over WebSocket.
6. **Settle** — on completion, an atomic Algorand transaction group (size ≥ 2) releases funds from escrow to the router's fee address and every contributing agent's wallet in one indivisible transfer, and a signed receipt is generated.

---

## Architecture

```text
                        ┌───────────────────────┐
                        │        Frontend        │
                        │   React + TypeScript   │
                        │    (Vite, Tailwind)    │
                        └───────────┬───────────┘
                                    │ REST / WebSocket
                                    ▼
                        ┌───────────────────────┐
                        │     Router Service     │
                        │   Spring Boot (Java)   │
                        │  Discovery · Quoting ·  │
                        │ Workflow Orchestration │
                        │       · Payments        │
                        └───┬───────────────┬────┘
                            │               │
                 ┌──────────┘               └──────────┐
                 ▼                                      ▼
        ┌──────────────────┐                 ┌───────────────────────┐
        │ PostgreSQL + Redis│                 │   Algorand Smart      │
        │  (state, quotes,  │                 │   Contract (PyTeal)   │
        │  workflows, tasks)│                 │  Escrow + Atomic       │
        └──────────────────┘                 │  Agent Payouts         │
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

All five agents share a common Python framework (`agents/shared`) that exposes a standard REST contract — `/health`, `/capabilities`, `/quote`, `/execute`, `/status/{taskId}` — so new agent types can be added by subclassing `BaseAgent`.

For the full component and sequence diagrams, see [`docs/judge-walkthrough.md`](./docs/judge-walkthrough.md).

---

## Key features

- **Prompt decomposition & DAG routing** — the router breaks an incoming prompt into a task graph and dispatches it to registered agents.
- **Competitive quoting & multi-criteria scoring** — agents return price, confidence, and time; a scoring engine ranks quotes across six weighted parameters.
- **x402 payments on Algorand** — paid endpoints (`/api/execution/start`, `/api/workflows/execute`) enforce HTTP 402 challenges, verified through the official x402 Facilitator and settled on Algorand Testnet in USDC ASA (`31566704`).
- **Atomic escrow payouts** — funds are locked in a PyTeal escrow contract and only ever released via an atomic transaction group, so no single payout can be executed in isolation.
- **One-click demo pipeline** — `POST /api/demo/run` drives the full flow end-to-end: plan → discover → quote → select → pay → execute → receipt.
- **Real-time Mission Control dashboard** — a React + TypeScript UI with an animated React Flow DAG canvas, a build-timeline view, Recharts telemetry, and live WebSocket updates.
- **CLI mode** — the same orchestration flow is available as a terminal-only interactive experience, no browser required.

---

## Tech stack

| Layer | Technology |
| --- | --- |
| Frontend | React 18, TypeScript, Vite, Tailwind CSS, React Query, React Flow, Recharts, Framer Motion |
| Router / orchestration | Java 17+, Spring Boot 3 (Web, Data JPA, Validation, Actuator, WebSocket), springdoc-openapi |
| Agents | Python, FastAPI, Pydantic v2, Uvicorn, Loguru |
| Data | PostgreSQL 15, Redis 7 |
| Blockchain | Algorand, PyTeal, `py-algorand-sdk`, x402 (Algorand/AVM variant) |
| Infra | Docker, Docker Compose, Kubernetes manifests |

---

## Project structure

```text
agentmesh/
├── agents/                  # Python FastAPI agents + shared framework
│   ├── shared/               # BaseAgent, config, models, services
│   ├── research-agent/
│   ├── coding-agent/
│   ├── image-agent/
│   ├── ppt-agent/
│   └── testing-agent/
├── router-service/           # Spring Boot orchestration & payments API
├── smart-contract/           # PyTeal escrow contract + Algorand SDK deploy script
│   ├── contracts/             # escrow.py / compiled escrow.teal
│   └── scripts/                # deploy_escrow.py (atomic group builder)
├── frontend/                 # React + TypeScript dashboard & CLI
├── database/
│   ├── schema/                 # PostgreSQL schema (agents, workflows, tasks, quotes, payments, transactions...)
│   └── seeds/                   # Seed data for agents
├── docs/
│   ├── api/                     # OpenAPI spec
│   ├── architecture/            # System design notes
│   ├── judge-walkthrough.md     # Full architecture + sequence diagrams
│   └── x402-integration-guide.md # x402 payment flow & business model
├── infrastructure/            # Docker & Kubernetes configs
├── scripts/                   # run / seed / build / stop / CLI helper scripts
└── docker-compose.yml         # Full local stack
```

---

## Algorand escrow contract

`smart-contract/contracts/escrow.py` (PyTeal, `Mode.Signature`, compiled to `escrow.teal`) enforces one rule: funds may leave the escrow address **only** as part of an atomic transaction group of size ≥ 2. That makes it structurally impossible for a single payout to be pushed through on its own.

`smart-contract/scripts/deploy_escrow.py` uses `py-algorand-sdk` to:
1. Build a protocol-fee transaction (the router's 10% cut) plus one payment transaction per contributing agent.
2. Group all of them into a single Algorand atomic transaction group and compute the group ID.
3. Submit the group and return transaction receipts for the router service / frontend to display.

Contract dependencies:
```text
pyteal>=0.24.0
py-algorand-sdk>=2.0.0
```

---

## Payments (x402 on Algorand)

Every protected execution endpoint follows the standard 4-step x402 challenge/settlement sequence, using **USDC ASA** on Algorand Testnet and the official x402 Facilitator for proof verification. Pricing is dynamic:

```
Total (USDC) = Base Fee ($1.00) + (Task Count × $0.50) + (Compute Time (s) × $0.05)
```

90% of each payment is escrowed and split among the winning agents via the atomic contract above; the router keeps a 10% platform fee. See [`docs/x402-integration-guide.md`](./docs/x402-integration-guide.md) for the full protocol walkthrough, tech-stack mapping, and reference client/server examples (`scripts/x402-client-example.js`, `scripts/x402-hono-server-example.js`).

---

## Getting started

### Prerequisites
- Docker & Docker Compose
- Optional, for running services outside containers: Java 17+, Node 18+, Python 3.11+

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

### Run a single agent standalone

```bash
cd agents/research-agent
pip install -r requirements.txt
python main.py
```

### Frontend: Web UI vs CLI

AgentMesh's frontend supports two execution modes.

**Web UI** (browser dashboard at http://localhost:3000):
```bash
npm run dev        # or: npm run dev:ui
cd frontend && npm run dev
```

**CLI** (terminal-only, no browser):
```bash
npm run cli        # or: npm run dev -- --cli, or ./scripts/cli.sh
cd frontend && npm run cli
```

In CLI mode you'll be prompted for a prompt (or a preset), a routing strategy, and then watch prompt decomposition, agent discovery, quote pricing, x402/Algorand escrow settlement, task execution, and the atomic payout receipt play out directly in your terminal.

---

## API documentation

- OpenAPI spec: [`docs/api/openapi.yaml`](./docs/api/openapi.yaml)
- Interactive Swagger UI (while the router service is running): http://localhost:8080/swagger-ui.html
- Judge/reviewer walkthrough with full sequence diagrams: [`docs/judge-walkthrough.md`](./docs/judge-walkthrough.md)

---

## Testing

```bash
# Agent framework tests
PYTHONPATH=. python3 -m pytest agents/research-agent/tests -v

# Router service tests
cd router-service && ./mvnw test
```

---

## License

MIT — see [LICENSE](./LICENSE).