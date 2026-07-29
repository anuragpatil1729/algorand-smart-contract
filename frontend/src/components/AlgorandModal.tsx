import React from 'react';
import { ShieldCheck, X, ExternalLink, CheckCircle2, Copy } from 'lucide-react';
import { PaymentDetailsDto } from '../services/api';

interface AlgorandModalProps {
  isOpen: boolean;
  onClose: () => void;
  paymentDetails?: PaymentDetailsDto | null;
}

export const AlgorandModal: React.FC<AlgorandModalProps> = ({ isOpen, onClose, paymentDetails }) => {
  if (!isOpen || !paymentDetails) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4 animate-in fade-in duration-200">
      <div className="w-full max-w-3xl rounded-3xl bg-[#0B0F19] border border-cyan-500/30 p-6 shadow-2xl space-y-6">
        
        {/* Header */}
        <div className="flex items-center justify-between border-b border-slate-800 pb-4">
          <div className="flex items-center space-x-3">
            <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-cyan-500/10 border border-cyan-500/30 text-cyan-400">
              <ShieldCheck className="h-6 w-6" />
            </div>
            <div>
              <h3 className="text-lg font-bold text-slate-100 flex items-center gap-2">
                Algorand Atomic Transfer Group Explorer
                <span className="rounded-full bg-emerald-500/20 px-2 py-0.5 text-xs font-bold text-emerald-400 border border-emerald-500/30">
                  VERIFIED ON-CHAIN
                </span>
              </h3>
              <p className="text-xs text-slate-400">Multi-Agent Escrow Settlement Receipt</p>
            </div>
          </div>
          <button 
            onClick={onClose}
            className="p-2 rounded-xl text-slate-400 hover:bg-slate-800 hover:text-white transition-colors"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        {/* Group Info Grid */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 p-4 rounded-2xl bg-slate-900/90 border border-slate-800 text-xs">
          <div>
            <span className="text-slate-500 block">Atomic Group ID</span>
            <span className="font-mono font-bold text-cyan-400 truncate block mt-0.5">{paymentDetails.txGroupId}</span>
          </div>
          <div>
            <span className="text-slate-500 block">Escrow Contract</span>
            <span className="font-mono font-bold text-indigo-400 truncate block mt-0.5">AGENTMESH_ESCROW_CONTRACT</span>
          </div>
          <div>
            <span className="text-slate-500 block">Total Disbursed</span>
            <span className="font-mono font-bold text-emerald-400 block mt-0.5">{paymentDetails.totalAmount} ALGO</span>
          </div>
          <div>
            <span className="text-slate-500 block">Status</span>
            <span className="font-bold text-emerald-400 flex items-center gap-1 mt-0.5">
              <CheckCircle2 className="w-3.5 h-3.5" /> {paymentDetails.status}
            </span>
          </div>
        </div>

        {/* Transactions Table */}
        <div className="space-y-3">
          <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400">Atomic Group Transactions ({paymentDetails.transactions.length})</h4>
          <div className="max-h-64 overflow-y-auto space-y-2 rounded-xl bg-slate-950/60 p-3 border border-slate-800">
            {paymentDetails.transactions.map((tx) => (
              <div key={tx.id} className="p-3 rounded-lg bg-slate-900/80 border border-slate-800/80 flex items-center justify-between text-xs space-x-3">
                <div className="flex items-center space-x-3">
                  <div className="h-2 w-2 rounded-full bg-cyan-400"></div>
                  <div>
                    <div className="font-mono font-bold text-slate-200 flex items-center gap-1">
                      {tx.txHash}
                    </div>
                    <div className="text-[10px] text-slate-500 mt-0.5">
                      From: <span className="text-slate-400">{tx.senderWallet.substring(0, 10)}...</span> {'->'} To: <span className="text-cyan-400">{tx.receiverWallet.substring(0, 10)}...</span>
                    </div>
                  </div>
                </div>
                <div className="text-right">
                  <span className="font-mono font-bold text-emerald-400 block">{tx.amount} ALGO</span>
                  <span className="text-[10px] text-slate-500 block">Round #{tx.blockRound}</span>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Footer */}
        <div className="flex items-center justify-end space-x-3 border-t border-slate-800 pt-4">
          <button
            onClick={onClose}
            className="px-5 py-2.5 rounded-xl bg-cyan-500 hover:bg-cyan-400 text-slate-950 font-bold text-xs transition-colors shadow-lg shadow-cyan-500/20"
          >
            Close Explorer
          </button>
        </div>

      </div>
    </div>
  );
};
