/**
 * AgentMesh x402 Client Example (Algorand Testnet USDC ASA)
 * 
 * Demonstrates:
 * 1. Making paid API execution request to AgentMesh Router
 * 2. Receiving HTTP 402 Payment Required challenge
 * 3. Signing Algorand Testnet transaction / proof using x402 conventions
 * 4. Retrying request with X-402-Payment-Proof header
 * 5. Receiving completed workflow result & Algorand settlement receipt
 */

const ROUTER_API = process.env.ROUTER_API || 'http://localhost:8080';

async function runPaidWorkflow() {
  console.log('🚀 AgentMesh x402 Client Example Starting...\n');

  const executionPayload = {
    workflowId: 'wf-x402-demo-' + Math.floor(Math.random() * 1000),
    assignmentPlan: {
      workflowId: 'wf-x402-demo-1',
      selectionStrategyUsed: 'BALANCED',
      totalQuotedPrice: 4.50,
      totalEstimatedDuration: 25,
      assignments: [
        {
          taskId: 'task-1',
          taskName: 'Market Research',
          requiredCapability: 'RESEARCH',
          selectedAgentId: 'agent-research-01',
          selectedAgentName: 'Research & Market Intelligence Agent',
          quotedPrice: 45.0,
          estimatedDuration: 10
        }
      ]
    }
  };

  // Step 1: Request paid execution without payment proof
  console.log('Step 1: Requesting paid execution endpoint POST /api/execution/start ...');
  let response = await fetch(`${ROUTER_API}/api/execution/start`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(executionPayload)
  });

  console.log(`Response Status: ${response.status} ${response.statusText}`);

  if (response.status === 402) {
    const challengeHeader = response.headers.get('X-402-Payment-Required');
    const challengeId = response.headers.get('X-402-Challenge-Id');
    const price = response.headers.get('X-402-Price');
    const asset = response.headers.get('X-402-Asset');
    const body = await response.json();

    console.log('\n💡 HTTP 402 Payment Required received!');
    console.log(`- Challenge ID: ${challengeId}`);
    console.log(`- Price: $${price} ${asset}`);
    console.log(`- Facilitator: ${body.challenge.facilitatorUrl}`);
    console.log(`- Merchant Wallet: ${body.challenge.merchantWallet}`);
    console.log(`- Network: ${body.challenge.network} (USDC ASA: ${body.challenge.assetId})`);

    // Step 2: Client signs payment proof (Simulated Algorand Testnet Tx for Client Demo)
    console.log('\nStep 2: Client signing payment proof for challenge ...');
    const txId = 'TX-ALGO-TEST-' + Math.floor(Math.random() * 1000000);
    const paymentProof = {
      challengeId: challengeId,
      transactionId: txId,
      senderAddress: 'XU4URLGPIYXCXPXYHBTHGLWPLEZOP2F3D7OM2VSRTWK4QEKTKRF6T74KJI',
      amount: parseFloat(price),
      asset: asset,
      signature: 'sig-avm-' + Math.floor(Math.random() * 1000000)
    };

    // Step 3: Retry request with X-402-Payment-Proof header
    console.log('\nStep 3: Retrying request with X-402-Payment-Proof header ...');
    response = await fetch(`${ROUTER_API}/api/execution/start`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-402-Payment-Proof': JSON.stringify(paymentProof)
      },
      body: JSON.stringify(executionPayload)
    });

    console.log(`Response Status: ${response.status} ${response.statusText}`);
    const result = await response.json();

    if (response.status === 200 && result.success) {
      console.log('\n✅ Workflow Execution Completed & Settled Successfully!');
      console.log(`- Workflow ID: ${result.data.workflowId}`);
      console.log(`- Status: ${result.data.status}`);
      console.log(`- Total Execution Time: ${result.data.totalExecutionTimeMs} ms`);

      if (result.data.validationReport && result.data.validationReport.x402Receipt) {
        const rcpt = result.data.validationReport.x402Receipt;
        console.log('\n🧾 Algorand Testnet Settlement Receipt:');
        console.log(`  - Receipt: ${rcpt.receipt}`);
        console.log(`  - Algorand Tx ID: ${rcpt.algorandTransactionId}`);
        console.log(`  - Asset / Amount: ${rcpt.amount} ${rcpt.asset}`);
        console.log(`  - Receipt Hash: ${rcpt.receiptHash}`);
        console.log(`  - Facilitator Status: ${rcpt.facilitatorStatus}`);
      }
    } else {
      console.error('❌ Execution failed:', result);
    }
  } else {
    console.log('Received non-402 response:', await response.text());
  }
}

runPaidWorkflow().catch(console.error);
