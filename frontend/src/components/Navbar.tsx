import React from 'react';
import { Shield, Zap, Wallet, Cpu, Activity } from 'lucide-react';

export const Navbar: React.FC = () => {
  return (
    <header className="sticky top-0 z-40 w-full border-b border-slate-800/80 bg-[#0B0F19]/80 backdrop-blur-xl">
      <div className="flex h-16 items-center justify-between px-6">
        
        {/* Brand Logo */}
        <div className="flex items-center space-x-3">
          <div className="relative flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-tr from-cyan-500 via-indigo-500 to-purple-600 p-0.5 shadow-lg shadow-cyan-500/20">
            <div className="flex h-full w-full items-center justify-center rounded-[10px] bg-[#0B0F19]">
              <Zap className="h-5 w-5 text-cyan-400 animate-pulse" />
            </div>
          </div>
          <div>
            <div className="flex items-center space-x-2">
              <span className="text-xl font-extrabold tracking-tight bg-gradient-to-r from-white via-slate-200 to-cyan-400 bg-clip-text text-transparent">
                AgentMesh
              </span>
              <span className="rounded-full bg-cyan-950/80 px-2 py-0.5 text-[10px] font-bold text-cyan-400 border border-cyan-500/30">
                v1.0 ALGORAND
              </span>
            </div>
            <p className="text-[11px] font-medium text-slate-400">Autonomous AI Multi-Agent Service Router</p>
          </div>
        </div>

        {/* Live Network Indicators & Algorand Wallet */}
        <div className="flex items-center space-x-4">
          <div className="hidden md:flex items-center space-x-2 rounded-xl bg-slate-900/90 border border-slate-800 px-3 py-1.5 text-xs text-slate-300">
            <Activity className="h-4 w-4 text-emerald-400 animate-pulse" />
            <span>Router Engine: <strong className="text-emerald-400">ONLINE</strong></span>
            <span className="text-slate-600">|</span>
            <Cpu className="h-4 w-4 text-cyan-400" />
            <span>Agents: <strong className="text-cyan-400">5 Active</strong></span>
          </div>

          {/* Algorand Wallet Chip */}
          <div className="flex items-center space-x-3 rounded-xl bg-gradient-to-r from-slate-900 via-slate-900 to-cyan-950/40 border border-cyan-500/30 px-3.5 py-1.5 text-xs shadow-inner">
            <div className="flex h-7 w-7 items-center justify-center rounded-lg bg-cyan-500/10 text-cyan-400 border border-cyan-500/30">
              <Shield className="h-4 w-4" />
            </div>
            <div>
              <div className="text-[10px] font-semibold text-slate-400 flex items-center gap-1">
                <Wallet className="h-3 w-3 text-cyan-400" /> Escrow Balance
              </div>
              <span className="font-mono text-sm font-bold text-cyan-300">
                1,250.00 <span className="text-[10px] text-cyan-400">ALGO</span>
              </span>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};
