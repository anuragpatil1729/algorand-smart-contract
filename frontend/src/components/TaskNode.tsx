import React from 'react';
import { Handle, Position } from 'reactflow';
import { CheckCircle2, Clock, AlertTriangle, Play, Cpu, ShieldCheck } from 'lucide-react';

export interface TaskNodeData {
  title: string;
  type: string;
  assignedAgent?: string;
  status: 'PENDING' | 'RUNNING' | 'COMPLETED' | 'FAILED' | 'CANCELLED';
  price: number;
  executionTimeMs?: number;
  complexity?: string;
}

export const TaskNode: React.FC<{ data: TaskNodeData }> = ({ data }) => {
  const getStatusStyles = () => {
    switch (data.status) {
      case 'RUNNING':
        return {
          bg: 'bg-blue-950/80 border-blue-500 shadow-blue-500/30',
          badge: 'bg-blue-500/20 text-blue-400 border-blue-500/40',
          icon: <Play className="w-3.5 h-3.5 animate-spin text-blue-400" />,
          label: 'RUNNING',
          pulse: 'animate-pulse border-blue-400'
        };
      case 'COMPLETED':
        return {
          bg: 'bg-emerald-950/80 border-emerald-500 shadow-emerald-500/20',
          badge: 'bg-emerald-500/20 text-emerald-400 border-emerald-500/40',
          icon: <CheckCircle2 className="w-3.5 h-3.5 text-emerald-400" />,
          label: 'COMPLETED',
          pulse: ''
        };
      case 'FAILED':
        return {
          bg: 'bg-rose-950/80 border-rose-500 shadow-rose-500/20',
          badge: 'bg-rose-500/20 text-rose-400 border-rose-500/40',
          icon: <AlertTriangle className="w-3.5 h-3.5 text-rose-400" />,
          label: 'FAILED',
          pulse: ''
        };
      default:
        return {
          bg: 'bg-slate-900/90 border-slate-700/80 shadow-slate-900/50',
          badge: 'bg-slate-800 text-slate-400 border-slate-700',
          icon: <Clock className="w-3.5 h-3.5 text-slate-400" />,
          label: 'PENDING',
          pulse: ''
        };
    }
  };

  const style = getStatusStyles();

  return (
    <div className={`w-72 rounded-2xl border ${style.bg} ${style.pulse} p-4 shadow-xl backdrop-blur-md transition-all duration-300`}>
      <Handle type="target" position={Position.Top} className="!bg-cyan-500" />

      {/* Header Badge */}
      <div className="flex items-center justify-between mb-2">
        <span className="text-[10px] font-bold tracking-wider text-slate-400 uppercase bg-slate-800/80 px-2 py-0.5 rounded-md border border-slate-700">
          {data.type}
        </span>
        <span className={`inline-flex items-center space-x-1 px-2 py-0.5 rounded-full text-[10px] font-bold border ${style.badge}`}>
          {style.icon}
          <span>{style.label}</span>
        </span>
      </div>

      {/* Title */}
      <h4 className="text-xs font-bold text-slate-100 mb-2 line-clamp-2 leading-snug">
        {data.title}
      </h4>

      {/* Agent & Specs */}
      <div className="space-y-1.5 border-t border-slate-800/80 pt-2 text-[11px]">
        <div className="flex items-center justify-between text-slate-400">
          <span className="flex items-center gap-1"><Cpu className="w-3 h-3 text-cyan-400" /> Agent:</span>
          <span className="font-semibold text-cyan-300 truncate max-w-[140px]">{data.assignedAgent || 'Auto-Selecting...'}</span>
        </div>
        <div className="flex items-center justify-between text-slate-400">
          <span className="flex items-center gap-1"><ShieldCheck className="w-3 h-3 text-indigo-400" /> Escrow Fee:</span>
          <span className="font-mono font-bold text-emerald-400">{data.price ? `${data.price} ALGO` : 'Calculating...'}</span>
        </div>
        {data.executionTimeMs ? (
          <div className="flex items-center justify-between text-slate-400 text-[10px]">
            <span>Latency:</span>
            <span className="font-mono text-slate-300">{data.executionTimeMs} ms</span>
          </div>
        ) : null}
      </div>

      <Handle type="source" position={Position.Bottom} className="!bg-cyan-500" />
    </div>
  );
};
