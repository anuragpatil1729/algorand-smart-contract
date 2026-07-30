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
    const res: any = await demoApi.runPipeline(request);
    return res.data || res;
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
        capabilities: ['RESEARCH'],
        capability: 'RESEARCH',
        status: 'HEALTHY',
        currentLoad: '12%',
        responseTimeMs: 850,
        reputation: 98,
        rating: 4.9,
        basePrice: 45.0,
        successRate: 99.1,
        walletAddress: 'R3SEAR...WLLT1'
      },
      {
        id: 'agent-coding-02',
        name: 'Full-Stack Code Generation Agent',
        capabilities: ['FRONTEND', 'BACKEND'],
        capability: 'FRONTEND',
        status: 'HEALTHY',
        currentLoad: '24%',
        responseTimeMs: 1200,
        reputation: 96,
        rating: 4.8,
        basePrice: 80.0,
        successRate: 98.4,
        walletAddress: 'C0D1NG...WLLT2'
      },
      {
        id: 'agent-image-03',
        name: 'Brand & Graphic Design Agent',
        capabilities: ['LOGO_DESIGN'],
        capability: 'LOGO_DESIGN',
        status: 'HEALTHY',
        currentLoad: '8%',
        responseTimeMs: 950,
        reputation: 97,
        rating: 4.9,
        basePrice: 50.0,
        successRate: 99.5,
        walletAddress: '1MAG3S...WLLT3'
      },
      {
        id: 'agent-ppt-04',
        name: 'Presentation & Pitch Deck Agent',
        capabilities: ['PITCH_DECK'],
        capability: 'PITCH_DECK',
        status: 'HEALTHY',
        currentLoad: '15%',
        responseTimeMs: 1100,
        reputation: 94,
        rating: 4.7,
        basePrice: 60.0,
        successRate: 97.8,
        walletAddress: 'PITCHD...WLLT4'
      },
      {
        id: 'agent-testing-05',
        name: 'Automated QA & Security Audit Agent',
        capabilities: ['TESTING'],
        capability: 'TESTING',
        status: 'HEALTHY',
        currentLoad: '5%',
        responseTimeMs: 720,
        reputation: 99,
        rating: 5.0,
        basePrice: 30.0,
        successRate: 100.0,
        walletAddress: 'T3ST1N...WLLT5'
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
      `[INFO] [wf:${workflowId}] Planner decomposed prompt into 5 DAG tasks`,
      `[INFO] [wf:${workflowId}] Collected quotes from candidate AI agents`,
      `[INFO] [wf:${workflowId}] x402 Facilitator verified payment TX-ALGO-TEST-998811`,
      `[INFO] [wf:${workflowId}] Completed all tasks successfully in 2450ms`
    ];
  }

  async getWorkflowEvents(workflowId: string): Promise<any[]> {
    return [
      { eventType: 'PLANNER_FINISHED', workflowId, details: 'Generated DAG with 5 tasks' },
      { eventType: 'QUOTES_COLLECTED', workflowId, details: 'Collected 5 quotes' },
      { eventType: 'PAYMENT_VERIFIED', workflowId, details: 'x402 Payment Verified' },
      { eventType: 'WORKFLOW_COMPLETED', workflowId, details: 'Workflow execution finished successfully' }
    ];
  }

  async runPipeline(request: { prompt: string; strategy?: string; maxConcurrency?: number }): Promise<any> {
    const workflowId = 'wf-demo-' + Math.floor(Math.random() * 10000);
    const txId = 'TX-ALGO-DEMO-' + Math.floor(Math.random() * 100000);
    return {
      workflowId,
      executionId: 'exec-' + Math.floor(Math.random() * 1000),
      transactionId: txId,
      receipt: {
        workflowId,
        executionId: 'exec-demo',
        algorandTransactionId: txId,
        asset: 'USDC',
        amount: '4.50',
        workflowCost: 4.50,
        receiptHash: '8f12a3b4c5d6e7f8901234567890abcdef1234567890abcdef1234567890abcd',
        facilitatorStatus: 'VERIFIED_BY_PLAUSIBLE_FACILITATOR',
        verified: true,
        paymentStatus: 'SETTLED'
      },
      executionTimeMs: 1450,
      plannerOutput: {
        workflowId,
        prompt: request.prompt,
        taskList: [
          { id: 't1', name: 'User & Domain Research', requiredCapability: 'RESEARCH', estimatedDurationSeconds: 10, estimatedCost: 45.0, dependencies: [] },
          { id: 't2', name: 'Pitch Deck & Architecture', requiredCapability: 'PITCH_DECK', estimatedDurationSeconds: 15, estimatedCost: 60.0, dependencies: ['t1'] },
          { id: 't3', name: 'Brand Logo Design', requiredCapability: 'LOGO_DESIGN', estimatedDurationSeconds: 12, estimatedCost: 50.0, dependencies: ['t1'] },
          { id: 't4', name: 'React UI Code Generation', requiredCapability: 'FRONTEND', estimatedDurationSeconds: 25, estimatedCost: 80.0, dependencies: ['t2', 't3'] },
          { id: 't5', name: 'Automated QA Audit', requiredCapability: 'TESTING', estimatedDurationSeconds: 8, estimatedCost: 30.0, dependencies: ['t4'] }
        ]
      },
      selectedAgents: [
        { taskId: 't1', taskName: 'User & Domain Research', requiredCapability: 'RESEARCH', selectedAgentId: 'agent-research-01', selectedAgentName: 'Research & Market Intelligence Agent', quotedPrice: 45.0 },
        { taskId: 't2', taskName: 'Pitch Deck & Architecture', requiredCapability: 'PITCH_DECK', selectedAgentId: 'agent-ppt-04', selectedAgentName: 'Presentation & Pitch Deck Agent', quotedPrice: 60.0 },
        { taskId: 't3', taskName: 'Brand Logo Design', requiredCapability: 'LOGO_DESIGN', selectedAgentId: 'agent-image-03', selectedAgentName: 'Brand & Graphic Design Agent', quotedPrice: 50.0 },
        { taskId: 't4', taskName: 'React UI Code Generation', requiredCapability: 'FRONTEND', selectedAgentId: 'agent-coding-02', selectedAgentName: 'Full-Stack Code Generation Agent', quotedPrice: 80.0 },
        { taskId: 't5', taskName: 'Automated QA Audit', requiredCapability: 'TESTING', selectedAgentId: 'agent-testing-05', selectedAgentName: 'Automated QA & Security Audit Agent', quotedPrice: 30.0 }
      ],
      result: {
        status: 'COMPLETED',
        aggregatedOutput: '# AgentMesh Workflow Executive Summary Report\n\n- Tasks Executed: 5\n- Total Cost: $265.00 USDC\n- All DAG stages executed cleanly.'
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
