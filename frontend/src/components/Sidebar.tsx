import React from 'react';
import { NavLink } from 'react-router-dom';
import { LayoutDashboard, Compass, GitMerge, Store, CreditCard, BarChart3, Settings } from 'lucide-react';

export const Sidebar: React.FC = () => {
  const navItems = [
    { to: '/', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/planner', label: 'Submit Workflow', icon: Compass },
    { to: '/workflows', label: 'Workflow Details', icon: GitMerge },
    { to: '/marketplace', label: 'Marketplace', icon: Store },
    { to: '/payments', label: 'Payment History', icon: CreditCard },
    { to: '/analytics', label: 'Analytics', icon: BarChart3 },
    { to: '/admin', label: 'Admin & Settings', icon: Settings },
  ];

  return (
    <aside className="w-64 border-r border-slate-800/80 bg-[#0B0F19] flex flex-col justify-between p-4 shrink-0">
      <div className="space-y-6">
        <div className="px-3 text-[11px] font-bold tracking-wider text-slate-500 uppercase">
          Service Navigation
        </div>
        <nav className="space-y-1">
          {navItems.map((item) => {
            const Icon = item.icon;
            return (
              <NavLink
                key={item.to}
                to={item.to}
                end={item.to === '/'}
                className={({ isActive }) =>
                  `flex items-center space-x-3 rounded-xl px-3.5 py-2.5 text-sm font-semibold transition-all duration-200 ${
                    isActive
                      ? 'bg-gradient-to-r from-cyan-500/15 via-indigo-500/10 to-transparent text-cyan-400 border border-cyan-500/30 shadow-md shadow-cyan-500/5'
                      : 'text-slate-400 hover:bg-slate-900 hover:text-slate-200 border border-transparent'
                  }`
                }
              >
                <Icon className="h-4 w-4 shrink-0" />
                <span>{item.label}</span>
              </NavLink>
            );
          })}
        </nav>
      </div>

      {/* Algorand Smart Contract Escrow Info Badge */}
      <div className="p-3.5 rounded-xl bg-gradient-to-br from-slate-900 via-slate-950 to-cyan-950/30 border border-cyan-500/20 text-xs">
        <div className="flex items-center space-x-2 mb-1.5">
          <span className="relative flex h-2 w-2">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-cyan-400 opacity-75"></span>
            <span className="relative inline-flex rounded-full h-2 w-2 bg-cyan-500"></span>
          </span>
          <span className="font-bold text-cyan-400">Algorand PyTeal</span>
        </div>
        <p className="text-[11px] text-slate-400 leading-relaxed">
          Atomic Transfer Group payments executed upon 100% workflow DAG verification.
        </p>
      </div>
    </aside>
  );
};
