import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { api } from '../services/api';
import { BarChart3, TrendingUp, Cpu, DollarSign, Clock, ShieldCheck } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';

export const AnalyticsPage: React.FC = () => {
  const { data: analytics } = useQuery({ queryKey: ['analytics'], queryFn: api.getAnalytics, refetchInterval: 5000 });

  const usageData = Object.entries(analytics?.agentUsageMap || {}).map(([key, val]) => ({
    name: key.replace('agent-', '').replace('-01', '').replace('-02', '').replace('-03', '').replace('-04', '').replace('-05', '').toUpperCase(),
    tasks: val,
  }));

  const COLORS = ['#06b6d4', '#6366f1', '#a855f7', '#ec4899', '#10b981'];

  return (
    <div className="p-8 space-y-8 max-w-7xl mx-auto">
      
      {/* Header */}
      <div className="space-y-2">
        <h1 className="text-3xl font-black text-white flex items-center gap-3">
          <BarChart3 className="w-8 h-8 text-cyan-400" />
          System Telemetry & Analytics
        </h1>
        <p className="text-sm text-slate-400">
          In-depth insights into agent task allocation, cost breakdown, latency performance, and Algorand network fees.
        </p>
      </div>

      {/* Summary Row */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="p-5 rounded-2xl glass-panel border border-slate-800 space-y-1">
          <span className="text-slate-400 text-xs font-semibold">Total Revenue (Router Fees)</span>
          <div className="font-mono text-2xl font-black text-emerald-400">{analytics?.totalRevenueAlgos ?? 14.8} ALGO</div>
        </div>
        <div className="p-5 rounded-2xl glass-panel border border-slate-800 space-y-1">
          <span className="text-slate-400 text-xs font-semibold">Overall Success Rate</span>
          <div className="font-mono text-2xl font-black text-cyan-400">{analytics?.overallSuccessRate ?? 98.2}%</div>
        </div>
        <div className="p-5 rounded-2xl glass-panel border border-slate-800 space-y-1">
          <span className="text-slate-400 text-xs font-semibold">Average Execution Latency</span>
          <div className="font-mono text-2xl font-black text-indigo-400">{analytics?.avgExecutionTimeSeconds ?? 14.2}s</div>
        </div>
        <div className="p-5 rounded-2xl glass-panel border border-slate-800 space-y-1">
          <span className="text-slate-400 text-xs font-semibold">Active Agents</span>
          <div className="font-mono text-2xl font-black text-purple-400">{analytics?.totalAgents ?? 5} Microservices</div>
        </div>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        
        {/* Agent Task Allocation */}
        <div className="p-6 rounded-3xl glass-panel border border-slate-800 space-y-4">
          <h3 className="text-sm font-bold text-white flex items-center gap-2">
            <Cpu className="w-4 h-4 text-cyan-400" />
            Agent Task Allocation Breakdown
          </h3>
          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={usageData.length > 0 ? usageData : [
                { name: 'RESEARCH', tasks: 14 },
                { name: 'CODE', tasks: 22 },
                { name: 'IMAGE', tasks: 18 },
                { name: 'PPT', tasks: 12 },
                { name: 'TESTING', tasks: 16 }
              ]}>
                <XAxis dataKey="name" stroke="#64748b" fontSize={11} tickLine={false} />
                <YAxis stroke="#64748b" fontSize={11} tickLine={false} />
                <Tooltip contentStyle={{ background: '#0f172a', border: '1px solid #1e293b', borderRadius: '12px', fontSize: '12px' }} />
                <Bar dataKey="tasks" fill="#06b6d4" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Workflow Cost Distribution */}
        <div className="p-6 rounded-3xl glass-panel border border-slate-800 space-y-4">
          <h3 className="text-sm font-bold text-white flex items-center gap-2">
            <DollarSign className="w-4 h-4 text-emerald-400" />
            Cost Distribution Across Capabilities
          </h3>
          <div className="h-64 w-full flex items-center justify-center">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={[
                    { name: 'Research', value: 25 },
                    { name: 'Frontend/Backend Code', value: 40 },
                    { name: 'Logo Graphics', value: 20 },
                    { name: 'Pitch Strategy', value: 15 }
                  ]}
                  cx="50%"
                  cy="50%"
                  innerRadius={60}
                  outerRadius={90}
                  paddingAngle={5}
                  dataKey="value"
                >
                  {COLORS.map((color, index) => (
                    <Cell key={`cell-${index}`} fill={color} />
                  ))}
                </Pie>
                <Tooltip contentStyle={{ background: '#0f172a', border: '1px solid #1e293b', borderRadius: '12px', fontSize: '12px' }} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

      </div>

    </div>
  );
};
