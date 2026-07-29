import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

export interface ScoredQuote {
  id: string;
  agentId: string;
  agentName: string;
  price: number;
  estimatedTimeSeconds: number;
  confidence: number;
  successRate: number;
  rating: number;
  score: number;
  selected: boolean;
}

export interface TaskDto {
  id: string;
  workflowId: string;
  taskType: string;
  description: string;
  assignedAgent: string;
  status: 'PENDING' | 'RUNNING' | 'COMPLETED' | 'FAILED' | 'CANCELLED';
  price: number;
  dependencies: string[];
  priority: number;
  estimatedComplexity: string;
  executionTimeMs: number;
  output?: string;
  quotes: ScoredQuote[];
  createdAt?: string;
  completedAt?: string;
}

export interface WorkflowResponse {
  id: string;
  prompt: string;
  status: 'PENDING_APPROVAL' | 'APPROVED' | 'RUNNING' | 'COMPLETED' | 'FAILED' | 'CANCELLED';
  totalPrice: number;
  escrowAddress?: string;
  escrowStatus?: string;
  aggregatedResult?: string;
  tasks: TaskDto[];
  createdAt?: string;
  completedAt?: string;
}

export interface AgentDto {
  id: string;
  name: string;
  endpoint: string;
  walletAddress: string;
  rating: number;
  successRate: number;
  healthStatus: string;
  basePrice: number;
  supportedCapabilities: string[];
}

export interface TransactionDto {
  id: string;
  txHash: string;
  senderWallet: string;
  receiverWallet: string;
  amount: number;
  agentId?: string;
  status: string;
  blockRound: number;
  timestamp: string;
}

export interface PaymentDetailsDto {
  id: string;
  workflowId: string;
  escrowWallet: string;
  totalAmount: number;
  status: string;
  txGroupId: string;
  transactions: TransactionDto[];
  createdAt?: string;
  completedAt?: string;
}

export interface AnalyticsSummary {
  totalAgents: number;
  activeWorkflows: number;
  completedWorkflows: number;
  totalTransactions: number;
  overallSuccessRate: number;
  avgExecutionTimeSeconds: number;
  totalRevenueAlgos: number;
  agentUsageMap: Record<string, number>;
  costDistributionMap: Record<string, number>;
  recentActivity: Array<{ date: string; workflows: number; revenue: number }>;
}

export interface ScoringConfig {
  reputationWeight: number;
  successRateWeight: number;
  confidenceWeight: number;
  priceWeight: number;
  etaWeight: number;
}

export interface ExecutionLog {
  id: string;
  workflowId: string;
  taskId?: string;
  agentId?: string;
  logLevel: string;
  message: string;
  timestamp: string;
}

// API methods
export const api = {
  createWorkflow: async (prompt: string): Promise<WorkflowResponse> => {
    const res = await axios.post(`${API_BASE}/workflows`, { prompt });
    return res.data;
  },

  getWorkflow: async (id: string): Promise<WorkflowResponse> => {
    const res = await axios.get(`${API_BASE}/workflows/${id}`);
    return res.data;
  },

  listWorkflows: async (): Promise<WorkflowResponse[]> => {
    const res = await axios.get(`${API_BASE}/workflows`);
    return res.data;
  },

  approveWorkflow: async (id: string): Promise<WorkflowResponse> => {
    const res = await axios.post(`${API_BASE}/workflows/${id}/approve`);
    return res.data;
  },

  executeWorkflow: async (id: string): Promise<WorkflowResponse> => {
    const res = await axios.post(`${API_BASE}/workflows/${id}/execute`);
    return res.data;
  },

  getAgents: async (capability?: string): Promise<AgentDto[]> => {
    const params = capability ? { capability } : {};
    const res = await axios.get(`${API_BASE}/agents`, { params });
    return res.data;
  },

  registerAgent: async (agent: Partial<AgentDto>): Promise<AgentDto> => {
    const res = await axios.post(`${API_BASE}/agents/register`, agent);
    return res.data;
  },

  getPaymentDetails: async (workflowId: string): Promise<PaymentDetailsDto> => {
    const res = await axios.get(`${API_BASE}/payments/${workflowId}`);
    return res.data;
  },

  getAnalytics: async (): Promise<AnalyticsSummary> => {
    const res = await axios.get(`${API_BASE}/analytics`);
    return res.data;
  },

  getLogs: async (workflowId?: string): Promise<ExecutionLog[]> => {
    const params = workflowId ? { workflowId } : {};
    const res = await axios.get(`${API_BASE}/admin/logs`, { params });
    return res.data;
  },

  getScoringConfig: async (): Promise<ScoringConfig> => {
    const res = await axios.get(`${API_BASE}/admin/scoring-config`);
    return res.data;
  },

  updateScoringConfig: async (config: ScoringConfig): Promise<ScoringConfig> => {
    const res = await axios.post(`${API_BASE}/admin/scoring-config`, config);
    return res.data;
  }
};
