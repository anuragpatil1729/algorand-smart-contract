import React, { memo } from 'react';
import { Handle, Position, NodeProps } from 'reactflow';
import { Cpu, CheckCircle2, AlertCircle, RefreshCw, Clock, Search, DollarSign, Award, ShieldCheck, Layers } from 'lucide-react';
import { motion } from 'framer-motion';

export interface TaskNodeData {
  label: string;
  capability: string;
  agentName?: string;
  status: 'PLANNING' | 'DISCOVERED' | 'QUOTED' | 'ASSIGNED' | 'WAITING' | 'RUNNING' | 'COMPLETED' | 'VERIFIED' | 'FAILED';
  price?: number;
  duration?: number;
  progress?: number;
  confidenceScore?: number;
}

export const TaskNode: React.FC<NodeProps<TaskNodeData>> = memo(({ data }) => {
  const getStatusStyles = (status: TaskNodeData['status']) => {
    switch (status) {
      case 'PLANNING':
        return {
          border: 'border-cyan-500/80 shadow-lg shadow-cyan-500/20 bg-slate-900/90',
          badge: 'bg-cyan-500/20 text-cyan-300 border-cyan-500/40',
          icon: <Layers className="w-3.5 h-3.5 text-cyan-400 animate-pulse" />,
          dot: 'bg-cyan-400 animate-ping'
        };
      case 'DISCOVERED':
        return {
          border: 'border-indigo-500/80 shadow-lg shadow-indigo-500/20 bg-slate-900/90',
          badge: 'bg-indigo-500/20 text-indigo-300 border-indigo-500/40',
          icon: <Search className="w-3.5 h-3.5 text-indigo-400 animate-pulse" />,
          dot: 'bg-indigo-400 animate-ping'
        };
      case 'QUOTED':
        return {
          border: 'border-amber-500/80 shadow-lg shadow-amber-500/20 bg-slate-900/90',
          badge: 'bg-amber-500/20 text-amber-300 border-amber-500/40',
          icon: <DollarSign className="w-3.5 h-3.5 text-amber-400 animate-bounce" />,
          dot: 'bg-amber-400 animate-ping'
        };
      case 'ASSIGNED':
        return {
          border: 'border-violet-500/80 shadow-lg shadow-violet-500/20 bg-slate-900/90',
          badge: 'bg-violet-500/20 text-violet-300 border-violet-500/40',
          icon: <Award className="w-3.5 h-3.5 text-violet-400" />,
          dot: 'bg-violet-400'
        };
      case 'WAITING':
        return {
          border: 'border-slate-700 bg-slate-950/90',
          badge: 'bg-slate-800 text-slate-400 border-slate-700',
          icon: <Clock className="w-3.5 h-3.5 text-slate-500" />,
          dot: 'bg-slate-500'
        };
      case 'RUNNING':
        return {
          border: 'border-violet-500/90 shadow-xl shadow-violet-500/30 bg-slate-900/95 ring-2 ring-violet-500/50',
          badge: 'bg-violet-500/20 text-violet-300 border-violet-500/50',
          icon: <RefreshCw className="w-3.5 h-3.5 text-violet-400 animate-spin" />,
          dot: 'bg-violet-400 animate-ping'
        };
      case 'COMPLETED':
        return {
          border: 'border-emerald-500/70 shadow-lg shadow-emerald-500/15 bg-slate-900/90',
          badge: 'bg-emerald-500/20 text-emerald-300 border-emerald-500/40',
          icon: <CheckCircle2 className="w-3.5 h-3.5 text-emerald-400" />,
          dot: 'bg-emerald-400'
        };
      case 'VERIFIED':
        return {
          border: 'border-cyan-400/80 shadow-lg shadow-cyan-400/25 bg-slate-900/95 ring-1 ring-cyan-400/40',
          badge: 'bg-cyan-500/20 text-cyan-300 border-cyan-400/50',
          icon: <ShieldCheck className="w-3.5 h-3.5 text-cyan-400" />,
          dot: 'bg-cyan-400'
        };
      case 'FAILED':
        return {
          border: 'border-rose-500/70 shadow-lg shadow-rose-500/15 bg-slate-900/90',
          badge: 'bg-rose-500/20 text-rose-300 border-rose-500/40',
          icon: <AlertCircle className="w-3.5 h-3.5 text-rose-400" />,
          dot: 'bg-rose-400'
        };
    }
  };

  const style = getStatusStyles(data.status);

  return (
    <motion.div
      initial={{ scale: 0.8, opacity: 0 }}
      animate={{ scale: 1, opacity: 1 }}
      transition={{ duration: 0.3 }}
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
      <div className="mb-2 flex items-center justify-between">
        <span className="inline-flex items-center space-x-1 text-[10px] font-mono font-medium text-indigo-300 bg-indigo-500/10 px-2 py-0.5 rounded-md border border-indigo-500/20">
          <Cpu className="w-3 h-3 text-indigo-400" />
          <span>{data.capability}</span>
        </span>
        {data.confidenceScore && (
          <span className="text-[10px] font-mono text-emerald-400 font-semibold">
            {(data.confidenceScore * 100).toFixed(0)}% Conf
          </span>
        )}
      </div>

      {/* Progress Bar if Running */}
      {data.status === 'RUNNING' && (
        <div className="w-full bg-slate-950 rounded-full h-1.5 mb-2 overflow-hidden border border-slate-800">
          <div className="bg-gradient-to-r from-violet-500 to-cyan-400 h-full animate-pulse w-3/4" />
        </div>
      )}

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
