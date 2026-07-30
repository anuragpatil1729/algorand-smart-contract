import React, { memo } from 'react';
import { Handle, Position, NodeProps } from 'reactflow';
import { Cpu, CheckCircle2, AlertCircle, RefreshCw, Clock } from 'lucide-react';
import { motion } from 'framer-motion';

export interface TaskNodeData {
  label: string;
  capability: string;
  agentName?: string;
  status: 'PENDING' | 'READY' | 'RUNNING' | 'COMPLETED' | 'FAILED' | 'RETRYING';
  price?: number;
  duration?: number;
  progress?: number;
}

export const TaskNode: React.FC<NodeProps<TaskNodeData>> = memo(({ data }) => {
  const getStatusStyles = (status: TaskNodeData['status']) => {
    switch (status) {
      case 'RUNNING':
        return {
          border: 'border-violet-500/80 shadow-lg shadow-violet-500/20 bg-slate-900/90',
          badge: 'bg-violet-500/20 text-violet-300 border-violet-500/40',
          icon: <RefreshCw className="w-3.5 h-3.5 text-violet-400 animate-spin" />,
          dot: 'bg-violet-400 animate-ping'
        };
      case 'COMPLETED':
        return {
          border: 'border-emerald-500/60 shadow-lg shadow-emerald-500/10 bg-slate-900/90',
          badge: 'bg-emerald-500/20 text-emerald-300 border-emerald-500/40',
          icon: <CheckCircle2 className="w-3.5 h-3.5 text-emerald-400" />,
          dot: 'bg-emerald-400'
        };
      case 'FAILED':
        return {
          border: 'border-rose-500/60 shadow-lg shadow-rose-500/10 bg-slate-900/90',
          badge: 'bg-rose-500/20 text-rose-300 border-rose-500/40',
          icon: <AlertCircle className="w-3.5 h-3.5 text-rose-400" />,
          dot: 'bg-rose-400'
        };
      case 'RETRYING':
        return {
          border: 'border-amber-500/60 shadow-lg shadow-amber-500/10 bg-slate-900/90',
          badge: 'bg-amber-500/20 text-amber-300 border-amber-500/40',
          icon: <RefreshCw className="w-3.5 h-3.5 text-amber-400 animate-spin" />,
          dot: 'bg-amber-400'
        };
      default:
        return {
          border: 'border-slate-800 bg-slate-950/80 hover:border-slate-700',
          badge: 'bg-slate-800/80 text-slate-400 border-slate-700/60',
          icon: <Clock className="w-3.5 h-3.5 text-slate-500" />,
          dot: 'bg-slate-500'
        };
    }
  };

  const style = getStatusStyles(data.status);

  return (
    <motion.div
      initial={{ scale: 0.9, opacity: 0 }}
      animate={{ scale: 1, opacity: 1 }}
      transition={{ duration: 0.2 }}
      className={`w-64 p-3.5 rounded-2xl border backdrop-blur-xl transition-all duration-300 ${style.border}`}
    >
      <Handle type="target" position={Position.Top} className="!bg-slate-500 !w-3 !h-3 !border-2 !border-slate-900" />

      {/* Header: Title & Status Badge */}
      <div className="flex items-start justify-between gap-2 mb-2">
        <div className="flex items-center space-x-2">
          <span className={`w-2 h-2 rounded-full ${style.dot}`} />
          <h4 className="text-xs font-semibold text-slate-100 truncate max-w-[140px]" title={data.label}>
            {data.label}
          </h4>
        </div>
        <div className={`flex items-center space-x-1 text-[10px] font-mono px-2 py-0.5 rounded-full border ${style.badge}`}>
          {style.icon}
          <span className="font-medium">{data.status}</span>
        </div>
      </div>

      {/* Capability Pill */}
      <div className="mb-2">
        <span className="inline-flex items-center space-x-1 text-[10px] font-mono font-medium text-indigo-300 bg-indigo-500/10 px-2 py-0.5 rounded-md border border-indigo-500/20">
          <Cpu className="w-3 h-3 text-indigo-400" />
          <span>{data.capability}</span>
        </span>
      </div>

      {/* Assigned Agent & Price */}
      <div className="flex items-center justify-between text-[11px] pt-2 border-t border-slate-800/60 font-mono text-slate-400">
        <span className="truncate max-w-[130px]" title={data.agentName || 'Auto-assigning...'}>
          {data.agentName || 'Auto-assigning...'}
        </span>
        {data.price !== undefined && (
          <span className="font-semibold text-emerald-400">
            ${data.price.toFixed(2)} USDC
          </span>
        )}
      </div>

      <Handle type="source" position={Position.Bottom} className="!bg-violet-500 !w-3 !h-3 !border-2 !border-slate-900" />
    </motion.div>
  );
});
