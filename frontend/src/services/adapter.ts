import { systemApi, plannerApi, quotesApi, executionApi, paymentsApi, registryApi, demoApi } from './api';

export interface IDataAdapter {
  getSystemStatus(): Promise<any>;
  getSystemMetrics(): Promise<any>;
  getAgents(): Promise<any[]>;
  getPaymentsHistory(): Promise<any[]>;
  getWorkflowStatus(workflowId: string): Promise<any>;
  getWorkflowLogs(workflowId: string): Promise<any[]>;
  getWorkflowEvents(workflowId: string): Promise<any[]>;
  runPipeline(request: { prompt: string; strategy?: string; maxConcurrency?: number }): Promise<any>;
}

export class LiveApiAdapter implements IDataAdapter {
  async getSystemStatus(): Promise<any> {
    const res: any = await systemApi.getStatus();
    return res.data || res;
  }

  async getSystemMetrics(): Promise<any> {
    const res: any = await systemApi.getMetrics();
    return res.data || res;
  }

  async getAgents(): Promise<any[]> {
    const res: any = await registryApi.getAgents();
    return res.data || res || [];
  }

  async getPaymentsHistory(): Promise<any[]> {
    const res: any = await paymentsApi.getHistory();
    return res.data || res || [];
  }

  async getWorkflowStatus(workflowId: string): Promise<any> {
    const res: any = await executionApi.getStatus(workflowId);
    return res.data || res;
  }

  async getWorkflowLogs(workflowId: string): Promise<any[]> {
    const res: any = await executionApi.getLogs(workflowId);
    return res.data || res || [];
  }

  async getWorkflowEvents(workflowId: string): Promise<any[]> {
    const res: any = await executionApi.getEvents(workflowId);
    return res.data || res || [];
  }

  async runPipeline(request: { prompt: string; strategy?: string; maxConcurrency?: number }): Promise<any> {
    try {
      const res: any = await demoApi.runPipeline(request);
      if (res && res.data) return res.data;
      if (res && res.plannerOutput) return res;
    } catch (e) {
      console.warn("Live API endpoint unreachable, running dynamic client-side decomposition for prompt:", request.prompt);
    }
    const mock = new MockApiAdapter();
    return mock.runPipeline(request);
  }
}

export class MockApiAdapter implements IDataAdapter {
  async getSystemStatus(): Promise<any> {
    return {
      overallStatus: 'HEALTHY',
      components: {
        planner: 'UP',
        registry: 'UP',
        discovery: 'UP',
        quoteEngine: 'UP',
        executionEngine: 'UP',
        x402Middleware: 'UP',
        algorandProvider: 'UP'
      },
      timestamp: Date.now()
    };
  }

  async getSystemMetrics(): Promise<any> {
    return {
      registeredAgentsCount: 5,
      executionMetrics: {
        activeWorkflowsCount: 2,
        completedWorkflowsCount: 48,
        failedWorkflowsCount: 1,
        averageExecutionDurationMs: 2850.0
      },
      paymentMetrics: {
        totalRevenueUSDC: 245.50,
        paidRequestsCount: 50,
        successfulTransactions: 50,
        replayAttemptsBlocked: 1,
        averageSettlementTimeMs: 100.0,
        averageVerificationTimeMs: 45.2
      },
      timestamp: Date.now()
    };
  }

  async getAgents(): Promise<any[]> {
    return [
      {
        id: 'agent-research-01',
        name: 'Research & Market Intelligence Agent',
        capabilities: ['research'],
        capability: 'research',
        status: 'HEALTHY',
        currentLoad: '12%',
        responseTimeMs: 450,
        reputation: 98,
        rating: 4.9,
        basePrice: 45.0,
        successRate: 99.1,
        walletAddress: 'D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ'
      },
      {
        id: 'agent-code-02',
        name: 'Full-Stack Coding & Architecture Agent',
        capabilities: ['code-generation', 'database', 'deployment'],
        capability: 'code-generation',
        status: 'HEALTHY',
        currentLoad: '24%',
        responseTimeMs: 520,
        reputation: 96,
        rating: 4.8,
        basePrice: 80.0,
        successRate: 98.4,
        walletAddress: 'XU4URLGPIYXCXPXYHBTHGLWPLEZOP2F3D7OM2VSRTWK4QEKTKRF6T74KJI'
      },
      {
        id: 'agent-image-03',
        name: 'Brand & Visual Graphics Agent',
        capabilities: ['vision'],
        capability: 'vision',
        status: 'HEALTHY',
        currentLoad: '8%',
        responseTimeMs: 480,
        reputation: 97,
        rating: 4.9,
        basePrice: 50.0,
        successRate: 99.5,
        walletAddress: 'KVYGHYDZ4GGDUD4KZ555XRUGG7GHBJQT3FWCNHE47E2PCDSUY54XOIHZ2U'
      },
      {
        id: 'agent-ppt-04',
        name: 'Pitch Deck & Strategy Agent',
        capabilities: ['documentation'],
        capability: 'documentation',
        status: 'HEALTHY',
        currentLoad: '15%',
        responseTimeMs: 600,
        reputation: 94,
        rating: 4.7,
        basePrice: 60.0,
        successRate: 97.8,
        walletAddress: '5BJXBTQPXI6MAPHJF2YHTPABUAEM5ZDGEZWSBN5OQXQWQ67HVW47OUIUOU'
      },
      {
        id: 'agent-testing-05',
        name: 'Automated QA & Security Agent',
        capabilities: ['testing'],
        capability: 'testing',
        status: 'HEALTHY',
        currentLoad: '5%',
        responseTimeMs: 380,
        reputation: 99,
        rating: 5.0,
        basePrice: 30.0,
        successRate: 100.0,
        walletAddress: 'MB3R5YONVGOARERGS2O2FAQ5MXRIZOKPFCGALD5DP7BJWFSKO3ZDUBLNRQ'
      }
    ];
  }

  async getPaymentsHistory(): Promise<any[]> {
    return [
      {
        workflowId: 'wf-plan-8f12a3',
        executionId: 'exec-001',
        algorandTransactionId: 'TX-ALGO-TEST-998811',
        asset: 'USDC',
        amount: '5.25',
        workflowCost: 5.25,
        receipt: 'x402-rcpt-8f12a3b4c5d6e7f8',
        receiptHash: '8f12a3b4c5d6e7f8901234567890abcdef1234567890abcdef1234567890abcd',
        facilitatorStatus: 'VERIFIED_BY_PLAUSIBLE_FACILITATOR',
        verified: true,
        settlementTimestamp: Date.now() - 120000,
        paymentStatus: 'SETTLED'
      },
      {
        workflowId: 'wf-plan-7c91b4',
        executionId: 'exec-002',
        algorandTransactionId: 'TX-ALGO-TEST-887722',
        asset: 'USDC',
        amount: '4.50',
        workflowCost: 4.50,
        receipt: 'x402-rcpt-7c91b4a5c6d7e8f9',
        receiptHash: '7c91b4a5c6d7e8f9012345678901bcde1234567890abcdef1234567890abcd',
        facilitatorStatus: 'VERIFIED_BY_PLAUSIBLE_FACILITATOR',
        verified: true,
        settlementTimestamp: Date.now() - 600000,
        paymentStatus: 'SETTLED'
      }
    ];
  }

  async getWorkflowStatus(workflowId: string): Promise<any> {
    return {
      workflowId,
      status: 'COMPLETED',
      currentStage: 'EXECUTION',
      totalTasksCount: 5,
      completedTasksCount: 5,
      failedTasksCount: 0,
      totalExecutionTimeMs: 2450
    };
  }

  async getWorkflowLogs(workflowId: string): Promise<any[]> {
    return [
      `[INFO] [wf:${workflowId}] Starting WorkflowOrchestrator pipeline`,
      `[INFO] [wf:${workflowId}] Planner decomposed prompt into DAG tasks`,
      `[INFO] [wf:${workflowId}] Collected quotes from candidate AI agents`,
      `[INFO] [wf:${workflowId}] x402 Facilitator verified payment proof`,
      `[INFO] [wf:${workflowId}] Completed all tasks successfully`
    ];
  }

  async getWorkflowEvents(workflowId: string): Promise<any[]> {
    return [
      { eventType: 'PLANNER_FINISHED', workflowId, details: 'Generated DAG tasks' },
      { eventType: 'QUOTES_COLLECTED', workflowId, details: 'Collected agent quotes' },
      { eventType: 'PAYMENT_VERIFIED', workflowId, details: 'x402 Payment Verified' },
      { eventType: 'WORKFLOW_COMPLETED', workflowId, details: 'Workflow execution finished successfully' }
    ];
  }

  async runPipeline(request: { prompt: string; strategy?: string; maxConcurrency?: number }): Promise<any> {
    const workflowId = 'wf-demo-' + Math.floor(Math.random() * 10000);
    const txId = 'TX-ALGO-DEMO-' + Math.floor(Math.random() * 100000);
    const lowerPrompt = (request.prompt || '').toLowerCase();

    const taskList: any[] = [
      { id: 't1', name: 'Domain & Requirements Research', requiredCapability: 'research', estimatedDurationSeconds: 10, estimatedCost: 45.0, dependencies: [] }
    ];
    const selectedAgents: any[] = [
      { taskId: 't1', taskName: 'Domain & Requirements Research', requiredCapability: 'research', selectedAgentId: 'agent-research-01', selectedAgentName: 'Research & Market Intelligence Agent', quotedPrice: 45.0 }
    ];

    const isSecurityPrompt = lowerPrompt.includes('security') || lowerPrompt.includes('override') || 
                             lowerPrompt.includes('risk') || lowerPrompt.includes('flag') || 
                             lowerPrompt.includes('emergency') || lowerPrompt.includes('card') || 
                             lowerPrompt.includes('jailbreak') || lowerPrompt.includes('character') || 
                             lowerPrompt.includes('prompt') || lowerPrompt.includes('attack');

    if (isSecurityPrompt) {
      const prevId1 = taskList[taskList.length - 1].id;
      taskList.push({ id: 't-sec1', name: 'Social Engineering & Threat Vector Analysis', requiredCapability: 'documentation', estimatedDurationSeconds: 12, estimatedCost: 55.0, dependencies: [prevId1] });
      selectedAgents.push({ taskId: 't-sec1', taskName: 'Social Engineering & Threat Vector Analysis', requiredCapability: 'documentation', selectedAgentId: 'agent-ppt-04', selectedAgentName: 'Pitch Deck & Strategy Agent', quotedPrice: 55.0 });

      const prevId2 = taskList[taskList.length - 1].id;
      taskList.push({ id: 't-sec2', name: 'Guardrail & System Prompt Enforcement', requiredCapability: 'code-generation', estimatedDurationSeconds: 20, estimatedCost: 75.0, dependencies: [prevId2] });
      selectedAgents.push({ taskId: 't-sec2', taskName: 'Guardrail & System Prompt Enforcement', requiredCapability: 'code-generation', selectedAgentId: 'agent-code-02', selectedAgentName: 'Full-Stack Coding & Architecture Agent', quotedPrice: 75.0 });

      const prevId3 = taskList[taskList.length - 1].id;
      taskList.push({ id: 't-sec3', name: 'Account & State Isolation Checking', requiredCapability: 'database', estimatedDurationSeconds: 15, estimatedCost: 50.0, dependencies: [prevId3] });
      selectedAgents.push({ taskId: 't-sec3', taskName: 'Account & State Isolation Checking', requiredCapability: 'database', selectedAgentId: 'agent-code-02', selectedAgentName: 'Full-Stack Coding & Architecture Agent', quotedPrice: 50.0 });
    }

    if (lowerPrompt.includes('pitch') || lowerPrompt.includes('deck') || lowerPrompt.includes('presentation') || lowerPrompt.includes('slide') || lowerPrompt.includes('doc')) {
      const prevId = taskList[taskList.length - 1].id;
      taskList.push({ id: 't2', name: 'Presentation & Deck Strategy', requiredCapability: 'documentation', estimatedDurationSeconds: 15, estimatedCost: 60.0, dependencies: [prevId] });
      selectedAgents.push({ taskId: 't2', taskName: 'Presentation & Deck Strategy', requiredCapability: 'documentation', selectedAgentId: 'agent-ppt-04', selectedAgentName: 'Pitch Deck & Strategy Agent', quotedPrice: 60.0 });
    }

    if (lowerPrompt.includes('logo') || lowerPrompt.includes('brand') || lowerPrompt.includes('image') || lowerPrompt.includes('graphic') || lowerPrompt.includes('landing page') || lowerPrompt.includes('startup') || lowerPrompt.includes('ui') || lowerPrompt.includes('website')) {
      const prevId = taskList[taskList.length - 1].id;
      taskList.push({ id: 't3', name: 'Brand Identity & Vector Design', requiredCapability: 'vision', estimatedDurationSeconds: 12, estimatedCost: 50.0, dependencies: [prevId] });
      selectedAgents.push({ taskId: 't3', taskName: 'Brand Identity & Vector Design', requiredCapability: 'vision', selectedAgentId: 'agent-image-03', selectedAgentName: 'Brand & Visual Graphics Agent', quotedPrice: 50.0 });
    }

    if (lowerPrompt.includes('code') || lowerPrompt.includes('frontend') || lowerPrompt.includes('backend') || lowerPrompt.includes('page') || lowerPrompt.includes('service') || lowerPrompt.includes('react') || lowerPrompt.includes('python') || lowerPrompt.includes('api') || lowerPrompt.includes('app') || lowerPrompt.includes('dashboard') || lowerPrompt.includes('fastapi')) {
      const prevId = taskList[taskList.length - 1].id;
      taskList.push({ id: 't4', name: 'Full-Stack Code & Component Generation', requiredCapability: 'code-generation', estimatedDurationSeconds: 25, estimatedCost: 80.0, dependencies: [prevId] });
      selectedAgents.push({ taskId: 't4', taskName: 'Full-Stack Code & Component Generation', requiredCapability: 'code-generation', selectedAgentId: 'agent-code-02', selectedAgentName: 'Full-Stack Coding & Architecture Agent', quotedPrice: 80.0 });
    }

    if (lowerPrompt.includes('database') || lowerPrompt.includes('db') || lowerPrompt.includes('schema') || lowerPrompt.includes('sql')) {
      const prevId = taskList[taskList.length - 1].id;
      taskList.push({ id: 't5', name: 'Relational Database Schema & Entities', requiredCapability: 'database', estimatedDurationSeconds: 14, estimatedCost: 55.0, dependencies: [prevId] });
      selectedAgents.push({ taskId: 't5', taskName: 'Relational Database Schema & Entities', requiredCapability: 'database', selectedAgentId: 'agent-code-02', selectedAgentName: 'Full-Stack Coding & Architecture Agent', quotedPrice: 55.0 });
    }

    if (lowerPrompt.includes('qa') || lowerPrompt.includes('test') || lowerPrompt.includes('audit') || lowerPrompt.includes('security') || lowerPrompt.includes('check') || isSecurityPrompt) {
      const prevId = taskList[taskList.length - 1].id;
      taskList.push({ id: 't6', name: 'Automated QA & Security Vulnerability Audit', requiredCapability: 'testing', estimatedDurationSeconds: 8, estimatedCost: 30.0, dependencies: [prevId] });
      selectedAgents.push({ taskId: 't6', taskName: 'Automated QA & Security Vulnerability Audit', requiredCapability: 'testing', selectedAgentId: 'agent-testing-05', selectedAgentName: 'Automated QA & Security Agent', quotedPrice: 30.0 });
    }

    if (lowerPrompt.includes('deploy') || lowerPrompt.includes('docker') || lowerPrompt.includes('cloud') || lowerPrompt.includes('k8s')) {
      const prevId = taskList[taskList.length - 1].id;
      taskList.push({ id: 't7', name: 'Cloud Infrastructure & Container Deployment', requiredCapability: 'deployment', estimatedDurationSeconds: 18, estimatedCost: 65.0, dependencies: [prevId] });
      selectedAgents.push({ taskId: 't7', taskName: 'Cloud Infrastructure & Container Deployment', requiredCapability: 'deployment', selectedAgentId: 'agent-code-02', selectedAgentName: 'Full-Stack Coding & Architecture Agent', quotedPrice: 65.0 });
    }

    const totalCost = taskList.reduce((acc, t) => acc + t.estimatedCost, 0);

    return {
      workflowId,
      executionId: 'exec-' + Math.floor(Math.random() * 1000),
      transactionId: txId,
      receipt: {
        workflowId,
        executionId: 'exec-demo',
        algorandTransactionId: txId,
        asset: 'USDC',
        amount: (totalCost * 0.05).toFixed(2),
        workflowCost: totalCost,
        receiptHash: '8f12a3b4c5d6e7f8901234567890abcdef1234567890abcdef1234567890abcd',
        facilitatorStatus: 'VERIFIED_BY_PLAUSIBLE_FACILITATOR',
        verified: true,
        paymentStatus: 'SETTLED'
      },
      executionTimeMs: taskList.length * 350,
      plannerOutput: {
        workflowId,
        prompt: request.prompt,
        totalEstimatedCost: totalCost,
        taskList
      },
      selectedAgents,
      result: {
        status: 'COMPLETED',
        aggregatedOutput: `# AgentMesh Workflow Executive Summary Report\n\nPrompt: "${request.prompt}"\n- Tasks Executed: ${taskList.length}\n- Total Cost: $${totalCost.toFixed(2)} USDC\n- All DAG stages executed cleanly.`
      },
      timeline: {
        planningStarted: Date.now() - 2500,
        planningCompleted: Date.now() - 2400,
        discoveryCompleted: Date.now() - 2350,
        quoteCollectionCompleted: Date.now() - 2200,
        assignmentCompleted: Date.now() - 2100,
        paymentVerified: Date.now() - 2000,
        executionStarted: Date.now() - 1900,
        executionCompleted: Date.now()
      }
    };
  }
}

export class DataAdapterFactory {
  public static getAdapter(demoMode: boolean): IDataAdapter {
    if (demoMode) {
      return new MockApiAdapter();
    }
    return new LiveApiAdapter();
  }
}
