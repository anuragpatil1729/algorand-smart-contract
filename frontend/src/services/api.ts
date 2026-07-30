/// <reference types="vite/client" />
import axios, { AxiosInstance } from 'axios';

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const apiClient: AxiosInstance = axios.create({
  baseURL: BASE_URL,
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.response.use(
  (response) => response.data,
  (error) => {
    console.warn('API Error Intercepted:', error?.response?.data || error.message);
    return Promise.reject(error);
  }
);

export const systemApi = {
  getStatus: () => apiClient.get('/api/system/status'),
  getMetrics: () => apiClient.get('/api/system/metrics'),
};

export const plannerApi = {
  createPlan: (prompt: string) => apiClient.post('/api/planner/plan', { prompt }),
  getPlan: (workflowId: string) => apiClient.get(`/api/planner/plan/${workflowId}`),
};

export const quotesApi = {
  collectQuotes: (plan: any) => apiClient.post('/api/quotes/collect', plan),
  selectQuotes: (request: any) => apiClient.post('/api/quotes/select', request),
  getQuotes: (workflowId: string) => apiClient.get(`/api/quotes/${workflowId}`),
};

export const executionApi = {
  startExecution: (request: any, paymentProofHeader?: string) => {
    const headers: Record<string, string> = {};
    if (paymentProofHeader) {
      headers['X-402-Payment-Proof'] = paymentProofHeader;
    }
    return apiClient.post('/api/execution/start', request, { headers });
  },
  getStatus: (workflowId: string) => apiClient.get(`/api/execution/${workflowId}`),
  getLogs: (workflowId: string) => apiClient.get(`/api/execution/${workflowId}/logs`),
  getEvents: (workflowId: string) => apiClient.get(`/api/execution/${workflowId}/events`),
  getMetrics: () => apiClient.get('/api/execution/metrics'),
};

export const paymentsApi = {
  getHistory: () => apiClient.get('/api/payments/history'),
  getMetrics: () => apiClient.get('/api/payments/metrics'),
  getReceipt: (workflowId: string) => apiClient.get(`/api/payments/receipt/${workflowId}`),
  getTransaction: (txId: string) => apiClient.get(`/api/payments/transaction/${txId}`),
};

export const registryApi = {
  getAgents: () => apiClient.get('/api/registry/agents'),
  getDiscovery: () => apiClient.get('/api/discovery'),
};

export const demoApi = {
  runPipeline: (request: { prompt: string; strategy?: string; maxConcurrency?: number; paymentProof?: any }) =>
    apiClient.post('/api/demo/run', request),
};
