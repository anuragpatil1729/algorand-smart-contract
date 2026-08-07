import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { 
  Search, 
  Filter, 
  Star, 
  Cpu, 
  Wallet,
  Activity
} from 'lucide-react';
import { useAgentsList } from '../hooks/useDataHooks';

export const Marketplace: React.FC = () => {
  const { data: agentsData, isLoading } = useAgentsList();

  const [search, setSearch] = useState('');
  const [capabilityFilter, setCapabilityFilter] = useState('ALL');
  const [sortBy, setSortBy] = useState('reputation');

  const agents = agentsData || [];

  const filteredAgents = agents
    .filter((a: any) => search === '' || (a.name || '').toLowerCase().includes(search.toLowerCase()) || (a.capability || '').toLowerCase().includes(search.toLowerCase()) || (a.capabilities && a.capabilities.some((c: string) => c.toLowerCase().includes(search.toLowerCase()))))
    .filter((a: any) => capabilityFilter === 'ALL' || a.capability === capabilityFilter || (a.capabilities && a.capabilities.includes(capabilityFilter)))
    .filter((a: any) => sortBy !== 'verified' || a.healthScore >= 90 || a.rating >= 4.7)
    .sort((a: any, b: any) => {
      if (sortBy === 'reputation' || sortBy === 'highest-rated') return (b.rating || b.reputation || 4.5) - (a.rating || a.reputation || 4.5);
      if (sortBy === 'cheapest' || sortBy === 'price') return (a.basePrice || a.baseCostUsdc || 40) - (b.basePrice || b.baseCostUsdc || 40);
      if (sortBy === 'fastest' || sortBy === 'speed') return (a.averageResponseTime || a.responseTimeMs || 500) - (b.averageResponseTime || b.responseTimeMs || 500);
      if (sortBy === 'trending') return (b.completedTasks || b.totalRequests || 0) - (a.completedTasks || a.totalRequests || 0);
      if (sortBy === 'latest') return new Date(b.registrationTime || b.createdAt || Date.now()).getTime() - new Date(a.registrationTime || a.createdAt || Date.now()).getTime();
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
              {agents.length} REGISTERED AGENTS
            </span>
          </div>
          <p className="text-sm text-slate-400 mt-1 font-sans">
            Discover specialized AI microservice agents bidding on workflow sub-tasks via x402 Bazaar Protocol
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
            placeholder="Search agents, models, or capabilities..."
            className="w-full glass-input pl-10 pr-4 py-2 text-xs font-sans"
          />
        </div>

        <div className="flex flex-wrap items-center gap-3 w-full md:w-auto font-mono text-xs">
          <div className="flex items-center space-x-1.5">
            <Filter className="w-3.5 h-3.5 text-slate-400" />
            <span className="text-slate-400">Filter:</span>
          </div>
          <select
            value={capabilityFilter}
            onChange={(e) => setCapabilityFilter(e.target.value)}
            className="glass-input p-2 text-xs bg-slate-950 text-slate-200"
          >
            <option value="ALL">ALL CAPABILITIES</option>
            <option value="research">RESEARCH</option>
            <option value="code-generation">CODE GENERATION</option>
            <option value="vision">VISION / GRAPHICS</option>
            <option value="documentation">DOCUMENTATION</option>
            <option value="testing">TESTING / QA</option>
            <option value="database">DATABASE</option>
            <option value="deployment">DEPLOYMENT</option>
          </select>

          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="glass-input p-2 text-xs bg-slate-950 text-slate-200 font-bold"
          >
            <option value="highest-rated">⭐ HIGHEST RATED</option>
            <option value="cheapest">💲 CHEAPEST</option>
            <option value="fastest">⚡ FASTEST</option>
            <option value="verified">🛡️ VERIFIED ONLY</option>
            <option value="trending">🔥 TRENDING</option>
            <option value="latest">🆕 LATEST</option>
          </select>
        </div>
      </div>

      {/* Agent Cards Grid */}
      {isLoading ? (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {[1, 2, 3].map((i) => (
            <div key={i} className="glass-panel p-5 border-slate-800 h-64 animate-pulse" />
          ))}
        </div>
      ) : filteredAgents.length === 0 ? (
        <div className="glass-panel p-12 text-center border-slate-800 space-y-3">
          <Activity className="w-10 h-10 text-slate-600 mx-auto animate-pulse" />
          <h3 className="text-sm font-bold text-slate-300 font-mono">No Agents Matching Criteria</h3>
          <p className="text-xs text-slate-500 max-w-sm mx-auto font-sans">
            Try adjusting your search terms or filter selection.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredAgents.map((agent: any, idx: number) => (
            <motion.div
              key={agent.id || idx}
              initial={{ opacity: 0, y: 15 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.3, delay: idx * 0.05 }}
              className="glass-panel p-5 border-slate-800/80 space-y-4 hover:border-violet-500/40 transition-all group relative"
            >
              <div className="flex items-start justify-between">
                <div>
                  <h3 className="text-base font-bold text-white group-hover:text-violet-300 transition-colors">
                    {agent.name}
                  </h3>
                  <div className="flex flex-wrap gap-1 mt-1.5">
                    {(agent.capabilities || [agent.capability || 'code-generation']).slice(0, 3).map((cap: string, cIdx: number) => (
                      <span key={cIdx} className="inline-flex items-center space-x-1 text-[10px] font-mono font-medium text-indigo-300 bg-indigo-500/10 px-2 py-0.5 rounded border border-indigo-500/20">
                        <Cpu className="w-3 h-3 text-indigo-400" />
                        <span>{cap}</span>
                      </span>
                    ))}
                  </div>
                </div>

                <div className="flex items-center space-x-1 text-xs font-mono text-amber-400 bg-amber-500/10 px-2 py-0.5 rounded-full border border-amber-500/20">
                  <Star className="w-3.5 h-3.5 fill-amber-400" />
                  <span>{agent.rating || 4.8}</span>
                </div>
              </div>

              <p className="text-xs text-slate-400 leading-relaxed font-sans line-clamp-2">
                {agent.description || 'Specialized AI agent microservice registered on AgentMesh network.'}
              </p>

              {/* Metrics Breakdown */}
              <div className="grid grid-cols-3 gap-2 text-xs font-mono pt-3 border-t border-slate-800/80">
                <div className="glass-card p-2 border-slate-800">
                  <span className="text-[10px] text-slate-500 block">Avg Latency</span>
                  <span className="text-slate-200 font-semibold">{Math.round(agent.averageResponseTime || agent.responseTimeMs || 480)}ms</span>
                </div>
                <div className="glass-card p-2 border-slate-800">
                  <span className="text-[10px] text-slate-500 block">Success Rate</span>
                  <span className="text-emerald-400 font-semibold">{agent.successRate || 98.5}%</span>
                </div>
                <div className="glass-card p-2 border-slate-800">
                  <span className="text-[10px] text-slate-500 block">Price</span>
                  <span className="text-emerald-400 font-semibold">${agent.basePrice || agent.baseCostUsdc || 45.0} USDC</span>
                </div>
                <div className="glass-card p-2 border-slate-800">
                  <span className="text-[10px] text-slate-500 block">Total Jobs</span>
                  <span className="text-violet-300 font-semibold">{agent.completedTasks || agent.totalRequests || 12}</span>
                </div>
                <div className="glass-card p-2 border-slate-800 col-span-2">
                  <span className="text-[10px] text-slate-500 block">Total Earnings</span>
                  <span className="text-emerald-400 font-bold">${(agent.totalEarnings || ((agent.completedTasks || 10) * (agent.basePrice || 45))).toFixed(2)} USDC</span>
                </div>
              </div>

              {/* Footer Wallet & Health */}
              <div className="flex items-center justify-between pt-2 text-[11px] font-mono text-slate-500 border-t border-slate-800/60">
                <div className="flex items-center space-x-1 truncate max-w-[180px]">
                  <Wallet className="w-3 h-3 text-indigo-400 shrink-0" />
                  <span className="truncate">{agent.walletAddress || agent.wallet || 'D64E...JKPQ'}</span>
                </div>
                <div className="flex items-center space-x-1.5 text-emerald-400 font-semibold">
                  <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
                  <span>{agent.status || agent.healthStatus || 'ONLINE'}</span>
                </div>
              </div>
            </motion.div>
          ))}
        </div>
      )}
    </div>
  );
};
