import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { api } from '../services/api';
import { Users, Play, CheckCircle2, DollarSign, TrendingUp, Clock, Zap, ArrowRight, Shield } from 'lucide-react';
import { AreaChart, Area, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import { useNavigate } from 'react-router-dom';

export const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const { data: analytics } = useQuery({ queryKey: ['analytics'], queryFn: api.getAnalytics, refetchInterval: 5000 });
  const { data: workflows } = useQuery({ queryKey: ['workflows'], queryFn: api.listWorkflows, refetchInterval: 5000 });

  const stats = [
    { label: 'Registered Agents', value: analytics?.totalAgents ?? 5, icon: Users, color: 'text-cyan-400', bg: 'bg-cyan-500/10' },
    { label: 'Active Workflows', value: analytics?.activeWorkflows ?? 0, icon: Play, color: 'text-blue-400', bg: 'bg-blue-500/10' },
    { label: 'Completed Workflows', value: analytics?.completedWorkflows ?? 0, icon: CheckCircle2, color: 'text-emerald-400', bg: 'bg-emerald-500/10' },
    { label: 'Total Transactions', value: analytics?.totalTransactions ?? 0, icon: DollarSign, color: 'text-purple-400', bg: 'bg-purple-500/10' },
    { label: 'Success Rate', value: `${analytics?.overallSuccessRate ?? 98.2}%`, icon: TrendingUp, color: 'text-indigo-400', bg: 'bg-indigo-500/10' },
    { label: 'Avg Execution Latency', value: `${analytics?.avgExecutionTimeSeconds ?? 14.2}s`, icon: Clock, color: 'text-amber-400', bg: 'bg-amber-500/10' },
  ];

  return (
    <div className="p-8 space-y-8 max-w-7xl mx-auto">
      
      {/* Hero Section */}
      <div className="relative overflow-hidden rounded-3xl bg-gradient-to-r from-cyan-950/40 via-slate-900 to-indigo-950/40 p-8 border border-cyan-500/30 shadow-2xl">
        <div className="absolute right-0 top-0 -mr-16 -mt-16 h-64 w-64 rounded-full bg-cyan-500/10 blur-3xl"></div>
        <div className="relative z-10 space-y-4 max-w-2xl">
          <div className="inline-flex items-center space-x-2 rounded-full bg-cyan-500/10 px-3 py-1 text-xs font-bold text-cyan-400 border border-cyan-500/30">
            <Zap className="h-3.5 w-3.5" />
            <span>AI Multi-Agent Service Router</span>
          </div>
          <h1 className="text-3xl font-black tracking-tight text-white sm:text-4xl">
            Orchestrate Autonomous AI Agents with <span className="bg-gradient-to-r from-cyan-400 to-indigo-400 bg-clip-text text-transparent">Algorand Atomic Payments</span>
          </h1>
          <p className="text-sm text-slate-400 leading-relaxed">
            Decompose complex prompts into structured task graphs, dynamically match specialized microservices using reputation scoring, and disburse instant trustless payments on Algorand.
          </p>
          <div className="pt-2 flex items-center space-x-4">
            <button
              onClick={() => navigate('/planner')}
              className="px-6 py-3 rounded-xl bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 text-white font-bold text-xs uppercase tracking-wider flex items-center space-x-2 shadow-lg shadow-cyan-500/25 transition-all transform active:scale-95"
            >
              <span>Submit New Prompt</span>
              <ArrowRight className="w-4 h-4" />
            </button>
            <button
              onClick={() => navigate('/marketplace')}
              className="px-6 py-3 rounded-xl bg-slate-900 hover:bg-slate-800 text-slate-300 border border-slate-700 font-bold text-xs uppercase tracking-wider transition-all"
            >
              Explore AI Agent Marketplace
            </button>
          </div>
        </div>
      </div>

      {/* Metrics Grid */}
      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
        {stats.map((stat, i) => {
          const Icon = stat.icon;
          return (
            <div key={i} className="p-5 rounded-2xl glass-card border border-slate-800/80 space-y-2">
              <div className="flex items-center justify-between">
                <span className="text-[11px] font-semibold text-slate-400">{stat.label}</span>
                <div className={`p-2 rounded-xl ${stat.bg} ${stat.color}`}>
                  <Icon className="w-4 h-4" />
                </div>
              </div>
              <div className="text-xl font-bold font-mono text-white">{stat.value}</div>
            </div>
          );
        })}
      </div>

      {/* Charts & Recent Activity */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        {/* Activity Chart */}
        <div className="lg:col-span-2 p-6 rounded-3xl glass-panel space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-sm font-bold text-slate-100">Workflow Execution & Revenue Volume</h3>
              <p className="text-xs text-slate-400">Weekly multi-agent DAG requests processed</p>
            </div>
            <span className="text-xs font-mono font-bold text-cyan-400 bg-cyan-500/10 px-2.5 py-1 rounded-lg border border-cyan-500/20">
              Live Algorand Feeds
            </span>
          </div>

          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={analytics?.recentActivity || []}>
                <defs>
                  <linearGradient id="colorWf" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#06b6d4" stopOpacity={0.4}/>
                    <stop offset="95%" stopColor="#06b6d4" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <XAxis dataKey="date" stroke="#64748b" fontSize={11} tickLine={false} />
                <YAxis stroke="#64748b" fontSize={11} tickLine={false} />
                <Tooltip contentStyle={{ background: '#0f172a', border: '1px solid #1e293b', borderRadius: '12px', fontSize: '12px' }} />
                <Area type="monotone" dataKey="workflows" stroke="#06b6d4" strokeWidth={3} fillOpacity={1} fill="url(#colorWf)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Recent Workflows Stream */}
        <div className="p-6 rounded-3xl glass-panel space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="text-sm font-bold text-slate-100">Recent Workflows</h3>
            <button onClick={() => navigate('/workflows')} className="text-xs text-cyan-400 hover:underline">View All</button>
          </div>

          <div className="space-y-3 max-h-72 overflow-y-auto pr-1">
            {workflows && workflows.length > 0 ? (
              workflows.slice(0, 4).map((w) => (
                <div 
                  key={w.id} 
                  onClick={() => navigate(`/workflows?id=${w.id}`)}
                  className="p-3.5 rounded-2xl bg-slate-900/80 hover:bg-slate-800/80 border border-slate-800/80 cursor-pointer transition-all space-y-2"
                >
                  <div className="flex items-center justify-between text-xs">
                    <span className="font-mono font-bold text-cyan-400">{w.id}</span>
                    <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${
                      w.status === 'COMPLETED' ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30' :
                      w.status === 'RUNNING' ? 'bg-blue-500/20 text-blue-400 border border-blue-500/30' :
                      'bg-slate-800 text-slate-400'
                    }`}>
                      {w.status}
                    </span>
                  </div>
                  <p className="text-xs text-slate-300 font-medium line-clamp-1">{w.prompt}</p>
                  <div className="flex items-center justify-between text-[11px] text-slate-500 border-t border-slate-800/60 pt-2">
                    <span>{w.tasks?.length || 0} Tasks</span>
                    <span className="font-mono text-emerald-400 font-semibold">{w.totalPrice} ALGO</span>
                  </div>
                </div>
              ))
            ) : (
              <div className="text-center py-8 text-xs text-slate-500">
                No workflows created yet. Click "Submit New Prompt" above to launch one!
              </div>
            )}
          </div>
        </div>

      </div>

    </div>
  );
};
