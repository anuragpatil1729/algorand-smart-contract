import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { api } from '../services/api';
import { CreditCard, ShieldCheck, CheckCircle2, ArrowDownRight, ExternalLink } from 'lucide-react';

export const PaymentsPage: React.FC = () => {
  const { data: workflows } = useQuery({ queryKey: ['workflows'], queryFn: api.listWorkflows, refetchInterval: 3000 });

  return (
    <div className="p-8 space-y-8 max-w-7xl mx-auto">
      
      {/* Header */}
      <div className="space-y-2">
        <h1 className="text-3xl font-black text-white flex items-center gap-3">
          <CreditCard className="w-8 h-8 text-cyan-400" />
          Algorand Escrow & Atomic Payments
        </h1>
        <p className="text-sm text-slate-400">
          Verify multi-agent payout distributions locked in PyTeal Escrow and settled via Algorand Atomic Group Transfers.
        </p>
      </div>

      {/* Escrow Status Summary */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="p-6 rounded-3xl bg-gradient-to-br from-slate-900 to-cyan-950/40 border border-cyan-500/30 space-y-3 shadow-xl">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Escrow Contract</span>
            <ShieldCheck className="w-5 h-5 text-cyan-400" />
          </div>
          <div className="font-mono text-sm font-bold text-white break-all">AGENTMESH_ESCROW_CONTRACT_7X9V</div>
          <span className="inline-flex items-center space-x-1 px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
            <CheckCircle2 className="w-3 h-3" /> ACTIVE ON ALGORAND
          </span>
        </div>

        <div className="p-6 rounded-3xl glass-panel border border-slate-800 space-y-3 shadow-xl">
          <span className="text-xs font-bold text-slate-400 uppercase tracking-wider block">Network Protocol Fee</span>
          <div className="font-mono text-2xl font-black text-emerald-400">1.5%</div>
          <p className="text-[11px] text-slate-400 leading-relaxed">Auto-routed into Network Fee Pool upon successful DAG signoff.</p>
        </div>

        <div className="p-6 rounded-3xl glass-panel border border-slate-800 space-y-3 shadow-xl">
          <span className="text-xs font-bold text-slate-400 uppercase tracking-wider block">Atomic Guarantee</span>
          <div className="font-mono text-2xl font-black text-cyan-400">0 Counterparty Risk</div>
          <p className="text-[11px] text-slate-400 leading-relaxed">If any agent task fails, the entire transaction group fails and funds refund to user.</p>
        </div>
      </div>

      {/* Workflows Escrow & Payments Table */}
      <div className="p-6 rounded-3xl glass-panel border border-slate-800 space-y-4">
        <h3 className="text-sm font-bold text-white">Workflow Escrow Settlement Logs</h3>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead>
              <tr className="border-b border-slate-800 text-slate-400 uppercase tracking-wider text-[10px]">
                <th className="p-3">Workflow ID</th>
                <th className="p-3">Escrow Address</th>
                <th className="p-3">Escrow Status</th>
                <th className="p-3">Total Amount</th>
                <th className="p-3">Tasks</th>
                <th className="p-3">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60 font-mono">
              {workflows && workflows.length > 0 ? (
                workflows.map((w) => (
                  <tr key={w.id} className="hover:bg-slate-900/50 transition-colors">
                    <td className="p-3 font-bold text-cyan-400">{w.id}</td>
                    <td className="p-3 text-slate-400">{w.escrowAddress || 'AGENTMESH_ESCROW_CONTRACT'}</td>
                    <td className="p-3">
                      <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-bold ${
                        w.escrowStatus === 'RELEASED' ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30' :
                        w.escrowStatus === 'LOCKED' ? 'bg-cyan-500/20 text-cyan-400 border border-cyan-500/30' :
                        'bg-slate-800 text-slate-400'
                      }`}>
                        {w.escrowStatus || 'NOT_CREATED'}
                      </span>
                    </td>
                    <td className="p-3 font-bold text-emerald-400">{w.totalPrice} ALGO</td>
                    <td className="p-3 text-slate-300">{w.tasks?.length || 0} Agents</td>
                    <td className="p-3 font-sans">
                      <a href={`/workflows?id=${w.id}`} className="text-cyan-400 hover:underline font-bold text-[11px] flex items-center gap-1">
                        Explorer <ExternalLink className="w-3 h-3" />
                      </a>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={6} className="text-center py-8 text-slate-500 font-sans">
                    No payments executed yet. Launch a workflow from the Submit Workflow tab!
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

    </div>
  );
};
