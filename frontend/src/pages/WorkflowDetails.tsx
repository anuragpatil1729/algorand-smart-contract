import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import ReactFlow, { Background, Controls, Node, Edge, Position } from 'reactflow';
import 'reactflow/dist/style.css';
import { api, PaymentDetailsDto } from '../services/api';
import { TaskNode } from '../components/TaskNode';
import { AlgorandModal } from '../components/AlgorandModal';
import { Play, ShieldCheck, Terminal, FileText, CheckCircle2, RefreshCw } from 'lucide-react';

const nodeTypes = { taskNode: TaskNode };

export const WorkflowDetails: React.FC = () => {
  const [searchParams] = useSearchParams();
  const workflowId = searchParams.get('id') || '';

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [paymentDetails, setPaymentDetails] = useState<PaymentDetailsDto | null>(null);

  const { data: workflow, refetch } = useQuery({
    queryKey: ['workflow', workflowId],
    queryFn: () => api.getWorkflow(workflowId),
    enabled: !!workflowId,
    refetchInterval: 2000,
  });

  const { data: logs } = useQuery({
    queryKey: ['logs', workflowId],
    queryFn: () => api.getLogs(workflowId),
    enabled: !!workflowId,
    refetchInterval: 1500,
  });

  const handleOpenAlgorandModal = async () => {
    if (!workflowId) return;
    try {
      const details = await api.getPaymentDetails(workflowId);
      setPaymentDetails(details);
      setIsModalOpen(true);
    } catch (e) {
      console.error(e);
    }
  };

  // Convert workflow tasks into React Flow Nodes & Edges
  const nodes: Node[] = (workflow?.tasks || []).map((t, idx) => ({
    id: t.id,
    type: 'taskNode',
    position: { x: (idx % 2 === 0 ? 100 : 420), y: Math.floor(idx / 2) * 180 + 40 },
    data: {
      title: t.description,
      type: t.taskType,
      assignedAgent: t.assignedAgent,
      status: t.status,
      price: t.price,
      executionTimeMs: t.executionTimeMs,
      complexity: t.estimatedComplexity,
    },
  }));

  const edges: Edge[] = [];
  (workflow?.tasks || []).forEach((t) => {
    if (t.dependencies && t.dependencies.length > 0) {
      t.dependencies.forEach((dep) => {
        edges.push({
          id: `e-${dep}-${t.id}`,
          source: dep,
          target: t.id,
          animated: t.status === 'RUNNING',
          style: { stroke: t.status === 'COMPLETED' ? '#10b981' : (t.status === 'RUNNING' ? '#06b6d4' : '#64748b') },
        });
      });
    }
  });

  return (
    <div className="p-8 space-y-8 max-w-7xl mx-auto">
      
      {/* Top Banner */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 p-6 rounded-3xl glass-panel border border-slate-800">
        <div className="space-y-1">
          <div className="flex items-center space-x-3">
            <span className="font-mono text-sm font-bold text-cyan-400">ID: {workflow?.id || workflowId || 'Select Workflow'}</span>
            <span className={`px-2.5 py-0.5 rounded-full text-xs font-bold ${
              workflow?.status === 'COMPLETED' ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/40' :
              workflow?.status === 'RUNNING' ? 'bg-blue-500/20 text-blue-400 border border-blue-500/40' :
              'bg-slate-800 text-slate-400'
            }`}>
              {workflow?.status || 'PENDING'}
            </span>
          </div>
          <h2 className="text-xl font-bold text-white">{workflow?.prompt || 'No active workflow selected'}</h2>
        </div>

        <div className="flex items-center space-x-3">
          <button
            onClick={() => refetch()}
            className="p-2.5 rounded-xl bg-slate-900 hover:bg-slate-800 text-slate-300 border border-slate-800 transition-colors"
          >
            <RefreshCw className="w-4 h-4" />
          </button>
          <button
            onClick={handleOpenAlgorandModal}
            className="px-4 py-2.5 rounded-xl bg-cyan-500/10 hover:bg-cyan-500/20 text-cyan-400 border border-cyan-500/30 font-bold text-xs flex items-center space-x-2 transition-colors"
          >
            <ShieldCheck className="w-4 h-4" />
            <span>View Algorand Atomic Receipt</span>
          </button>
        </div>
      </div>

      {/* DAG Workflow Visualizer */}
      <div className="space-y-3">
        <h3 className="text-xs font-bold uppercase tracking-wider text-slate-400 flex items-center gap-2">
          <Play className="w-4 h-4 text-cyan-400" />
          React Flow Task Graph (DAG Execution State)
        </h3>
        
        <div className="h-[420px] w-full rounded-3xl glass-panel border border-slate-800 overflow-hidden relative">
          <ReactFlow nodes={nodes} edges={edges} nodeTypes={nodeTypes} fitView>
            <Background color="#1e293b" gap={24} size={1} />
            <Controls className="!bg-slate-900 !border-slate-800 !text-slate-300" />
          </ReactFlow>
        </div>
      </div>

      {/* Execution Terminal Logs & Aggregated Deliverable */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        
        {/* Terminal Logs */}
        <div className="p-6 rounded-3xl glass-panel border border-slate-800 space-y-4">
          <h3 className="text-xs font-bold uppercase tracking-wider text-slate-300 flex items-center gap-2">
            <Terminal className="w-4 h-4 text-cyan-400" />
            Live Execution Terminal Stream
          </h3>
          <div className="h-80 overflow-y-auto rounded-2xl bg-slate-950 p-4 font-mono text-xs text-slate-300 space-y-2 border border-slate-900">
            {logs && logs.length > 0 ? (
              logs.map((log) => (
                <div key={log.id} className="leading-relaxed">
                  <span className="text-slate-600">[{log.timestamp.substring(11, 19)}]</span>{' '}
                  <span className={log.logLevel === 'ERROR' ? 'text-rose-400' : (log.logLevel === 'WARN' ? 'text-amber-400' : 'text-cyan-400')}>
                    [{log.logLevel}]
                  </span>{' '}
                  <span className="text-slate-300">{log.message}</span>
                </div>
              ))
            ) : (
              <div className="text-slate-600 text-center py-12">Listening for WebSocket event telemetry...</div>
            )}
          </div>
        </div>

        {/* Deliverable Package */}
        <div className="p-6 rounded-3xl glass-panel border border-slate-800 space-y-4">
          <h3 className="text-xs font-bold uppercase tracking-wider text-slate-300 flex items-center gap-2">
            <FileText className="w-4 h-4 text-emerald-400" />
            Merged Multi-Agent Deliverables
          </h3>
          <div className="h-80 overflow-y-auto rounded-2xl bg-slate-950 p-5 font-mono text-xs text-slate-300 space-y-4 border border-slate-900 whitespace-pre-wrap leading-relaxed">
            {workflow?.aggregatedResult ? (
              workflow.aggregatedResult
            ) : (
              <div className="text-slate-600 text-center py-12">Deliverables will aggregate upon 100% DAG task completion...</div>
            )}
          </div>
        </div>

      </div>

      {/* Algorand Modal */}
      <AlgorandModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} paymentDetails={paymentDetails} />

    </div>
  );
};
