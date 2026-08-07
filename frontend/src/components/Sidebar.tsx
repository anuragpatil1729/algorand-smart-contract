import React from 'react';
import { NavLink } from 'react-router-dom';
import { 
  LayoutDashboard, 
  Workflow, 
  Store, 
  CreditCard, 
  BarChart3, 
  ShieldAlert, 
  Layers,
  Sparkles,
  ChevronRight
} from 'lucide-react';
import { motion } from 'framer-motion';

export const Sidebar: React.FC = () => {
  const navItems = [
    { to: '/', label: 'Mission Control', icon: LayoutDashboard, badge: 'Live' },
    { to: '/planner', label: 'Workflow Builder', icon: Workflow, badge: 'Hero' },
    { to: '/marketplace', label: 'Agent Marketplace', icon: Store, count: '5' },
    { to: '/agent-dashboard', label: 'Agent Portal', icon: Sparkles, badge: 'New' },
    { to: '/payments', label: 'x402 Payments', icon: CreditCard, badge: 'USDC' },
    { to: '/analytics', label: 'Analytics', icon: BarChart3 },
    { to: '/admin', label: 'System Health', icon: ShieldAlert, badge: 'UP' },
  ];

  return (
    <aside className="w-64 bg-[#070b14]/90 border-r border-slate-800/80 flex flex-col justify-between p-4 h-[calc(100vh-61px)] sticky top-[61px]">
      <div className="space-y-6">
        {/* Section Header */}
        <div className="px-3 pt-2">
          <p className="text-[10px] font-semibold text-slate-500 uppercase tracking-wider font-mono">
            Navigation Menu
          </p>
        </div>

        {/* Links */}
        <nav className="space-y-1.5">
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `relative flex items-center justify-between px-3.5 py-2.5 rounded-xl text-sm font-medium transition-all duration-200 group ${
                  isActive
                    ? 'bg-gradient-to-r from-violet-600/20 to-indigo-600/10 text-white border border-violet-500/30 shadow-md shadow-violet-500/10'
                    : 'text-slate-400 hover:text-slate-100 hover:bg-slate-900/60'
                }`
              }
            >
              {({ isActive }) => (
                <>
                  <div className="flex items-center space-x-3">
                    <item.icon className={`w-4 h-4 transition-colors ${isActive ? 'text-violet-400' : 'text-slate-400 group-hover:text-slate-200'}`} />
                    <span>{item.label}</span>
                  </div>

                  {item.badge && (
                    <span className={`text-[10px] font-mono px-2 py-0.5 rounded-full font-semibold ${
                      item.badge === 'Hero' 
                        ? 'bg-gradient-to-r from-violet-500 to-indigo-500 text-white shadow-sm shadow-violet-500/50' 
                        : item.badge === 'USDC' 
                        ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' 
                        : 'bg-slate-800 text-slate-300'
                    }`}>
                      {item.badge}
                    </span>
                  )}

                  {isActive && (
                    <motion.div
                      layoutId="activeIndicator"
                      className="absolute right-1 w-1 h-5 bg-violet-500 rounded-full"
                      transition={{ type: 'spring', stiffness: 300, damping: 30 }}
                    />
                  )}
                </>
              )}
            </NavLink>
          ))}
        </nav>
      </div>

      {/* Footer System Summary Box */}
      <div className="glass-card p-3.5 border-slate-800/80 bg-slate-950/40">
        <div className="flex items-center justify-between mb-2">
          <div className="flex items-center space-x-2">
            <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
            <span className="text-xs font-semibold text-slate-200 font-mono">AgentMesh Node</span>
          </div>
          <span className="text-[10px] text-slate-500 font-mono">v1.0.0</span>
        </div>
        <p className="text-[11px] text-slate-400 leading-relaxed font-sans">
          Algorand Atomic Escrow & x402 Protocol Runtime
        </p>
      </div>
    </aside>
  );
};
