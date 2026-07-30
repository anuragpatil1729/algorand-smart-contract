export interface PlannedTaskDto {
  id: string;
  name?: string;
  description: string;
  requiredCapability: string;
  estimatedDurationSeconds: number;
  estimatedCost: number;
  dependencies: string[];
  priority?: number;
  estimatedComplexity?: string;
}

export interface WorkflowPlanResponseDto {
  workflowId: string;
  prompt: string;
  taskList: PlannedTaskDto[];
  executionStages: Record<number, PlannedTaskDto[]>;
  parallelGroups: string[][];
  totalEstimatedDurationSeconds: number;
  totalEstimatedCost: number;
  requiredCapabilities: string[];
  graphRepresentation: Record<string, any>;
  warnings: string[];
  missingCapabilities: string[];
}

export interface TaskAssignment {
  taskId: string;
  taskName: string;
  requiredCapability: string;
  selectedAgentId: string;
  selectedAgentName: string;
  quotedPrice: number;
  estimatedDuration: number;
  confidenceScore?: number;
  selectionReasoning?: string;
}

export interface AssignmentPlan {
  workflowId: string;
  selectionStrategyUsed: string;
  totalQuotedPrice: number;
  totalEstimatedDuration: number;
  assignments: TaskAssignment[];
}

export interface X402Challenge {
  challengeId: string;
  workflowId: string;
  price: number;
  asset: string;
  assetId: string;
  network: string;
  merchantWallet: string;
  facilitatorUrl: string;
  expiresAt: number;
  requirements?: Record<string, any>;
}

export interface X402PaymentProof {
  challengeId: string;
  transactionId: string;
  senderAddress: string;
  amount: number;
  asset: string;
  signature: string;
  timestamp?: number;
}

export interface X402Receipt {
  workflowId: string;
  executionId: string;
  algorandTransactionId: string;
  asset: string;
  amount: string;
  workflowCost: number;
  receipt: string;
  receiptHash: string;
  facilitatorStatus: string;
  verified: boolean;
  settlementTimestamp: number;
  paymentStatus: string;
}

export interface WorkflowExecutionStatusDto {
  workflowId: string;
  status: string;
  currentStage: string;
  totalTasksCount: number;
  completedTasksCount: number;
  failedTasksCount: number;
  totalExecutionTimeMs: number;
}

export interface WorkflowResult {
  workflowId: string;
  status: string;
  totalExecutionTimeMs: number;
  aggregatedOutput: string;
  validationReport?: Record<string, any>;
}

export interface WorkflowTimeline {
  planningStarted: number;
  planningCompleted: number;
  discoveryCompleted: number;
  quoteCollectionCompleted: number;
  assignmentCompleted: number;
  paymentVerified: number;
  executionStarted: number;
  executionCompleted: number;
}

export interface UnifiedWorkflowRequest {
  prompt: string;
  strategy?: string;
  maxConcurrency?: number;
  paymentProof?: X402PaymentProof;
}

export interface UnifiedWorkflowResponse {
  workflowId: string;
  executionId: string;
  transactionId: string;
  receipt: X402Receipt;
  executionTimeMs: number;
  plannerOutput: WorkflowPlanResponseDto;
  selectedAgents: TaskAssignment[];
  quoteSummary: any[];
  executionSummary: WorkflowExecutionStatusDto;
  result: WorkflowResult;
  timeline: WorkflowTimeline;
  validationReport?: Record<string, any>;
}

export interface SystemHealthDto {
  overallStatus: string;
  components: Record<string, string>;
  timestamp: number;
}

export interface SystemMetricsDto {
  executionMetrics: Record<string, any>;
  paymentMetrics: Record<string, any>;
  registeredAgentsCount: number;
  timestamp: number;
}
