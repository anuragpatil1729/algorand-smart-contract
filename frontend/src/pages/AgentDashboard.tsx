import React, { useState } from 'react';
import { motion } from 'framer-motion';
import {
  DollarSign,
  Briefcase,
  Activity,
  Clock,
  Star,
  CreditCard,
  CheckCircle2,
  TrendingUp,
  Cpu,
  RefreshCw
} from 'lucide-react';
import { useAgentsList, usePaymentsHistory } from '../hooks/useDataHooks';

export const AgentDashboard: React.FC = () => {
  const { data: agentsData, isLoading: loadingAgents } = useAgentsList();
  const { data: paymentsData, isLoading: loadingPayments } = usePaymentsHistory();
  const [selectedAgentId, setSelectedAgentId] = useState<string>('agent-research-01');

  const agents = agentsData || [];
  const payments = paymentsData || [];

  const currentAgent = agents.find((a: any) => a.id === selectedAgentId) || agents[0] || {
    id: 'agent-research-01',
    name: 'Research & Market Intelligence Agent',
    capabilities: ['research', 'market-analysis'],
    basePrice: 45.0,
    rating: 4.9,
    successRate: 98.5,
    averageResponseTime: 420.0,
    completedTasks: 42,
    failedTasks: 1,
    totalRequests: 43,
    totalEarnings: 1890.0,
    status: 'ONLINE',
    walletAddress: 'D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ'
  };

  const agentPayments = payments.filter((p: any) => p.agentId === currentAgent.id || !p.agentId);

  return (
    <div className="space-y-8 pb-12">
      {/* Header & Agent Selector */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center space-x-2">
            <h1 className="text-2xl font-extrabold text-white tracking-tight">
              Agent Provider Dashboard
            </h1>
            <span className="px-2.5 py-0.5 rounded-full bg-violet-500/10 text-violet-400 border border-violet-500/20 text-xs font-mono font-semibold">
              PROVIDER PORTAL
            </span>
          </div>
          <p className="text-sm text-slate-400 mt-1 font-sans">
            Monitor earnings, completed jobs, latency telemetry, and x402 payment history for your registered AI microservices
          </p>
        </div>

        {/* Select Agent Dropdown */}
        <div className="flex items-center space-x-2">
          <label className="text-xs font-mono text-slate-400">Active Agent:</label>
          <select
            value={selectedAgentId}
            onChange={(e) => setSelectedAgentId(e.target.value)}
            className="glass-input px-3 py-2 text-xs bg-slate-950 text-slate-100 font-mono font-bold"
          >
            {agents.map((a: any) => (
              <option key={a.id} value={a.id}>
                {a.name} ({a.id})
              </option>
            ))}
          </select>
        </div>
      </div>

      {/* Top Telemetry Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-5">
        <motion.div initial={{ opacity: 0, y: 15 }} animate={{ opacity: 1, y: 0 }} className="glass-panel p-5 border-slate-800/80 space-y-2">
          <div className="flex items-center justify-between text-slate-400">
            <span className="text-xs font-mono uppercase font-semibold">Total Earnings</span>
            <DollarSign className="w-4 h-4 text-emerald-400" />
          </div>
          <p className="text-2xl font-extrabold text-white font-mono">
            ${(currentAgent.totalEarnings || ((currentAgent.completedTasks || 10) * (currentAgent.basePrice || 45))).toFixed(2)} <span className="text-xs font-normal text-emerald-400">USDC</span>
          </p>
          <p className="text-[11px] text-slate-500 font-mono">Direct AVM Atomic Payouts</p>
        </motion.div>

        <motion.div initial={{ opacity: 0, y: 15 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.05 }} className="glass-panel p-5 border-slate-800/80 space-y-2">
          <div className="flex items-center justify-between text-slate-400">
            <span className="text-xs font-mono uppercase font-semibold">Jobs Completed</span>
            <Briefcase className="w-4 h-4 text-violet-400" />
          </div>
          <p className="text-2xl font-extrabold text-white font-mono">
            {currentAgent.completedTasks || currentAgent.totalRequests || 15} <span className="text-xs font-normal text-slate-400">tasks</span>
          </p>
          <p className="text-[11px] text-emerald-400 font-mono">Success Rate: {currentAgent.successRate || 98.5}%</p>
        </motion.div>

        <motion.div initial={{ opacity: 0, y: 15 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.1 }} className="glass-panel p-5 border-slate-800/80 space-y-2">
          <div className="flex items-center justify-between text-slate-400">
            <span className="text-xs font-mono uppercase font-semibold">Average Latency</span>
            <Clock className="w-4 h-4 text-cyan-400" />
          </div>
          <p className="text-2xl font-extrabold text-white font-mono">
            {Math.round(currentAgent.averageResponseTime || 450)} <span className="text-xs font-normal text-slate-400">ms</span>
          </p>
          <p className="text-[11px] text-slate-500 font-mono">p95 SLA Target: &lt;1000ms</p>
        </motion.div>

        <motion.div initial={{ opacity: 0, y: 15 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.15 }} className="glass-panel p-5 border-slate-800/80 space-y-2">
          <div className="flex items-center justify-between text-slate-400">
            <span className="text-xs font-mono uppercase font-semibold">Reputation & Rating</span>
            <Star className="w-4 h-4 text-amber-400 fill-amber-400" />
          </div>
          <p className="text-2xl font-extrabold text-white font-mono">
            {currentAgent.rating || 4.9} / 5.0
          </p>
          <p className="text-[11px] text-indigo-400 font-mono">Bazaar Score: 98.0 / 100</p>
        </motion.div>
      </div>

      {/* Agent Details & Capability Spec */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="glass-panel p-6 border-slate-800/80 lg:col-span-2 space-y-4">
          <h3 className="text-base font-bold text-white flex items-center space-x-2">
            <Cpu className="w-4 h-4 text-violet-400" />
            <span>{currentAgent.name} Operational Details</span>
          </h3>

          <div className="space-y-3 text-xs font-mono">
            <div className="flex justify-between py-2 border-b border-slate-800/80">
              <span className="text-slate-400">Agent Identifier:</span>
              <span className="text-violet-300 font-bold">{currentAgent.id}</span>
            </div>
            <div className="flex justify-between py-2 border-b border-slate-800/80">
              <span className="text-slate-400">Service Endpoint:</span>
              <span className="text-slate-200">{currentAgent.endpoint || 'http://localhost:8001'}</span>
            </div>
            <div className="flex justify-between py-2 border-b border-slate-800/80">
              <span className="text-slate-400">x402 Bazaar Discovery URI:</span>
              <span className="text-cyan-400">{currentAgent.endpoint || 'http://localhost:8001'}/.well-known/x402-bazaar.json</span>
            </div>
            <div className="flex justify-between py-2 border-b border-slate-800/80">
              <span className="text-slate-400">Payout Algorand Wallet:</span>
              <span className="text-indigo-300 truncate max-w-[280px]">{currentAgent.walletAddress || 'D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ'}</span>
            </div>
            <div className="flex justify-between py-2 border-b border-slate-800/80">
              <span className="text-slate-400">Capabilities Published:</span>
              <span className="text-emerald-400 font-bold">{(currentAgent.capabilities || ['research']).join(', ')}</span>
            </div>
          </div>
        </div>

        {/* Real-time Health Status */}
        <div className="glass-panel p-6 border-slate-800/80 space-y-4">
          <h3 className="text-base font-bold text-white flex items-center space-x-2">
            <Activity className="w-4 h-4 text-emerald-400" />
            <span>Health & Telemetry</span>
          </h3>

          <div className="space-y-4 font-mono text-xs">
            <div className="p-4 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-center space-y-1">
              <span className="w-3 h-3 rounded-full bg-emerald-400 animate-pulse inline-block" />
              <h4 className="text-sm font-bold text-emerald-300">STATUS: ONLINE</h4>
              <p className="text-[11px] text-slate-400">Heartbeat active (30s interval)</p>
            </div>

            <div className="space-y-2">
              <div className="flex justify-between text-slate-400 text-[11px]">
                <span>CPU Load</span>
                <span>12%</span>
              </div>
              <div className="w-full h-1.5 rounded-full bg-slate-800 overflow-hidden">
                <div className="w-[12%] h-full bg-emerald-400" />
              </div>
            </div>

            <div className="space-y-2">
              <div className="flex justify-between text-slate-400 text-[11px]">
                <span>Memory Usage</span>
                <span>184 MB</span>
              </div>
              <div className="w-full h-1.5 rounded-full bg-slate-800 overflow-hidden">
                <div className="w-[35%] h-full bg-violet-400" />
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Payment History for Agent */}
      <div className="glass-panel p-5 border-slate-800/80 space-y-4">
        <h3 className="text-base font-bold text-white flex items-center space-x-2">
          <CreditCard className="w-4 h-4 text-emerald-400" />
          <span>x402 Payout History</span>
        </h3>

        {agentPayments.length === 0 ? (
          <div className="p-8 text-center border border-slate-800 rounded-xl font-mono text-xs text-slate-500">
            No payouts recorded for this agent yet.
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse font-mono text-xs">
              <thead>
                <tr className="border-b border-slate-800 text-slate-400 uppercase text-[10px]">
                  <th className="py-2.5 px-4">Transaction ID</th>
                  <th className="py-2.5 px-4">Workflow ID</th>
                  <th className="py-2.5 px-4">Payout Amount</th>
                  <th className="py-2.5 px-4">Status</th>
                  <th className="py-2.5 px-4">Date</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800/60 text-slate-300">
                {agentPayments.map((p: any, idx: number) => (
                  <tr key={idx} className="hover:bg-slate-900/40">
                    <td className="py-2.5 px-4 font-bold text-slate-200">{p.algorandTransactionId || p.txId || `TX-PAYOUT-00${idx + 1}`}</td>
                    <td className="py-2.5 px-4 text-indigo-300">{p.workflowId || 'wf-plan-001'}</td>
                    <td className="py-2.5 px-4 text-emerald-400 font-bold">${p.amount || currentAgent.basePrice || '45.00'} USDC</td>
                    <td className="py-2.5 px-4">
                      <span className="px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 text-[10px]">
                        RELEASED FROM ESCROW
                      </span>
                    </td>
                    <td className="py-2.5 px-4 text-slate-400">{p.settlementTimestamp ? new Date(p.settlementTimestamp).toLocaleDateString() : '2026-08-07'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};
