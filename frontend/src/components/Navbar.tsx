import React from 'react';
import { Cpu, ShieldCheck, Zap, Wallet, ExternalLink, Activity } from 'lucide-react';
import { motion } from 'framer-motion';

export const Navbar: React.FC = () => {
  return (
    <header className="sticky top-0 z-40 bg-[#070b14]/80 backdrop-blur-xl border-b border-slate-800/80 px-6 py-3">
      <div className="flex items-center justify-between">
        {/* Left: Platform Title & Breadcrumb */}
        <div className="flex items-center space-x-3">
          <div className="flex items-center space-x-2">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-violet-600 via-indigo-600 to-cyan-400 p-0.5 shadow-lg shadow-indigo-500/20">
              <div className="w-full h-full bg-slate-950 rounded-[10px] flex items-center justify-center">
                <Zap className="w-5 h-5 text-cyan-400" />
              </div>
            </div>
            <div>
              <div className="flex items-center space-x-2">
                <span className="font-bold text-lg bg-gradient-to-r from-white via-slate-200 to-slate-400 bg-clip-text text-transparent tracking-tight">
                  AgentMesh
                </span>
                <span className="text-[10px] font-semibold px-2 py-0.5 rounded-full bg-violet-500/10 text-violet-400 border border-violet-500/20 uppercase tracking-widest">
                  v1.0 x402
                </span>
              </div>
              <p className="text-xs text-slate-400 font-mono hidden sm:block">
                Pay-Per-Use AI Orchestration Engine
              </p>
            </div>
          </div>
        </div>

        {/* Center/Right: Network Live Badges & Status */}
        <div className="flex items-center space-x-3">
          {/* Algorand Testnet Status Pill */}
          <motion.div 
            whileHover={{ scale: 1.02 }}
            className="flex items-center space-x-2 px-3 py-1.5 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-xs font-mono"
          >
            <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
            <span className="font-semibold hidden md:inline">ALGORAND TESTNET</span>
            <span className="text-slate-500 hidden md:inline">|</span>
            <span className="text-emerald-300">USDC ASA</span>
          </motion.div>

          {/* x402 Facilitator Badge */}
          <motion.div 
            whileHover={{ scale: 1.02 }}
            className="flex items-center space-x-2 px-3 py-1.5 rounded-xl bg-violet-500/10 border border-violet-500/20 text-violet-300 text-xs font-mono hidden sm:flex"
          >
            <ShieldCheck className="w-3.5 h-3.5 text-violet-400" />
            <span className="font-semibold">x402 FACILITATOR</span>
            <span className="text-slate-500">|</span>
            <span className="text-slate-400">goplausible</span>
          </motion.div>

          {/* Merchant Wallet Badge */}
          <div className="flex items-center space-x-2 px-3 py-1.5 rounded-xl bg-slate-900/80 border border-slate-800 text-slate-300 text-xs font-mono">
            <Wallet className="w-3.5 h-3.5 text-indigo-400" />
            <span className="text-slate-400 hidden lg:inline">Merchant:</span>
            <span className="text-indigo-300 font-medium">D64E...OHKPQ</span>
          </div>
        </div>
      </div>
    </header>
  );
};
