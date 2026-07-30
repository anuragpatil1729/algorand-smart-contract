# AgentMesh x402 Integration Guide (Algorand Testnet)

This guide documents the **x402 pay-per-use agentic payments integration** for AgentMesh. AgentMesh protects paid execution endpoints using the official x402 specification settled on **Algorand Testnet** using **USDC ASA** and verified via the x402 Facilitator.

---

## 1. Overview & Protocol Flow

Every paid workflow execution follows the official x402 challenge-response flow:

```text
Client                          AgentMesh Router                       x402 Facilitator
  │                                    │                                      │
  ├─── 1. POST /api/execution/start ──►│                                      │
  │    (No payment header)             │                                      │
  │                                    ├── Calculates dynamic price           │
  │◄── 2. HTTP 402 Payment Required ───┤   Generates x402 challenge           │
  │    (Headers & Challenge JSON)      │                                      │
  │                                    │                                      │
  ├── 3. Signs Algorand Testnet Tx ────┤                                      │
  │                                    │                                      │
  ├─── 4. POST /api/execution/start ──►│                                      │
  │    (X-402-Payment-Proof Header)    ├──── 5. POST /verify ─────────────────►│
  │                                    │◄─── 6. 200 OK (Verified: true) ──────┤
  │                                    │                                      │
  │                                    ├── Executes Workflow                  │
  │                                    ├── Generates SHA-256 Receipt         │
  │◄── 7. HTTP 200 OK (With Receipt) ──┤                                      │
```

---

## 2. Environment Variables & Configuration

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

## 3. Protected vs Free Endpoints

### Paid Endpoints (Protected by x402 Middleware)
- `POST /api/execution/start`
- `POST /api/workflows/execute`
- `POST /api/workflows/run`

### Free Endpoints (Not Protected)
- `GET /health`
- `POST /api/planner`
- `GET /api/discovery`
- `POST /api/quotes/collect`
- `POST /api/quotes/select`
- `GET /api/registry`
- `GET /api/payments/*`

---

## 4. REST API Endpoints

### Get x402 Receipt by Workflow ID
`GET /api/payments/receipt/{workflowId}`

**Response:**
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

### Get Settlement Metrics
`GET /api/payments/metrics`

**Response:**
```json
{
  "success": true,
  "data": {
    "paidRequestsCount": 42,
    "successfulTransactions": 42,
    "failedPaymentsCount": 0,
    "replayAttemptsBlocked": 1,
    "totalRevenueUSDC": 220.50,
    "averageSettlementTimeMs": 100.0,
    "averageVerificationTimeMs": 45.2
  }
}
```

---

## 5. Client Integration Example

Run the JavaScript demonstration client:

```bash
node scripts/x402-client-example.js
```

---

## 6. Testing Guide

Run the full x402 test suite using Maven:

```bash
cd router-service
mvn test -Dtest=*x402*
```
