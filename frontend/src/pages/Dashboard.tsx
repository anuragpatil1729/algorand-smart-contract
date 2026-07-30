import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { 
  Play, 
  CheckCircle2, 
  Cpu, 
  DollarSign, 
  Clock, 
  TrendingUp, 
  CreditCard, 
  Activity, 
  ShieldCheck, 
  ArrowUpRight,
  RefreshCw,
  Sparkles,
  Zap
} from 'lucide-react';
import { Link } from 'react-router-dom';

export const Dashboard: React.FC = () => {
  const [metrics, setMetrics] = useState({
    runningWorkflows: 2,
    completedWorkflows: 48,
    registeredAgents: 5,
    revenueUsdc: 245.50,
    avgExecutionTimeSeconds: 2.8,
    successRate: 98.2,
    totalPayments: 50,
    activeAgents: 5
  });

  const [recentWorkflows, setRecentWorkflows] = useState([
    { id: 'wf-plan-8f12a3', prompt: 'Create startup landing page with logo & QA', status: 'RUNNING', cost: 5.25, time: '1.2s' },
    { id: 'wf-plan-7c91b4', prompt: 'Python FastAPI research & benchmarking agent', status: 'COMPLETED', cost: 4.50, time: '2.4s' },
    { id: 'wf-plan-6a50e2', prompt: 'Pitch deck presentation & market architecture', status: 'COMPLETED', cost: 6.00, time: '3.1s' }
  ]);

  const [recentPayments, setRecentPayments] = useState([
    { txId: 'TX-ALGO-TEST-998811', amount: '5.25', asset: 'USDC', verified: true, time: '2 mins ago' },
    { txId: 'TX-ALGO-TEST-887722', amount: '4.50', asset: 'USDC', verified: true, time: '8 mins ago' },
    { txId: 'TX-ALGO-TEST-776633', amount: '6.00', asset: 'USDC', verified: true, time: '15 mins ago' }
  ]);

  const [agentsHealth, setAgentsHealth] = useState([
    { name: 'Research Agent', capability: 'RESEARCH', health: 100, load: '12%', status: 'HEALTHY' },
    { name: 'Coding Agent', capability: 'FRONTEND', health: 98, load: '24%', status: 'HEALTHY' },
    { name: 'Image Agent', capability: 'LOGO_DESIGN', health: 100, load: '8%', status: 'HEALTHY' },
    { name: 'PPT Agent', capability: 'PITCH_DECK', health: 95, load: '15%', status: 'HEALTHY' },
    { name: 'Testing Agent', capability: 'TESTING', health: 100, load: '5%', status: 'HEALTHY' }
  ]);

  return (
    <div className="space-y-8 pb-12">
      {/* Header Banner */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center space-x-2">
            <h1 className="text-2xl font-extrabold text-white tracking-tight">
              Mission Control Dashboard
            </h1>
            <span className="px-2.5 py-0.5 rounded-full bg-violet-500/10 text-violet-400 border border-violet-500/20 text-xs font-mono font-semibold">
              REAL-TIME ORCHESTRATION
            </span>
          </div>
          <p className="text-sm text-slate-400 mt-1 font-sans">
            Overview of AI agent workflows, live execution DAGs, and x402 Algorand settlements
          </p>
        </div>

        <div className="flex items-center space-x-3">
          <Link
            to="/planner"
            className="glass-button px-4 py-2.5 flex items-center space-x-2 text-sm font-semibold shadow-violet-600/30"
          >
            <Sparkles className="w-4 h-4 text-cyan-300" />
            <span>Launch Workflow Builder</span>
          </Link>
        </div>
      </div>

      {/* Top 8 Metric Cards Grid */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {[
          { label: 'Running Workflows', value: metrics.runningWorkflows, icon: Play, color: 'text-violet-400', bg: 'bg-violet-500/10', border: 'border-violet-500/20' },
          { label: 'Completed Workflows', value: metrics.completedWorkflows, icon: CheckCircle2, color: 'text-emerald-400', bg: 'bg-emerald-500/10', border: 'border-emerald-500/20' },
          { label: 'Registered Agents', value: metrics.registeredAgents, icon: Cpu, color: 'text-indigo-400', bg: 'bg-indigo-500/10', border: 'border-indigo-500/20' },
          { label: 'Total Revenue (USDC)', value: `$${metrics.revenueUsdc.toFixed(2)}`, icon: DollarSign, color: 'text-cyan-400', bg: 'bg-cyan-500/10', border: 'border-cyan-500/20' },
          { label: 'Avg Execution Time', value: `${metrics.avgExecutionTimeSeconds}s`, icon: Clock, color: 'text-amber-400', bg: 'bg-amber-500/10', border: 'border-amber-500/20' },
          { label: 'Success Rate', value: `${metrics.successRate}%`, icon: TrendingUp, color: 'text-emerald-400', bg: 'bg-emerald-500/10', border: 'border-emerald-500/20' },
          { label: 'Total Payments', value: metrics.totalPayments, icon: CreditCard, color: 'text-purple-400', bg: 'bg-purple-500/10', border: 'border-purple-500/20' },
          { label: 'Active Agents', value: metrics.activeAgents, icon: Activity, color: 'text-emerald-400', bg: 'bg-emerald-500/10', border: 'border-emerald-500/20' }
        ].map((m, idx) => (
          <motion.div
            key={idx}
            initial={{ opacity: 0, y: 15 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3, delay: idx * 0.05 }}
            className={`glass-panel p-4 border ${m.border} relative overflow-hidden group hover:border-slate-700 transition-all`}
          >
            <div className="flex items-center justify-between">
              <span className="text-xs font-mono font-medium text-slate-400 uppercase tracking-wider">
                {m.label}
              </span>
              <div className={`p-2 rounded-xl ${m.bg}`}>
                <m.icon className={`w-4 h-4 ${m.color}`} />
              </div>
            </div>
            <div className="mt-3">
              <span className="text-2xl font-bold text-white tracking-tight font-mono">
                {m.value}
              </span>
            </div>
          </motion.div>
        ))}
      </div>

      {/* Main Grid: Workflows Activity & Payments */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left 2 Cols: Live Workflow Activity Stream */}
        <div className="lg:col-span-2 space-y-6">
          <div className="glass-panel p-5 border-slate-800/80">
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center space-x-2">
                <Activity className="w-5 h-5 text-violet-400" />
                <h2 className="text-base font-bold text-white">Live Workflow Activity Stream</h2>
              </div>
              <Link to="/planner" className="text-xs text-violet-400 hover:text-violet-300 font-mono flex items-center space-x-1">
                <span>Builder</span>
                <ArrowUpRight className="w-3.5 h-3.5" />
              </Link>
            </div>

            <div className="space-y-3">
              {recentWorkflows.map((wf) => (
                <div key={wf.id} className="glass-card p-4 border-slate-800/80 flex items-center justify-between hover:bg-slate-800/40 transition-colors">
                  <div className="flex items-center space-x-3">
                    <div className={`w-2.5 h-2.5 rounded-full ${wf.status === 'RUNNING' ? 'bg-violet-400 animate-ping' : 'bg-emerald-400'}`} />
                    <div>
                      <h4 className="text-xs font-bold text-slate-100 font-sans">{wf.prompt}</h4>
                      <p className="text-[11px] text-slate-400 font-mono mt-0.5">ID: {wf.id} • Duration: {wf.time}</p>
                    </div>
                  </div>

                  <div className="flex items-center space-x-3">
                    <span className="text-xs font-mono font-semibold text-emerald-400">${wf.cost.toFixed(2)} USDC</span>
                    <span className={`text-[10px] font-mono px-2 py-0.5 rounded-full border ${
                      wf.status === 'RUNNING' ? 'bg-violet-500/20 text-violet-300 border-violet-500/30' : 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30'
                    }`}>
                      {wf.status}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Recent x402 Payments Table */}
          <div className="glass-panel p-5 border-slate-800/80">
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center space-x-2">
                <ShieldCheck className="w-5 h-5 text-emerald-400" />
                <h2 className="text-base font-bold text-white">Recent x402 Algorand Settlements</h2>
              </div>
              <Link to="/payments" className="text-xs text-emerald-400 hover:text-emerald-300 font-mono flex items-center space-x-1">
                <span>View All</span>
                <ArrowUpRight className="w-3.5 h-3.5" />
              </Link>
            </div>

            <div className="space-y-2 font-mono text-xs">
              {recentPayments.map((p, idx) => (
                <div key={idx} className="glass-card p-3 border-slate-800/60 flex items-center justify-between">
                  <div className="flex items-center space-x-3">
                    <CreditCard className="w-4 h-4 text-violet-400" />
                    <div>
                      <span className="text-slate-200 font-semibold">{p.txId}</span>
                      <span className="text-slate-500 text-[10px] block">{p.time}</span>
                    </div>
                  </div>
                  <div className="flex items-center space-x-3">
                    <span className="text-emerald-400 font-bold">${p.amount} {p.asset}</span>
                    <span className="text-[10px] px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                      VERIFIED
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Right 1 Col: Agent Health Monitor */}
        <div className="space-y-6">
          <div className="glass-panel p-5 border-slate-800/80">
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center space-x-2">
                <Cpu className="w-5 h-5 text-indigo-400" />
                <h2 className="text-base font-bold text-white">Agent Health Monitor</h2>
              </div>
              <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                5 / 5 UP
              </span>
            </div>

            <div className="space-y-3">
              {agentsHealth.map((a, idx) => (
                <div key={idx} className="glass-card p-3.5 border-slate-800/60 flex items-center justify-between">
                  <div>
                    <h4 className="text-xs font-bold text-slate-100">{a.name}</h4>
                    <span className="text-[10px] font-mono text-indigo-400 bg-indigo-500/10 px-1.5 py-0.5 rounded">
                      {a.capability}
                    </span>
                  </div>
                  <div className="text-right font-mono">
                    <span className="text-xs text-emerald-400 font-bold">{a.health}%</span>
                    <span className="text-[10px] text-slate-500 block">Load: {a.load}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
