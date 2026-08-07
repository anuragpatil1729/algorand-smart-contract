import React from 'react';
import { motion } from 'framer-motion';
import { 
  BarChart3, 
  TrendingUp, 
  DollarSign, 
  Clock, 
  Cpu, 
  CheckCircle2 
} from 'lucide-react';
import { 
  ResponsiveContainer, 
  AreaChart, 
  Area, 
  BarChart, 
  Bar, 
  XAxis, 
  YAxis, 
  Tooltip, 
  CartesianGrid 
} from 'recharts';

export const AnalyticsPage: React.FC = () => {
  const workflowTrendData = [
    { day: 'Mon', workflows: 8, revenue: 38.0 },
    { day: 'Tue', workflows: 12, revenue: 58.5 },
    { day: 'Wed', workflows: 15, revenue: 72.0 },
    { day: 'Thu', workflows: 11, revenue: 52.5 },
    { day: 'Fri', workflows: 18, revenue: 89.0 },
    { day: 'Sat', workflows: 22, revenue: 110.0 },
    { day: 'Sun', workflows: 20, revenue: 98.5 }
  ];

  const agentUsageData = [
    { name: 'Research', tasks: 48, cost: 2160 },
    { name: 'Coding', tasks: 42, cost: 3360 },
    { name: 'Image', tasks: 30, cost: 1500 },
    { name: 'PPT', tasks: 25, cost: 1500 },
    { name: 'Testing', tasks: 50, cost: 1500 }
  ];

  const agentGrowthData = [
    { month: 'Jan', agents: 2, revenue: 120 },
    { month: 'Feb', agents: 3, revenue: 240 },
    { month: 'Mar', agents: 4, revenue: 410 },
    { month: 'Apr', agents: 5, revenue: 590 },
    { month: 'May', agents: 7, revenue: 820 },
    { month: 'Jun', agents: 9, revenue: 1150 }
  ];

  return (
    <div className="space-y-8 pb-12">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center space-x-2">
            <h1 className="text-2xl font-extrabold text-white tracking-tight">
              Platform Telemetry & Analytics
            </h1>
            <span className="px-2.5 py-0.5 rounded-full bg-violet-500/10 text-violet-400 border border-violet-500/20 text-xs font-mono font-semibold">
              RECHARTS TELEMETRY
            </span>
          </div>
          <p className="text-sm text-slate-400 mt-1 font-sans">
            Real-time analytics for revenue, agent growth, workflow success rates, average costs, and execution speed
          </p>
        </div>
      </div>

      {/* Top Analytics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        {[
          { label: 'Weekly Revenue', value: '$518.50 USDC', icon: DollarSign, color: 'text-emerald-400' },
          { label: 'Total Tasks Executed', value: '195 Tasks', icon: Cpu, color: 'text-indigo-400' },
          { label: 'Avg Workflow Time', value: '2.85s', icon: Clock, color: 'text-amber-400' },
          { label: 'Platform Success Rate', value: '98.8%', icon: CheckCircle2, color: 'text-emerald-400' }
        ].map((c, idx) => (
          <div key={idx} className="glass-panel p-4 border-slate-800/80">
            <div className="flex items-center justify-between">
              <span className="text-xs font-mono text-slate-400 uppercase">{c.label}</span>
              <c.icon className={`w-4 h-4 ${c.color}`} />
            </div>
            <div className="mt-2 text-xl font-bold text-white font-mono">{c.value}</div>
          </div>
        ))}
      </div>

      {/* Charts Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Revenue & Workflow Trend Area Chart */}
        <div className="glass-panel p-5 border-slate-800/80 space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="text-sm font-bold text-white flex items-center space-x-2">
              <TrendingUp className="w-4 h-4 text-emerald-400" />
              <span>Revenue Trend (USDC per Day)</span>
            </h3>
            <span className="text-xs font-mono text-slate-400">7-Day History</span>
          </div>

          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={workflowTrendData}>
                <defs>
                  <linearGradient id="revenueGrad" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#10b981" stopOpacity={0.4}/>
                    <stop offset="95%" stopColor="#10b981" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                <XAxis dataKey="day" stroke="#64748b" fontSize={11} />
                <YAxis stroke="#64748b" fontSize={11} />
                <Tooltip contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '12px', fontSize: '12px' }} />
                <Area type="monotone" dataKey="revenue" stroke="#10b981" fillOpacity={1} fill="url(#revenueGrad)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Agent Task Distribution Bar Chart */}
        <div className="glass-panel p-5 border-slate-800/80 space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="text-sm font-bold text-white flex items-center space-x-2">
              <Cpu className="w-4 h-4 text-violet-400" />
              <span>Agent Task Execution Volume</span>
            </h3>
            <span className="text-xs font-mono text-slate-400">By Capability</span>
          </div>

          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={agentUsageData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                <XAxis dataKey="name" stroke="#64748b" fontSize={11} />
                <YAxis stroke="#64748b" fontSize={11} />
                <Tooltip contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '12px', fontSize: '12px' }} />
                <Bar dataKey="tasks" fill="#8b5cf6" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Agent Network Growth Chart */}
        <div className="glass-panel p-5 border-slate-800/80 space-y-4 lg:col-span-2">
          <div className="flex items-center justify-between">
            <h3 className="text-sm font-bold text-white flex items-center space-x-2">
              <BarChart3 className="w-4 h-4 text-indigo-400" />
              <span>Decentralized Agent Network Growth & Revenue Scale</span>
            </h3>
            <span className="text-xs font-mono text-slate-400">Monthly Growth</span>
          </div>

          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={agentGrowthData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                <XAxis dataKey="month" stroke="#64748b" fontSize={11} />
                <YAxis stroke="#64748b" fontSize={11} />
                <Tooltip contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '12px', fontSize: '12px' }} />
                <Bar dataKey="agents" fill="#6366f1" radius={[6, 6, 0, 0]} name="Active Agents" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
};
