import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { ShieldCheck, CheckCircle2, Wallet, ExternalLink, X, ArrowRight, Zap } from 'lucide-react';

interface AlgorandModalProps {
  isOpen: boolean;
  onClose: () => void;
  challengeData?: any;
  receiptData?: any;
}

export const AlgorandModal: React.FC<AlgorandModalProps> = ({
  isOpen,
  onClose,
  challengeData,
  receiptData
}) => {
  if (!isOpen) return null;

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md">
        <motion.div
          initial={{ opacity: 0, scale: 0.95, y: 10 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.95, y: 10 }}
          className="glass-panel w-full max-w-xl p-6 border-slate-800 bg-slate-900/95 shadow-2xl relative"
        >
          {/* Close button */}
          <button
            onClick={onClose}
            className="absolute top-4 right-4 text-slate-400 hover:text-slate-200 p-1.5 rounded-lg hover:bg-slate-800 transition-colors"
          >
            <X className="w-5 h-5" />
          </button>

          {/* Modal Header */}
          <div className="flex items-center space-x-3 mb-6">
            <div className="p-3 rounded-2xl bg-gradient-to-tr from-violet-600 to-indigo-600 shadow-lg shadow-violet-500/30">
              <ShieldCheck className="w-6 h-6 text-white" />
            </div>
            <div>
              <h3 className="text-lg font-bold text-slate-100 tracking-tight">
                x402 Algorand Testnet Settlement
              </h3>
              <p className="text-xs text-slate-400 font-mono">
                Official Pay-Per-Use Protocol Flow
              </p>
            </div>
          </div>

          {/* 5-step Flow Visualizer */}
          <div className="grid grid-cols-5 gap-2 mb-6">
            {[
              { step: '1', title: 'Request', active: true },
              { step: '2', title: '402 Challenge', active: true },
              { step: '3', title: 'AVM Signature', active: true },
              { step: '4', title: 'Facilitator', active: true },
              { step: '5', title: 'Settled', active: !!receiptData }
            ].map((s, idx) => (
              <div key={idx} className="flex flex-col items-center">
                <div className={`w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold font-mono ${
                  s.active ? 'bg-violet-600 text-white shadow-md shadow-violet-600/40' : 'bg-slate-800 text-slate-500'
                }`}>
                  {s.step}
                </div>
                <span className="text-[10px] text-slate-400 font-mono mt-1 text-center">
                  {s.title}
                </span>
              </div>
            ))}
          </div>

          {/* Receipt Details Card */}
          {receiptData ? (
            <div className="glass-card p-4 border-emerald-500/30 bg-emerald-950/10 space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-xs font-semibold text-emerald-400 flex items-center space-x-1.5 font-mono">
                  <CheckCircle2 className="w-4 h-4" />
                  <span>Verified by x402 Facilitator</span>
                </span>
                <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-300">
                  STATUS 200 SETTLED
                </span>
              </div>

              <div className="space-y-2 text-xs font-mono">
                <div className="flex justify-between py-1 border-b border-slate-800/80">
                  <span className="text-slate-400">Algorand Tx ID:</span>
                  <span className="text-slate-200 font-semibold">{receiptData.algorandTransactionId || 'TX-ALGO-DEMO-9988'}</span>
                </div>
                <div className="flex justify-between py-1 border-b border-slate-800/80">
                  <span className="text-slate-400">Settled Amount:</span>
                  <span className="text-emerald-400 font-bold">${receiptData.amount || '4.50'} USDC</span>
                </div>
                <div className="flex justify-between py-1 border-b border-slate-800/80">
                  <span className="text-slate-400">Receipt Hash:</span>
                  <span className="text-slate-400 truncate max-w-[220px]" title={receiptData.receiptHash}>
                    {receiptData.receiptHash || '8f12a3b4c5d6e7f8901234567890abcd'}
                  </span>
                </div>
              </div>
            </div>
          ) : (
            <div className="glass-card p-4 border-violet-500/30 bg-violet-950/10 text-center space-y-2">
              <Zap className="w-6 h-6 text-violet-400 mx-auto animate-bounce" />
              <p className="text-xs text-violet-200 font-mono">
                Signing AVM Transaction & Verifying with Plausible Facilitator...
              </p>
            </div>
          )}

          {/* Action Footer */}
          <div className="mt-6 flex items-center justify-end space-x-3">
            <button
              onClick={onClose}
              className="px-4 py-2 rounded-xl text-xs font-medium text-slate-300 hover:text-white bg-slate-800 hover:bg-slate-700 transition-colors"
            >
              Close
            </button>
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
