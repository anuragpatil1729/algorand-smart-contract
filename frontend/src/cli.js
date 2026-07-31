#!/usr/bin/env node

import readline from 'readline';
import http from 'http';

// ANSI Formatting Utilities
const c = {
  reset: '\x1b[0m',
  bold: '\x1b[1m',
  dim: '\x1b[2m',
  italic: '\x1b[3m',
  underline: '\x1b[4m',

  red: '\x1b[31m',
  green: '\x1b[32m',
  yellow: '\x1b[33m',
  blue: '\x1b[34m',
  magenta: '\x1b[35m',
  cyan: '\x1b[36m',
  white: '\x1b[37m',
  gray: '\x1b[90m',
};

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

rl.on('close', () => {
  process.exit(0);
});

const askQuestion = (query) =>
  new Promise((resolve) => {
    if (rl.closed) return resolve('5');
    rl.question(query, (answer) => resolve(answer || ''));
  });

const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

const PRESET_PROMPTS = [
  'Create a startup landing page with logo, presentation deck & QA',
  'Build a Python FastAPI microservice with research benchmarks',
  'Full-stack React dashboard with authentication and security audit',
];

function printHeader() {
  console.clear();
  console.log(`${c.cyan}${c.bold}╔══════════════════════════════════════════════════════════════════════════════════╗${c.reset}`);
  console.log(`${c.cyan}${c.bold}║                                                                                  ║${c.reset}`);
  console.log(`${c.cyan}${c.bold}║   🤖  ${c.magenta}AGENTMESH CLI CONTROL PANEL${c.cyan}                                          ║${c.reset}`);
  console.log(`${c.cyan}${c.bold}║   ${c.dim}Multi-Agent Prompt Decomposition · Competitive Quoting · Algorand Escrow${c.cyan}  ║${c.reset}`);
  console.log(`${c.cyan}${c.bold}║                                                                                  ║${c.reset}`);
  console.log(`${c.cyan}${c.bold}╚══════════════════════════════════════════════════════════════════════════════════╝${c.reset}\n`);
}

function printSectionHeader(title, color = c.magenta) {
  console.log(`\n${color}${c.bold}================================================================================${c.reset}`);
  console.log(`${color}${c.bold} ${title}${c.reset}`);
  console.log(`${color}${c.bold}================================================================================${c.reset}`);
}

async function checkBackendHealth() {
  return new Promise((resolve) => {
    const req = http.get('http://localhost:8080/api/system/status', (res) => {
      if (res.statusCode === 200) resolve(true);
      else resolve(false);
    });
    req.on('error', () => resolve(false));
    req.setTimeout(1500, () => {
      req.destroy();
      resolve(false);
    });
  });
}

async function runLivePipeline(prompt, strategy) {
  return new Promise((resolve, reject) => {
    const data = JSON.stringify({ prompt, strategy, maxConcurrency: 5 });
    const req = http.request(
      'http://localhost:8080/api/demo/run',
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Content-Length': Buffer.byteLength(data),
        },
      },
      (res) => {
        let body = '';
        res.on('data', (chunk) => (body += chunk));
        res.on('end', () => {
          try {
            const parsed = JSON.parse(body);
            resolve(parsed.data || parsed);
          } catch (err) {
            reject(err);
          }
        });
      }
    );
    req.on('error', reject);
    req.write(data);
    req.end();
  });
}

/**
 * Fully Dynamic NLP Task Planner Engine
 * Dynamically decomposes ANY user prompt string into tasks, required capabilities, quotes, and execution outputs.
 */
function generateDynamicTasks(prompt) {
  const p = prompt.toLowerCase();
  const tasks = [];
  const subjectName = prompt.length > 40 ? prompt.substring(0, 37) + '...' : prompt;

  // 1. Research & Requirements Task
  tasks.push({
    id: 'task-1',
    name: `Domain & Requirements Analysis: "${subjectName}"`,
    requiredCapability: 'RESEARCH',
    capability: 'RESEARCH',
    estimatedCost: parseFloat((1.20 + (prompt.length % 7) * 0.15).toFixed(2)),
    dependencies: [],
  });

  let taskCount = 1;

  // 2. Visual / Presentation / Branding / UI Layout Task
  if (
    p.includes('logo') ||
    p.includes('ui') ||
    p.includes('design') ||
    p.includes('landing') ||
    p.includes('image') ||
    p.includes('dashboard') ||
    p.includes('visual') ||
    p.includes('brand') ||
    p.includes('graphic') ||
    p.includes('presentation') ||
    p.includes('deck') ||
    p.includes('slide') ||
    p.includes('theme') ||
    p.includes('wall of fame')
  ) {
    taskCount++;
    const isPpt = p.includes('presentation') || p.includes('deck') || p.includes('slide');
    const cap = isPpt ? 'PRESENTATION' : 'IMAGE_GENERATION';
    tasks.push({
      id: `task-${taskCount}`,
      name: isPpt ? `Executive Pitch & Visual Deck Generation` : `Brand & Visual Theme Design`,
      requiredCapability: cap,
      capability: cap,
      estimatedCost: parseFloat((1.80 + (prompt.length % 5) * 0.2).toFixed(2)),
      dependencies: ['task-1'],
    });
  }

  // 3. Core Development / Component Coding Task
  taskCount++;
  let devName = 'Core Component & Application Architecture';
  let devCap = 'CODING';

  if (p.includes('python') || p.includes('fastapi') || p.includes('backend') || p.includes('api') || p.includes('server')) {
    devName = 'Backend Microservice & API Implementation';
  } else if (p.includes('wall of fame') || p.includes('tailwind') || p.includes('html') || p.includes('react') || p.includes('frontend') || p.includes('ui') || p.includes('modal') || p.includes('search')) {
    devName = 'Responsive UI & Interactive Component Coding';
  } else if (p.includes('smart contract') || p.includes('algorand') || p.includes('solidity') || p.includes('chain') || p.includes('escrow')) {
    devName = 'Smart Contract & Escrow Logic';
  }

  tasks.push({
    id: `task-${taskCount}`,
    name: devName,
    requiredCapability: devCap,
    capability: devCap,
    estimatedCost: parseFloat((2.50 + (prompt.length % 9) * 0.25).toFixed(2)),
    dependencies: ['task-1'],
  });

  // 4. Feature / Security / Data Layer / Interactive Filter Task
  if (
    p.includes('auth') ||
    p.includes('login') ||
    p.includes('database') ||
    p.includes('sql') ||
    p.includes('security') ||
    p.includes('benchmark') ||
    p.includes('audit') ||
    p.includes('filter') ||
    p.includes('search') ||
    p.includes('modal')
  ) {
    taskCount++;
    let featureName = 'Interactive Search & Category Filter Logic';
    let cap = 'CODING';

    if (p.includes('auth') || p.includes('login')) {
      featureName = 'Authentication & Access Control Security';
      cap = 'AUTHENTICATION';
    } else if (p.includes('database') || p.includes('sql')) {
      featureName = 'Database Schema & Integration Layer';
      cap = 'DATABASE';
    } else if (p.includes('benchmark') || p.includes('audit')) {
      featureName = 'Performance Benchmarking & Security Audit';
      cap = 'TESTING';
    }

    tasks.push({
      id: `task-${taskCount}`,
      name: featureName,
      requiredCapability: cap,
      capability: cap,
      estimatedCost: parseFloat((1.50 + (prompt.length % 4) * 0.2).toFixed(2)),
      dependencies: [`task-${taskCount - 1}`],
    });
  }

  // 5. Verification & Testing Task
  taskCount++;
  tasks.push({
    id: `task-${taskCount}`,
    name: `End-to-End Validation & Code Review for "${subjectName}"`,
    requiredCapability: 'TESTING',
    capability: 'TESTING',
    estimatedCost: parseFloat((1.00 + (prompt.length % 6) * 0.15).toFixed(2)),
    dependencies: [`task-${taskCount - 1}`],
  });

  return tasks;
}

function generateSimulatedData(prompt, strategy) {
  const workflowId = 'wf-' + Math.random().toString(36).substring(2, 10);
  const txId = 'TX-ALGO-DEMO-' + Math.random().toString(36).substring(2, 10).toUpperCase();

  const tasks = generateDynamicTasks(prompt);

  const agentsMap = {
    RESEARCH: { id: 'agent-research-01', name: 'ResearchAgent-01', endpoint: 'http://localhost:8001' },
    CODING: { id: 'agent-coding-01', name: 'CodingAgent-01', endpoint: 'http://localhost:8002' },
    IMAGE_GENERATION: { id: 'agent-image-01', name: 'ImageAgent-01', endpoint: 'http://localhost:8003' },
    PRESENTATION: { id: 'agent-ppt-01', name: 'PPTAgent-01', endpoint: 'http://localhost:8004' },
    TESTING: { id: 'agent-testing-01', name: 'TestingAgent-01', endpoint: 'http://localhost:8005' },
    DATABASE: { id: 'agent-db-01', name: 'DatabaseAgent-01', endpoint: 'http://localhost:8006' },
    AUTHENTICATION: { id: 'agent-auth-01', name: 'AuthSecurityAgent-01', endpoint: 'http://localhost:8007' },
  };

  const selectedAgents = tasks.map((t) => {
    const cap = t.requiredCapability || t.capability || 'CODING';
    const ag = agentsMap[cap] || { id: 'agent-generic', name: 'CodingAgent-01', endpoint: 'http://localhost:8002' };
    const priceMult = strategy === 'COST_OPTIMIZED' ? 0.85 : strategy === 'QUALITY_FIRST' ? 1.25 : strategy === 'SPEED_OPTIMIZED' ? 1.1 : 1.0;
    const quotedPrice = parseFloat((t.estimatedCost * priceMult).toFixed(2));
    const confidence = strategy === 'QUALITY_FIRST' ? Math.floor(95 + Math.random() * 5) : Math.floor(88 + Math.random() * 9);

    return {
      taskId: t.id,
      taskName: t.name,
      requiredCapability: cap,
      capability: cap,
      selectedAgentId: ag.id,
      selectedAgentName: ag.name,
      selectedAgentEndpoint: ag.endpoint,
      quotedPrice,
      estimatedDuration: Math.floor(700 + Math.random() * 1400),
      confidenceScore: confidence,
      selectionReason: `Optimal score matching capability ${cap} under ${strategy} strategy`,
    };
  });

  const totalAmount = parseFloat(selectedAgents.reduce((acc, a) => acc + a.quotedPrice, 0).toFixed(2));

  return {
    workflowId,
    transactionId: txId,
    executionTimeMs: 3800,
    plannerOutput: {
      workflowId,
      prompt,
      taskList: tasks,
    },
    selectedAgents,
    receipt: {
      transactionId: txId,
      payerWallet: 'DEMOWALLETADDRESS999999999999999999999999999',
      totalAmount,
      escrowAddress: 'ESCROW_CONTRACT_ALG_99999999999999999999',
      status: 'VERIFIED',
      timestamp: new Date().toISOString(),
    },
    result: {
      workflowId,
      status: 'COMPLETED',
      totalTasks: tasks.length,
      successfulTasks: tasks.length,
      taskResults: tasks.reduce((acc, t) => {
        const cap = t.requiredCapability || t.capability || 'CODING';
        const agName = agentsMap[cap]?.name || 'CodingAgent-01';
        acc[t.id] = {
          taskId: t.id,
          status: 'COMPLETED',
          executionTimeMs: Math.floor(500 + Math.random() * 1100),
          resultData: `Artifact successfully generated by ${agName} for step "${t.name}"`,
        };
        return acc;
      }, {}),
    },
  };
}

async function renderPipeline(response, isLive) {
  const { workflowId, plannerOutput, selectedAgents, receipt, result } = response;
  const tasks = plannerOutput?.taskList || [];

  // STAGE 1: ROUTER WORKFLOW PLANNING & DAG
  printSectionHeader('🧠 STAGE 1: ROUTER WORKFLOW PLANNING & DAG DECOMPOSITION', c.cyan);
  console.log(`${c.dim}[Router Service]${c.reset} Generating task execution graph for Workflow ID: ${c.yellow}${c.bold}${workflowId}${c.reset}`);
  await sleep(300);
  console.log(`${c.dim}[Router Service]${c.reset} Dynamically decomposed prompt into ${c.green}${tasks.length} sub-tasks${c.reset}:\n`);

  tasks.forEach((task, idx) => {
    const isLast = idx === tasks.length - 1;
    const prefix = isLast ? '└─' : '├─';
    const cap = task.requiredCapability || task.capability || 'GENERAL_COMPUTATION';
    const depStr = task.dependencies && task.dependencies.length > 0 ? ` (depends on: ${task.dependencies.join(', ')})` : '';
    console.log(`  ${c.cyan}${prefix}${c.reset} [${c.yellow}${task.id}${c.reset}] ${c.bold}${task.name || task.description}${c.reset}`);
    console.log(`      └─ Required Capability: ${c.magenta}${cap}${c.reset}${c.dim}${depStr}${c.reset}`);
  });
  await sleep(400);

  // STAGE 2: AGENT DISCOVERY
  printSectionHeader('📡 STAGE 2: AGENT DISCOVERY & CAPABILITY RESOLUTION', c.blue);
  console.log(`${c.dim}[Discovery Service]${c.reset} Resolving active registered agents across mesh ports...`);
  await sleep(300);

  const rawCapabilities = tasks.map((t) => t.requiredCapability || t.capability || 'GENERAL_COMPUTATION');
  const capabilities = [...new Set(rawCapabilities)];

  capabilities.forEach((cap) => {
    const capStr = String(cap || 'GENERAL_COMPUTATION');
    const assigned = selectedAgents.find((a) => (a.requiredCapability || a.capability) === capStr);
    const agentName = assigned?.selectedAgentName || `${capStr}Agent`;
    const endpoint = assigned?.selectedAgentEndpoint || 'http://localhost:8000';
    console.log(`  ${c.green}✓${c.reset} Capability ${c.magenta}${capStr.padEnd(22)}${c.reset} ──► Matched Agent ${c.cyan}${c.bold}${agentName}${c.reset} (${c.dim}${endpoint}${c.reset})`);
  });
  await sleep(400);

  // STAGE 3: PRICE ESTIMATION & QUOTE SELECTION
  printSectionHeader('💰 STAGE 3: COMPETITIVE QUOTING & PRICE ESTIMATION', c.yellow);
  console.log(`${c.dim}[Quote Collector]${c.reset} Broadcasting bid requests and computing multi-criteria quote scores...\n`);
  await sleep(400);

  let totalCost = 0;
  selectedAgents.forEach((assign) => {
    const price = assign.quotedPrice || 0;
    totalCost += price;
    const conf = assign.confidenceScore || Math.floor(90 + Math.random() * 8);
    const estTime = assign.estimatedDuration || 1200;

    console.log(`  • Task ${c.yellow}${assign.taskId}${c.reset} (${c.bold}${assign.taskName || assign.taskId}${c.reset})`);
    console.log(`    └─ Assigned: ${c.cyan}${assign.selectedAgentName}${c.reset} | Quoted: ${c.green}${price.toFixed(2)} USDC${c.reset} | Confidence: ${c.magenta}${conf}%${c.reset} | Est: ${c.dim}${estTime}ms${c.reset}`);
  });

  console.log(`\n${c.yellow}${c.bold}────────────────────────────────────────────────────────────────────────────────${c.reset}`);
  console.log(`  ${c.bold}💵 TOTAL ESTIMATED WORKFLOW COST:${c.reset} ${c.green}${c.bold}${totalCost.toFixed(2)} USDC${c.reset}`);
  console.log(`${c.yellow}${c.bold}────────────────────────────────────────────────────────────────────────────────${c.reset}`);
  await sleep(500);

  // STAGE 4: TASK ALLOCATION & ALGORAND ESCROW LOCK
  printSectionHeader('⚡ STAGE 4: TASK ALLOCATION & ALGORAND ESCROW LOCK (x402)', c.magenta);
  console.log(`${c.dim}[x402 Facilitator]${c.reset} Issuing Payment Challenge (HTTP 402)...`);
  await sleep(300);
  console.log(`${c.dim}[Algorand Escrow]${c.reset} Locking ${c.green}${totalCost.toFixed(2)} USDC${c.reset} in Escrow Smart Contract...`);
  await sleep(300);
  console.log(`  ${c.green}✓${c.reset} Escrow Address:   ${c.cyan}${receipt?.escrowAddress || 'ESCROW_ALG_CONTRACT_ADDRESS_9999'}${c.reset}`);
  console.log(`  ${c.green}✓${c.reset} Transaction ID:   ${c.yellow}${c.bold}${receipt?.transactionId || 'TX-ALGO-DEMO'}${c.reset}`);
  console.log(`  ${c.green}✓${c.reset} Escrow Status:    ${c.green}${c.bold}${receipt?.status || 'VERIFIED'}${c.reset}`);
  await sleep(400);

  // STAGE 5: AGENT WORKFLOW EXECUTION
  printSectionHeader('🚀 STAGE 5: DISTRIBUTED AGENT WORKFLOW EXECUTION', c.green);
  console.log(`${c.dim}[Workflow Executor]${c.reset} Orchestrating DAG task execution across candidate agents...\n`);
  await sleep(300);

  const taskResultsMap = result?.taskResults || {};
  for (const assign of selectedAgents) {
    const tRes = taskResultsMap[assign.taskId] || {};
    const execTime = tRes.executionTimeMs || assign.estimatedDuration || 1000;
    process.stdout.write(`  ⏳ Executing ${c.yellow}${assign.taskId}${c.reset} via ${c.cyan}${assign.selectedAgentName}${c.reset} ... `);
    await sleep(Math.min(execTime / 3, 300));
    console.log(`${c.green}${c.bold}DONE${c.reset} (${execTime}ms)`);
    if (tRes.resultData) {
      console.log(`     └─ Output: ${c.dim}${tRes.resultData.substring(0, 80)}...${c.reset}`);
    }
  }
  await sleep(400);

  // STAGE 6: FINAL SETTLEMENT RECEIPT
  printSectionHeader('📜 STAGE 6: ALGORAND SETTLEMENT RECEIPT & SUMMARY', c.cyan);
  console.log(`  ${c.bold}Execution Status:${c.reset}       ${c.green}${c.bold}${result?.status || 'COMPLETED'}${c.reset}`);
  console.log(`  ${c.bold}Workflow ID:${c.reset}            ${c.yellow}${workflowId}${c.reset}`);
  console.log(`  ${c.bold}Total Escrow Amount:${c.reset}    ${c.green}${c.bold}${totalCost.toFixed(2)} USDC${c.reset}`);
  console.log(`  ${c.bold}Algorand Payout Tx:${c.reset}     ${c.magenta}${receipt?.transactionId || 'TX-ALGO-DEMO'}${c.reset}`);
  console.log(`  ${c.bold}Atomic Group Settlement:${c.reset} ${c.dim}Released escrow to ${selectedAgents.length} agents in single atomic transaction.${c.reset}`);
  console.log(`${c.cyan}${c.bold}================================================================================${c.reset}\n`);
}

async function mainLoop() {
  while (!rl.closed) {
    printHeader();

    const isLive = await checkBackendHealth();
    if (isLive) {
      console.log(`${c.green}${c.bold}🟢 ROUTER BACKEND IS LIVE${c.reset} ${c.dim}(Connected to Spring Boot router service at http://localhost:8080)${c.reset}\n`);
    } else {
      console.log(`${c.yellow}${c.bold}🟡 ROUTER BACKEND OFFLINE${c.reset} ${c.dim}(http://localhost:8080 unreachable. Running Dynamic NLP Simulation)${c.reset}`);
      console.log(`${c.dim}   Tip: Run './scripts/run.sh' in another terminal to boot live Java Router & Python Agents.${c.reset}\n`);
    }

    console.log(`${c.bold}Select Prompt Input Option:${c.reset}`);
    console.log(`  ${c.cyan}1.${c.reset} Preset: "Create a startup landing page with logo, presentation deck & QA"`);
    console.log(`  ${c.cyan}2.${c.reset} Preset: "Build a Python FastAPI microservice with research benchmarks"`);
    console.log(`  ${c.cyan}3.${c.reset} Preset: "Full-stack React dashboard with authentication and security audit"`);
    console.log(`  ${c.cyan}4.${c.reset} Enter custom prompt`);
    console.log(`  ${c.cyan}5.${c.reset} Exit`);

    const choiceInput = await askQuestion(`\n${c.bold}Enter option (1-5) [default: 1]: ${c.reset}`);
    const choice = choiceInput.trim() || '1';

    if (choice === '5' || choice.toLowerCase() === 'exit' || choice.toLowerCase() === 'q') {
      console.log(`\n${c.magenta}Exiting AgentMesh CLI. Goodbye! 👋${c.reset}\n`);
      rl.close();
      process.exit(0);
    }

    let userPrompt = '';
    if (choice === '1') userPrompt = PRESET_PROMPTS[0];
    else if (choice === '2') userPrompt = PRESET_PROMPTS[1];
    else if (choice === '3') userPrompt = PRESET_PROMPTS[2];
    else if (choice === '4') {
      userPrompt = await askQuestion(`\n${c.bold}Enter your custom prompt: ${c.reset}`);
      if (!userPrompt.trim()) {
        console.log(`${c.red}Prompt cannot be empty.${c.reset}`);
        await sleep(800);
        continue;
      }
    } else {
      console.log(`${c.red}Invalid option.${c.reset}`);
      await sleep(800);
      continue;
    }

    console.log(`\n${c.bold}Select Routing Strategy:${c.reset}`);
    console.log(`  ${c.cyan}1.${c.reset} BALANCED (Default)`);
    console.log(`  ${c.cyan}2.${c.reset} COST_OPTIMIZED`);
    console.log(`  ${c.cyan}3.${c.reset} SPEED_OPTIMIZED`);
    console.log(`  ${c.cyan}4.${c.reset} QUALITY_FIRST`);

    const stratChoiceInput = await askQuestion(`\n${c.bold}Enter strategy (1-4) [default: 1]: ${c.reset}`);
    const stratChoice = stratChoiceInput.trim() || '1';
    const strategyMap = { '1': 'BALANCED', '2': 'COST_OPTIMIZED', '3': 'SPEED_OPTIMIZED', '4': 'QUALITY_FIRST' };
    const strategy = strategyMap[stratChoice] || 'BALANCED';

    console.log(`\n${c.green}🚀 Launching AgentMesh Pipeline for prompt:${c.reset}`);
    console.log(`"${c.bold}${userPrompt}${c.reset}" (Strategy: ${c.magenta}${strategy}${c.reset})\n`);
    await sleep(400);

    let pipelineData;
    if (isLive) {
      try {
        console.log(`${c.dim}[HTTP Request] Sending workflow execution request to http://localhost:8080/api/demo/run ...${c.reset}`);
        pipelineData = await runLivePipeline(userPrompt, strategy);
      } catch (err) {
        console.log(`${c.yellow}[Warning] Live execution failed (${err.message}). Falling back to dynamic simulation mode...${c.reset}`);
        pipelineData = generateSimulatedData(userPrompt, strategy);
      }
    } else {
      pipelineData = generateSimulatedData(userPrompt, strategy);
    }

    await renderPipeline(pipelineData, isLive);

    const nextAction = await askQuestion(`${c.bold}Press ENTER to run another prompt, or type 'exit' to quit: ${c.reset}`);
    if (nextAction.trim().toLowerCase() === 'exit' || nextAction.trim().toLowerCase() === 'q') {
      console.log(`\n${c.magenta}Exiting AgentMesh CLI. Goodbye! 👋${c.reset}\n`);
      rl.close();
      process.exit(0);
    }
  }
}

mainLoop().catch((err) => {
  console.error(`${c.red}Fatal Error:${c.reset}`, err);
  rl.close();
  process.exit(1);
});
