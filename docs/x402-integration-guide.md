# AgentMesh x402 Payment Flow & Integration Guide (Algorand Testnet)

This guide documents the **x402 pay-per-call agentic payments submission** for AgentMesh. AgentMesh protects paid execution endpoints using the official x402 specification settled on **Algorand Testnet** using **USDC ASA** and verified via the official **x402 Facilitator**.

---

## 1. 💼 Paying-User Use Case & Business Model

### A. Paying-User Use Case
* **Target Audience**: Product Engineering Teams, Enterprise AI Workflow Developers, Autonomous Agent Orchestrators, and API Consumers.
* **Problem**: Traditional SaaS subscriptions charge fixed monthly rates regardless of usage, while simple LLM APIs lack multi-agent specialization, prompt decomposition, competitive bidding, atomic escrow settlement, and verifiable receipts.
* **Solution**: AgentMesh provides a pay-per-execution platform where complex requests (e.g. *"Generate market intelligence report + create full-stack landing page + design presentation deck + run automated QA"*) are decomposed into specialized sub-tasks, routed to competing AI agents, and settled via micropayments on Algorand.

### B. Real Pay-Per-Call Business Model
* **Dynamic Pay-Per-Call Pricing**:
  $$\text{Total Price (USDC)} = \text{Base Fee (\$1.00)} + (\text{Task Count} \times \text{\$0.50}) + (\text{Compute Time (s)} \times \text{\$0.05})$$
* **Revenue Model & Split**:
  * **Router Platform Fee**: 10% cut per completed workflow call.
  * **Agent Payout Pool**: 90% locked in an Algorand PyTeal atomic escrow smart contract (`smart-contract/contracts/escrow.py`) and split directly among winning AI microservices upon successful task validation.
* **Value Proposition**: Users only pay for completed, validated task outputs with cryptographic receipts; AI agent developers monetize their microservice capabilities on a real-per-call basis.

---

## 2. 🛠️ Tech Stack Mapping

| Layer | Recommended / Permitted Tech Stack | AgentMesh Implementation |
| --- | --- | --- |
| **Client** | `@x402/fetch`, `@x402/avm` | Web UI (`frontend/src/services/adapter.ts`), CLI (`frontend/src/cli.js`), and Node client (`scripts/x402-client-example.js`) |
| **Server** | `@x402/hono`, `@x402/core/server` | Router Service Middleware (`X402PaymentInterceptor.java`, `AlgorandX402Provider.java`) & Node reference server (`scripts/x402-hono-server-example.js`) |
| **Facilitator** | `https://facilitator.goplausible.xyz` | Direct HTTP verification integration (`AlgorandX402Provider.java` -> `/verify`) |
| **Settlement** | **USDC ASA** | Algorand Testnet USDC ASA (Asset ID: `31566704`) with PyTeal Escrow (`escrow.py`) |

---

## 3. 🔄 Protocol Flow (4-Step x402 Standard)

Every protected workflow execution (`POST /api/execution/start`, `POST /api/workflows/execute`) strictly adheres to the 4-step x402 challenge-settlement sequence:

```text
Client (@x402/fetch, @x402/avm)             AgentMesh Router / Server                      x402 Facilitator
  │                                                      │                                              │
  ├─── 1. Challenge: POST /api/execution/start ─────────►│                                              │
  │    (No payment proof header)                         ├── Calculates dynamic price                   │
  │◄── HTTP 402 Payment Required ────────────────────────┤   Generates x402 challenge                   │
  │    (X-402-Payment-Required header & JSON challenge)  │                                              │
  │                                                      │                                              │
  ├─── 2. Client signs & retries ────────────────────────┤                                              │
  │    Signs AVM transaction / payment proof             │                                              │
  │    POST /api/execution/start                         │                                              │
  │    (Header: X-402-Payment-Proof)                     │                                              │
  │                                                      ├────── 3. Verify & Settle: POST /verify ──────►│
  │                                                      │◄───── 200 OK (Verified: true) ───────────────┤
  │                                                      │                                              │
  │                                                      ├── Executes Agent Workflow                    │
  │                                                      ├── Generates SHA-256 Receipt                 │
  │◄── 4. Response with Receipt: HTTP 200 OK ────────────┤                                              │
  │    (JSON output + transaction-linked x402 receipt)   │                                              │
```

---

## 4. ⚙️ Environment Configuration

Configure `router-service/src/main/resources/application.yml` or set environment variables:

| Variable | Default Value | Description |
| --- | --- | --- |
| `AGENTMESH_X402_FACILITATOR_URL` | `https://facilitator.goplausible.xyz` | Official x402 Facilitator verification service endpoint |
| `AGENTMESH_X402_MERCHANT_WALLET` | `D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ` | Algorand Testnet wallet address receiving USDC payouts |
| `AGENTMESH_X402_USDC_ASSET_ID` | `31566704` | Algorand Testnet USDC ASA ID |
| `AGENTMESH_X402_PRICING_BASE_FEE` | `1.00` | Base USDC workflow execution fee |
| `AGENTMESH_X402_PRICING_PER_TASK_FEE` | `0.50` | Per-task USDC fee |
| `AGENTMESH_X402_PRICING_PER_SECOND_FEE` | `0.05` | Per-second compute duration USDC fee |

---

## 5. 🛡️ Protected vs Free Endpoints

### Protected Endpoints (Enforced by x402 Middleware)
- `POST /api/execution/start`
- `POST /api/workflows/execute`
- `POST /api/workflows/run`

### Free Endpoints (Unprotected)
- `GET /health`
- `POST /api/planner`
- `GET /api/discovery`
- `POST /api/quotes/collect`
- `POST /api/quotes/select`
- `GET /api/registry`
- `GET /api/payments/*`

---

## 6. 📜 REST API & Receipt Verification

### Get x402 Receipt by Workflow ID
`GET /api/payments/receipt/{workflowId}`

**Response Output:**
```json
{
  "success": true,
  "data": {
    "workflowId": "wf-x402-demo-1",
    "executionId": "exec-abc12345",
    "algorandTransactionId": "TX-ALGO-TEST-998877",
    "asset": "USDC",
    "amount": "5.25",
    "workflowCost": 5.25,
    "receipt": "x402-rcpt-8f12a3b4c5d6e7f8",
    "receiptHash": "8f12a3b4c5d6e7f8901234567890abcdef1234567890abcdef1234567890abcd",
    "facilitatorStatus": "VERIFIED_BY_PLAUSIBLE_FACILITATOR",
    "verified": true,
    "settlementTimestamp": 1785321000000,
    "paymentStatus": "SETTLED"
  }
}
```

---

## 7. 🧪 Client Verification & Test Suite

Run the JavaScript client demo:
```bash
node scripts/x402-client-example.js
```

Run the complete Maven test suite:
```bash
cd router-service
mvn test -Dtest="ChallengeTest,AlgorandX402ProviderTest,MockFacilitatorTest,PaymentVerificationTest,ReceiptTest,X402MiddlewareTest"
```

