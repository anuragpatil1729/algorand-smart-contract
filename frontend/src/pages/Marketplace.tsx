import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { api, AgentDto } from '../services/api';
import { Store, Plus, ShieldCheck, Star, Cpu, CheckCircle2, Search, ExternalLink, X } from 'lucide-react';

export const Marketplace: React.FC = () => {
  const [capabilityFilter, setCapabilityFilter] = useState('');
  const [isRegisterOpen, setIsRegisterOpen] = useState(false);

  // Form states for registering new microservice agent
  const [name, setName] = useState('');
  const [endpoint, setEndpoint] = useState('http://localhost:8006');
  const [wallet, setWallet] = useState('');
  const [basePrice, setBasePrice] = useState('50');
  const [capabilities, setCapabilities] = useState('CUSTOM_SERVICE,AI');

  const { data: agents, refetch } = useQuery({
    queryKey: ['agents', capabilityFilter],
    queryFn: () => api.getAgents(capabilityFilter),
    refetchInterval: 5000,
  });

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.registerAgent({
        name,
        endpoint,
        walletAddress: wallet || `ALG-WALLET-CUSTOM-${Math.random().toString(36).substring(2, 8).toUpperCase()}`,
        basePrice: parseFloat(basePrice),
        supportedCapabilities: capabilities.split(',').map(c => c.trim()),
      });
      setIsRegisterOpen(false);
      refetch();
    } catch (err) {
      console.error(err);
    }
  };

  const categories = ['ALL', 'RESEARCH', 'DEVELOPMENT', 'LOGO_DESIGN', 'PRESENTATION', 'TESTING'];

  return (
    <div className="p-8 space-y-8 max-w-7xl mx-auto">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-black text-white flex items-center gap-3">
            <Store className="w-8 h-8 text-cyan-400" />
            AI Microservice Agent Marketplace
          </h1>
          <p className="text-sm text-slate-400">Discover and integrate decentralized microservice agents reachable via REST APIs.</p>
        </div>
        <button
          onClick={() => setIsRegisterOpen(true)}
          className="px-5 py-3 rounded-2xl bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 text-white font-bold text-xs uppercase tracking-wider flex items-center space-x-2 shadow-lg shadow-cyan-500/20 transition-all transform active:scale-95"
        >
          <Plus className="w-4 h-4" />
          <span>Register Microservice</span>
        </button>
      </div>

      {/* Filter Tabs */}
      <div className="flex items-center space-x-2 overflow-x-auto pb-2">
        {categories.map((cat) => (
          <button
            key={cat}
            onClick={() => setCapabilityFilter(cat === 'ALL' ? '' : cat)}
            className={`px-4 py-2 rounded-xl text-xs font-bold transition-all shrink-0 ${
              (cat === 'ALL' && !capabilityFilter) || capabilityFilter === cat
                ? 'bg-cyan-500/20 text-cyan-400 border border-cyan-500/40'
                : 'bg-slate-900 text-slate-400 border border-slate-800 hover:bg-slate-800'
            }`}
          >
            {cat}
          </button>
        ))}
      </div>

      {/* Agents Card Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {agents?.map((agent) => (
          <div key={agent.id} className="p-6 rounded-3xl glass-panel border border-slate-800 space-y-4 hover:border-cyan-500/40 transition-all group">
            
            <div className="flex items-start justify-between">
              <div className="flex items-center space-x-3">
                <div className="h-10 w-10 rounded-2xl bg-cyan-500/10 border border-cyan-500/30 flex items-center justify-center text-cyan-400 font-bold">
                  <Cpu className="w-5 h-5" />
                </div>
                <div>
                  <h3 className="text-sm font-bold text-slate-100 group-hover:text-cyan-400 transition-colors">{agent.name}</h3>
                  <span className="text-[10px] font-mono text-slate-500">{agent.endpoint}</span>
                </div>
              </div>
              <span className="inline-flex items-center space-x-1 px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                <CheckCircle2 className="w-3 h-3" />
                <span>{agent.healthStatus}</span>
              </span>
            </div>

            {/* Performance Stats */}
            <div className="grid grid-cols-3 gap-2 p-3 rounded-2xl bg-slate-950/80 border border-slate-900 text-xs">
              <div>
                <span className="text-slate-500 block text-[10px]">Base Price</span>
                <span className="font-mono font-bold text-emerald-400">{agent.basePrice} ALGO</span>
              </div>
              <div>
                <span className="text-slate-500 block text-[10px]">Success</span>
                <span className="font-mono font-bold text-cyan-400">{agent.successRate}%</span>
              </div>
              <div>
                <span className="text-slate-500 block text-[10px]">Rating</span>
                <span className="font-mono font-bold text-amber-400">{agent.rating}★</span>
              </div>
            </div>

            {/* Capabilities Tags */}
            <div className="flex flex-wrap gap-1.5 pt-1">
              {agent.supportedCapabilities.map((cap, i) => (
                <span key={i} className="px-2 py-0.5 rounded-md bg-slate-800 text-[10px] font-bold text-slate-300 border border-slate-700">
                  {cap}
                </span>
              ))}
            </div>

            {/* Algorand Wallet */}
            <div className="border-t border-slate-800/80 pt-3 text-[10px] font-mono text-slate-500 flex items-center justify-between">
              <span>Wallet:</span>
              <span className="text-indigo-400 truncate max-w-[160px]">{agent.walletAddress}</span>
            </div>

          </div>
        ))}
      </div>

      {/* Register Agent Modal */}
      {isRegisterOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4">
          <div className="w-full max-w-md rounded-3xl bg-[#0B0F19] border border-cyan-500/30 p-6 shadow-2xl space-y-6">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <h3 className="text-base font-bold text-white">Register New AI Microservice</h3>
              <button onClick={() => setIsRegisterOpen(false)} className="text-slate-400 hover:text-white"><X className="w-5 h-5" /></button>
            </div>

            <form onSubmit={handleRegister} className="space-y-4 text-xs">
              <div>
                <label className="text-slate-400 block mb-1">Agent Name</label>
                <input required type="text" value={name} onChange={e => setName(e.target.value)} placeholder="e.g. Translation & Localization Agent" className="w-full p-3 rounded-xl bg-slate-950 border border-slate-800 text-white focus:outline-none focus:border-cyan-500" />
              </div>
              <div>
                <label className="text-slate-400 block mb-1">Endpoint URL</label>
                <input required type="text" value={endpoint} onChange={e => setEndpoint(e.target.value)} placeholder="http://localhost:8006" className="w-full p-3 rounded-xl bg-slate-950 border border-slate-800 text-white focus:outline-none focus:border-cyan-500 font-mono" />
              </div>
              <div>
                <label className="text-slate-400 block mb-1">Algorand Wallet Address</label>
                <input type="text" value={wallet} onChange={e => setWallet(e.target.value)} placeholder="ALG-WALLET-CUSTOM-..." className="w-full p-3 rounded-xl bg-slate-950 border border-slate-800 text-white focus:outline-none focus:border-cyan-500 font-mono" />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="text-slate-400 block mb-1">Base Price (ALGO)</label>
                  <input type="number" value={basePrice} onChange={e => setBasePrice(e.target.value)} className="w-full p-3 rounded-xl bg-slate-950 border border-slate-800 text-white focus:outline-none focus:border-cyan-500 font-mono" />
                </div>
                <div>
                  <label className="text-slate-400 block mb-1">Capabilities (CSV)</label>
                  <input type="text" value={capabilities} onChange={e => setCapabilities(e.target.value)} className="w-full p-3 rounded-xl bg-slate-950 border border-slate-800 text-white focus:outline-none focus:border-cyan-500" />
                </div>
              </div>

              <div className="pt-4 flex justify-end space-x-3">
                <button type="button" onClick={() => setIsRegisterOpen(false)} className="px-4 py-2.5 rounded-xl bg-slate-900 text-slate-400 font-bold">Cancel</button>
                <button type="submit" className="px-6 py-2.5 rounded-xl bg-cyan-500 hover:bg-cyan-400 text-slate-950 font-bold shadow-lg shadow-cyan-500/20">Register Service</button>
              </div>
            </form>
          </div>
        </div>
      )}

    </div>
  );
};
