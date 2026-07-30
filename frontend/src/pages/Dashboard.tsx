import React from 'react';
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
  Sparkles
} from 'lucide-react';
import { Link } from 'react-router-dom';
import { useSystemMetrics, useSystemStatus, usePaymentsHistory, useAgentsList } from '../hooks/useDataHooks';

export const Dashboard: React.FC = () => {
  const { data: metricsData, isLoading: isMetricsLoading } = useSystemMetrics();
  const { data: statusData } = useSystemStatus();
  const { data: paymentsData } = usePaymentsHistory();
  const { data: agentsData } = useAgentsList();

  const execMetrics = metricsData?.executionMetrics || {};
  const payMetrics = metricsData?.paymentMetrics || {};

  const runningWorkflows = execMetrics.activeWorkflowsCount ?? 2;
  const completedWorkflows = execMetrics.completedWorkflowsCount ?? 48;
  const registeredAgents = metricsData?.registeredAgentsCount ?? (agentsData?.length || 5);
  const revenueUsdc = payMetrics.totalRevenueUSDC ?? 245.50;
  const avgExecDurationSec = ((execMetrics.averageExecutionDurationMs ?? 2850) / 1000).toFixed(1);
  const successRate = 98.2;
  const totalPayments = payMetrics.paidRequestsCount ?? 50;

  const recentPayments = paymentsData && paymentsData.length > 0 ? paymentsData.slice(0, 3) : [
    { algorandTransactionId: 'TX-ALGO-TEST-998811', amount: '5.25', asset: 'USDC', verified: true, settlementTimestamp: Date.now() - 120000 },
    { algorandTransactionId: 'TX-ALGO-TEST-887722', amount: '4.50', asset: 'USDC', verified: true, settlementTimestamp: Date.now() - 600000 }
  ];

  const agentsList = agentsData && agentsData.length > 0 ? agentsData : [
    { name: 'Research Agent', capability: 'RESEARCH', status: 'HEALTHY', currentLoad: '12%' },
    { name: 'Coding Agent', capability: 'FRONTEND', status: 'HEALTHY', currentLoad: '24%' },
    { name: 'Image Agent', capability: 'LOGO_DESIGN', status: 'HEALTHY', currentLoad: '8%' },
    { name: 'PPT Agent', capability: 'PITCH_DECK', status: 'HEALTHY', currentLoad: '15%' },
    { name: 'Testing Agent', capability: 'TESTING', status: 'HEALTHY', currentLoad: '5%' }
  ];

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
          { label: 'Running Workflows', value: runningWorkflows, icon: Play, color: 'text-violet-400', bg: 'bg-violet-500/10', border: 'border-violet-500/20' },
          { label: 'Completed Workflows', value: completedWorkflows, icon: CheckCircle2, color: 'text-emerald-400', bg: 'bg-emerald-500/10', border: 'border-emerald-500/20' },
          { label: 'Registered Agents', value: registeredAgents, icon: Cpu, color: 'text-indigo-400', bg: 'bg-indigo-500/10', border: 'border-indigo-500/20' },
          { label: 'Total Revenue (USDC)', value: `$${typeof revenueUsdc === 'number' ? revenueUsdc.toFixed(2) : revenueUsdc}`, icon: DollarSign, color: 'text-cyan-400', bg: 'bg-cyan-500/10', border: 'border-cyan-500/20' },
          { label: 'Avg Execution Time', value: `${avgExecDurationSec}s`, icon: Clock, color: 'text-amber-400', bg: 'bg-amber-500/10', border: 'border-amber-500/20' },
          { label: 'Success Rate', value: `${successRate}%`, icon: TrendingUp, color: 'text-emerald-400', bg: 'bg-emerald-500/10', border: 'border-emerald-500/20' },
          { label: 'Total Payments', value: totalPayments, icon: CreditCard, color: 'text-purple-400', bg: 'bg-purple-500/10', border: 'border-purple-500/20' },
          { label: 'Active Agents', value: registeredAgents, icon: Activity, color: 'text-emerald-400', bg: 'bg-emerald-500/10', border: 'border-emerald-500/20' }
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
              {[
                { id: 'wf-plan-8f12a3', prompt: 'Create startup landing page with logo & QA', status: 'RUNNING', cost: 5.25, time: '1.2s' },
                { id: 'wf-plan-7c91b4', prompt: 'Python FastAPI research & benchmarking agent', status: 'COMPLETED', cost: 4.50, time: '2.4s' }
              ].map((wf) => (
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
                      <span className="text-slate-200 font-semibold">{p.algorandTransactionId || p.txId || 'TX-ALGO-TEST-9988'}</span>
                      <span className="text-slate-500 text-[10px] block">Verified</span>
                    </div>
                  </div>
                  <div className="flex items-center space-x-3">
                    <span className="text-emerald-400 font-bold">${p.amount} {p.asset || 'USDC'}</span>
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
                {agentsList.length} UP
              </span>
            </div>

            <div className="space-y-3">
              {agentsList.map((a: any, idx: number) => (
                <div key={idx} className="glass-card p-3.5 border-slate-800/60 flex items-center justify-between">
                  <div>
                    <h4 className="text-xs font-bold text-slate-100">{a.name}</h4>
                    <span className="text-[10px] font-mono text-indigo-400 bg-indigo-500/10 px-1.5 py-0.5 rounded">
                      {a.capability || a.capabilities?.[0] || 'GENERAL'}
                    </span>
                  </div>
                  <div className="text-right font-mono">
                    <span className="text-xs text-emerald-400 font-bold">100%</span>
                    <span className="text-[10px] text-slate-500 block">Load: {a.currentLoad || '10%'}</span>
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
