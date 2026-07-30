import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { 
  ShieldCheck, 
  CreditCard, 
  Copy, 
  CheckCircle2, 
  ExternalLink, 
  ArrowRight, 
  DollarSign, 
  Clock,
  Zap
} from 'lucide-react';

export const PaymentsPage: React.FC = () => {
  const [copiedId, setCopiedId] = useState<string | null>(null);

  const settlements = [
    { txId: 'TX-ALGO-TEST-998811', workflowId: 'wf-plan-8f12a3', amount: '5.25', asset: 'USDC', verified: true, time: '11:28:04', receiptHash: '8f12a3b4c5d6e7f8901234567890abcd' },
    { txId: 'TX-ALGO-TEST-887722', workflowId: 'wf-plan-7c91b4', amount: '4.50', asset: 'USDC', verified: true, time: '11:15:20', receiptHash: '7c91b4a5c6d7e8f9012345678901bcde' },
    { txId: 'TX-ALGO-TEST-776633', workflowId: 'wf-plan-6a50e2', amount: '6.00', asset: 'USDC', verified: true, time: '10:45:12', receiptHash: '6a50e2b3c4d5e6f7890123456789cdef' },
    { txId: 'TX-ALGO-TEST-665544', workflowId: 'wf-plan-5f40d1', amount: '7.50', asset: 'USDC', verified: true, time: '09:30:00', receiptHash: '5f40d1a2b3c4d5e6789012345678defa' }
  ];

  const copyTx = (txId: string) => {
    navigator.clipboard.writeText(txId);
    setCopiedId(txId);
    setTimeout(() => setCopiedId(null), 2000);
  };

  return (
    <div className="space-y-8 pb-12">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center space-x-2">
            <h1 className="text-2xl font-extrabold text-white tracking-tight">
              x402 Algorand Payment Center
            </h1>
            <span className="px-2.5 py-0.5 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 text-xs font-mono font-semibold">
              USDC ASA #31566704
            </span>
          </div>
          <p className="text-sm text-slate-400 mt-1 font-sans">
            Inspect real-time HTTP 402 challenge verifications, facilitator receipts, and Algorand Testnet settlements
          </p>
        </div>
      </div>

      {/* 5-Step x402 Flow Visualization Banner */}
      <div className="glass-panel p-6 border-slate-800/80">
        <h3 className="text-sm font-bold text-white mb-4 flex items-center space-x-2">
          <Zap className="w-4 h-4 text-violet-400" />
          <span>Official x402 Protocol Execution Flow</span>
        </h3>

        <div className="grid grid-cols-1 md:grid-cols-5 gap-3">
          {[
            { step: '1', title: 'Paid Request', desc: 'POST /api/execution/start' },
            { step: '2', title: '402 Challenge', desc: 'HTTP 402 + Merchant & Price Header' },
            { step: '3', title: 'Wallet Signature', desc: 'Client signs AVM USDC transfer' },
            { step: '4', title: 'Facilitator Verify', desc: 'goplausible validates proof' },
            { step: '5', title: 'Receipt & Execute', desc: 'SHA-256 Receipt returned' }
          ].map((s, idx) => (
            <div key={idx} className="glass-card p-4 border-slate-800/80 space-y-2 relative">
              <div className="flex items-center justify-between">
                <span className="w-7 h-7 rounded-full bg-gradient-to-r from-violet-600 to-indigo-600 text-white font-mono font-bold text-xs flex items-center justify-center shadow-md shadow-violet-500/30">
                  {s.step}
                </span>
                {idx < 4 && <ArrowRight className="w-4 h-4 text-slate-600 hidden md:block" />}
              </div>
              <h4 className="text-xs font-bold text-slate-200 font-mono">{s.title}</h4>
              <p className="text-[11px] text-slate-400 font-sans leading-snug">{s.desc}</p>
            </div>
          ))}
        </div>
      </div>

      {/* Transactions Table */}
      <div className="glass-panel p-5 border-slate-800/80 space-y-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <ShieldCheck className="w-5 h-5 text-emerald-400" />
            <h2 className="text-base font-bold text-white">Verified x402 Settlements History</h2>
          </div>
          <span className="text-xs font-mono text-slate-400">Total Settled: <strong className="text-emerald-400">$23.25 USDC</strong></span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse font-mono text-xs">
            <thead>
              <tr className="border-b border-slate-800 text-slate-400 uppercase text-[10px]">
                <th className="py-3 px-4">Algorand Tx ID</th>
                <th className="py-3 px-4">Workflow ID</th>
                <th className="py-3 px-4">Amount Paid</th>
                <th className="py-3 px-4">Status</th>
                <th className="py-3 px-4">Timestamp</th>
                <th className="py-3 px-4">SHA-256 Receipt Hash</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60 text-slate-300">
              {settlements.map((s, idx) => (
                <tr key={idx} className="hover:bg-slate-900/40 transition-colors">
                  <td className="py-3 px-4 font-semibold text-slate-200">
                    <div className="flex items-center space-x-1.5">
                      <span>{s.txId}</span>
                      <button onClick={() => copyTx(s.txId)} className="text-slate-500 hover:text-slate-200 p-1">
                        <Copy className="w-3 h-3" />
                      </button>
                    </div>
                  </td>
                  <td className="py-3 px-4 text-indigo-300">{s.workflowId}</td>
                  <td className="py-3 px-4 font-bold text-emerald-400">${s.amount} {s.asset}</td>
                  <td className="py-3 px-4">
                    <span className="px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 text-[10px]">
                      FACILITATOR VERIFIED
                    </span>
                  </td>
                  <td className="py-3 px-4 text-slate-400">{s.time}</td>
                  <td className="py-3 px-4 text-slate-500 truncate max-w-[180px]" title={s.receiptHash}>
                    {s.receiptHash}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
