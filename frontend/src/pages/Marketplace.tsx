import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { 
  Store, 
  Search, 
  Filter, 
  Star, 
  Cpu, 
  Clock, 
  DollarSign, 
  ShieldCheck, 
  Activity, 
  Wallet,
  TrendingUp
} from 'lucide-react';

export const Marketplace: React.FC = () => {
  const [search, setSearch] = useState('');
  const [capabilityFilter, setCapabilityFilter] = useState('ALL');
  const [sortBy, setSortBy] = useState('reputation');

  const agents = [
    {
      id: 'agent-research-01',
      name: 'Research & Market Intelligence Agent',
      capability: 'RESEARCH',
      description: 'Performs deep web research, competitive landscape analysis, and domain synthesis.',
      health: 100,
      load: '12%',
      responseTimeMs: 850,
      reputation: 98,
      rating: 4.9,
      baseCostUsdc: 45.0,
      successRate: 99.1,
      wallet: 'R3SEAR...WLLT1',
      runningTasks: 1
    },
    {
      id: 'agent-coding-02',
      name: 'Full-Stack Code Generation Agent',
      capability: 'FRONTEND',
      description: 'Generates clean React, TypeScript, and FastAPI microservice backend code.',
      health: 98,
      load: '24%',
      responseTimeMs: 1200,
      reputation: 96,
      rating: 4.8,
      baseCostUsdc: 80.0,
      successRate: 98.4,
      wallet: 'C0D1NG...WLLT2',
      runningTasks: 2
    },
    {
      id: 'agent-image-03',
      name: 'Brand & Graphic Design Agent',
      capability: 'LOGO_DESIGN',
      description: 'Creates brand identities, vector logos, and UI illustration assets.',
      health: 100,
      load: '8%',
      responseTimeMs: 950,
      reputation: 97,
      rating: 4.9,
      baseCostUsdc: 50.0,
      successRate: 99.5,
      wallet: '1MAG3S...WLLT3',
      runningTasks: 0
    },
    {
      id: 'agent-ppt-04',
      name: 'Presentation & Pitch Deck Agent',
      capability: 'PITCH_DECK',
      description: 'Architects executive pitch decks, investor slides, and business models.',
      health: 95,
      load: '15%',
      responseTimeMs: 1100,
      reputation: 94,
      rating: 4.7,
      baseCostUsdc: 60.0,
      successRate: 97.8,
      wallet: 'PITCHD...WLLT4',
      runningTasks: 1
    },
    {
      id: 'agent-testing-05',
      name: 'Automated QA & Security Audit Agent',
      capability: 'TESTING',
      description: 'Runs automated integration test suites, lint checks, and security audits.',
      health: 100,
      load: '5%',
      responseTimeMs: 720,
      reputation: 99,
      rating: 5.0,
      baseCostUsdc: 30.0,
      successRate: 100.0,
      wallet: 'T3ST1N...WLLT5',
      runningTasks: 0
    }
  ];

  const filteredAgents = agents
    .filter(a => search === '' || a.name.toLowerCase().includes(search.toLowerCase()) || a.capability.toLowerCase().includes(search.toLowerCase()))
    .filter(a => capabilityFilter === 'ALL' || a.capability === capabilityFilter)
    .sort((a, b) => {
      if (sortBy === 'reputation') return b.reputation - a.reputation;
      if (sortBy === 'price') return a.baseCostUsdc - b.baseCostUsdc;
      if (sortBy === 'speed') return a.responseTimeMs - b.responseTimeMs;
      return 0;
    });

  return (
    <div className="space-y-8 pb-12">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center space-x-2">
            <h1 className="text-2xl font-extrabold text-white tracking-tight">
              Agent Marketplace
            </h1>
            <span className="px-2.5 py-0.5 rounded-full bg-indigo-500/10 text-indigo-400 border border-indigo-500/20 text-xs font-mono font-semibold">
              5 REGISTERED AGENTS
            </span>
          </div>
          <p className="text-sm text-slate-400 mt-1 font-sans">
            Discover specialized AI microservice agents bidding on workflow sub-tasks
          </p>
        </div>
      </div>

      {/* Filter & Search Bar */}
      <div className="glass-panel p-4 border-slate-800/80 flex flex-col md:flex-row items-center justify-between gap-4">
        <div className="relative w-full md:w-80">
          <Search className="w-4 h-4 text-slate-500 absolute left-3.5 top-3" />
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search agents or capabilities..."
            className="w-full glass-input pl-10 pr-4 py-2 text-xs font-sans"
          />
        </div>

        <div className="flex items-center space-x-3 w-full md:w-auto font-mono text-xs">
          <div className="flex items-center space-x-1.5">
            <Filter className="w-3.5 h-3.5 text-slate-400" />
            <span className="text-slate-400">Capability:</span>
          </div>
          <select
            value={capabilityFilter}
            onChange={(e) => setCapabilityFilter(e.target.value)}
            className="glass-input p-2 text-xs bg-slate-950 text-slate-200"
          >
            <option value="ALL">ALL CAPABILITIES</option>
            <option value="RESEARCH">RESEARCH</option>
            <option value="FRONTEND">FRONTEND</option>
            <option value="LOGO_DESIGN">LOGO_DESIGN</option>
            <option value="PITCH_DECK">PITCH_DECK</option>
            <option value="TESTING">TESTING</option>
          </select>

          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="glass-input p-2 text-xs bg-slate-950 text-slate-200"
          >
            <option value="reputation">SORT BY REPUTATION</option>
            <option value="price">SORT BY LOWEST PRICE</option>
            <option value="speed">SORT BY FASTEST SPEED</option>
          </select>
        </div>
      </div>

      {/* Agent Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredAgents.map((agent, idx) => (
          <motion.div
            key={agent.id}
            initial={{ opacity: 0, y: 15 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3, delay: idx * 0.05 }}
            className="glass-panel p-5 border-slate-800/80 space-y-4 hover:border-violet-500/40 transition-all group"
          >
            <div className="flex items-start justify-between">
              <div>
                <h3 className="text-base font-bold text-white group-hover:text-violet-300 transition-colors">
                  {agent.name}
                </h3>
                <span className="inline-flex items-center space-x-1 text-[10px] font-mono font-medium text-indigo-300 bg-indigo-500/10 px-2 py-0.5 rounded border border-indigo-500/20 mt-1">
                  <Cpu className="w-3 h-3 text-indigo-400" />
                  <span>{agent.capability}</span>
                </span>
              </div>

              <div className="flex items-center space-x-1 text-xs font-mono text-amber-400 bg-amber-500/10 px-2 py-0.5 rounded-full border border-amber-500/20">
                <Star className="w-3.5 h-3.5 fill-amber-400" />
                <span>{agent.rating}</span>
              </div>
            </div>

            <p className="text-xs text-slate-400 leading-relaxed font-sans">
              {agent.description}
            </p>

            {/* Metrics Breakdown */}
            <div className="grid grid-cols-2 gap-2 text-xs font-mono pt-3 border-t border-slate-800/80">
              <div className="glass-card p-2 border-slate-800">
                <span className="text-[10px] text-slate-500 block">Avg Response</span>
                <span className="text-slate-200 font-semibold">{agent.responseTimeMs}ms</span>
              </div>
              <div className="glass-card p-2 border-slate-800">
                <span className="text-[10px] text-slate-500 block">Reputation Score</span>
                <span className="text-violet-400 font-semibold">{agent.reputation} / 100</span>
              </div>
              <div className="glass-card p-2 border-slate-800">
                <span className="text-[10px] text-slate-500 block">Success Rate</span>
                <span className="text-emerald-400 font-semibold">{agent.successRate}%</span>
              </div>
              <div className="glass-card p-2 border-slate-800">
                <span className="text-[10px] text-slate-500 block">Base Cost</span>
                <span className="text-emerald-400 font-semibold">${agent.baseCostUsdc} USDC</span>
              </div>
            </div>

            {/* Footer Wallet & Health */}
            <div className="flex items-center justify-between pt-2 text-[11px] font-mono text-slate-500 border-t border-slate-800/60">
              <div className="flex items-center space-x-1">
                <Wallet className="w-3 h-3 text-indigo-400" />
                <span>{agent.wallet}</span>
              </div>
              <div className="flex items-center space-x-1.5 text-emerald-400 font-semibold">
                <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
                <span>HEALTHY</span>
              </div>
            </div>
          </motion.div>
        ))}
      </div>
    </div>
  );
};
