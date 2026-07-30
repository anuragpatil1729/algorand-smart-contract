import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useDemoMode } from '../contexts/DemoModeContext';
import { DataAdapterFactory } from '../services/adapter';

export const useSystemStatus = () => {
  const { demoMode } = useDemoMode();
  const adapter = DataAdapterFactory.getAdapter(demoMode);

  return useQuery({
    queryKey: ['systemStatus', demoMode],
    queryFn: () => adapter.getSystemStatus(),
    refetchInterval: 10000,
  });
};

export const useSystemMetrics = () => {
  const { demoMode } = useDemoMode();
  const adapter = DataAdapterFactory.getAdapter(demoMode);

  return useQuery({
    queryKey: ['systemMetrics', demoMode],
    queryFn: () => adapter.getSystemMetrics(),
    refetchInterval: 5000,
  });
};

export const useAgentsList = () => {
  const { demoMode } = useDemoMode();
  const adapter = DataAdapterFactory.getAdapter(demoMode);

  return useQuery({
    queryKey: ['agentsList', demoMode],
    queryFn: () => adapter.getAgents(),
    refetchInterval: 10000,
  });
};

export const usePaymentsHistory = () => {
  const { demoMode } = useDemoMode();
  const adapter = DataAdapterFactory.getAdapter(demoMode);

  return useQuery({
    queryKey: ['paymentsHistory', demoMode],
    queryFn: () => adapter.getPaymentsHistory(),
    refetchInterval: 5000,
  });
};

export const useWorkflowStatus = (workflowId?: string) => {
  const { demoMode } = useDemoMode();
  const adapter = DataAdapterFactory.getAdapter(demoMode);

  return useQuery({
    queryKey: ['workflowStatus', workflowId, demoMode],
    queryFn: () => (workflowId ? adapter.getWorkflowStatus(workflowId) : null),
    enabled: !!workflowId,
    refetchInterval: 2000,
  });
};

export const useWorkflowLogs = (workflowId?: string) => {
  const { demoMode } = useDemoMode();
  const adapter = DataAdapterFactory.getAdapter(demoMode);

  return useQuery({
    queryKey: ['workflowLogs', workflowId, demoMode],
    queryFn: () => (workflowId ? adapter.getWorkflowLogs(workflowId) : []),
    enabled: !!workflowId,
    refetchInterval: 2000,
  });
};

export const useWorkflowEvents = (workflowId?: string) => {
  const { demoMode } = useDemoMode();
  const adapter = DataAdapterFactory.getAdapter(demoMode);

  return useQuery({
    queryKey: ['workflowEvents', workflowId, demoMode],
    queryFn: () => (workflowId ? adapter.getWorkflowEvents(workflowId) : []),
    enabled: !!workflowId,
    refetchInterval: 2000,
  });
};

export const useRunPipelineMutation = () => {
  const { demoMode } = useDemoMode();
  const adapter = DataAdapterFactory.getAdapter(demoMode);
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: { prompt: string; strategy?: string; maxConcurrency?: number }) =>
      adapter.runPipeline(request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['systemMetrics'] });
      queryClient.invalidateQueries({ queryKey: ['paymentsHistory'] });
    },
  });
};
