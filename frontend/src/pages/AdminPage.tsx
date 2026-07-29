import React, { useState, useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { api, ScoringConfig } from '../services/api';
import { Settings, Sliders, Terminal, Save, CheckCircle2 } from 'lucide-react';

export const AdminPage: React.FC = () => {
  const { data: config, refetch } = useQuery({ queryKey: ['scoringConfig'], queryFn: api.getScoringConfig });
  const { data: logs } = useQuery({ queryKey: ['adminLogs'], queryFn: () => api.getLogs(), refetchInterval: 3000 });

  const [reputation, setReputation] = useState(0.35);
  const [successRate, setSuccessRate] = useState(0.25);
  const [confidence, setConfidence] = useState(0.20);
  const [price, setPrice] = useState(0.10);
  const [eta, setEta] = useState(0.10);
  const [saved, setSaved] = useState(false);

  useEffect(() => {
    if (config) {
      setReputation(config.reputationWeight);
      setSuccessRate(config.successRateWeight);
      setConfidence(config.confidenceWeight);
      setPrice(config.priceWeight);
      setEta(config.etaWeight);
    }
  }, [config]);

  const handleSaveConfig = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.updateScoringConfig({
        reputationWeight: reputation,
        successRateWeight: successRate,
        confidenceWeight: confidence,
        priceWeight: price,
        etaWeight: eta,
      });
      setSaved(true);
      refetch();
      setTimeout(() => setSaved(false), 3000);
    } catch (err) {
      console.error(err);
    }
  };

  const totalWeight = Math.round((reputation + successRate + confidence + price + eta) * 100) / 100;

  return (
    <div className="p-8 space-y-8 max-w-7xl mx-auto">
      
      {/* Header */}
      <div className="space-y-2">
        <h1 className="text-3xl font-black text-white flex items-center gap-3">
          <Settings className="w-8 h-8 text-cyan-400" />
          Admin & Algorithm Configuration
        </h1>
        <p className="text-sm text-slate-400">
          Adjust the weighted scoring algorithm parameters that determine agent selection for task DAG execution.
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        
        {/* Scoring Algorithm Config Panel */}
        <div className="p-6 rounded-3xl glass-panel border border-slate-800 space-y-6">
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <h3 className="text-sm font-bold text-white flex items-center gap-2">
              <Sliders className="w-4 h-4 text-cyan-400" />
              Agent Selection Scoring Formula
            </h3>
            <span className={`text-xs font-mono font-bold px-2.5 py-0.5 rounded-full border ${
              totalWeight === 1.0 ? 'bg-emerald-500/20 text-emerald-400 border-emerald-500/40' : 'bg-amber-500/20 text-amber-400 border-amber-500/40'
            }`}>
              Sum = {totalWeight}
            </span>
          </div>

          <form onSubmit={handleSaveConfig} className="space-y-5 text-xs">
            
            <div className="space-y-2">
              <div className="flex justify-between font-semibold text-slate-300">
                <span>Reputation / Rating Weight</span>
                <span className="font-mono text-cyan-400">{(reputation * 100).toFixed(0)}%</span>
              </div>
              <input type="range" min="0" max="1" step="0.05" value={reputation} onChange={e => setReputation(parseFloat(e.target.value))} className="w-full accent-cyan-400" />
            </div>

            <div className="space-y-2">
              <div className="flex justify-between font-semibold text-slate-300">
                <span>Success Rate Weight</span>
                <span className="font-mono text-indigo-400">{(successRate * 100).toFixed(0)}%</span>
              </div>
              <input type="range" min="0" max="1" step="0.05" value={successRate} onChange={e => setSuccessRate(parseFloat(e.target.value))} className="w-full accent-indigo-400" />
            </div>

            <div className="space-y-2">
              <div className="flex justify-between font-semibold text-slate-300">
                <span>Agent Confidence Weight</span>
                <span className="font-mono text-purple-400">{(confidence * 100).toFixed(0)}%</span>
              </div>
              <input type="range" min="0" max="1" step="0.05" value={confidence} onChange={e => setConfidence(parseFloat(e.target.value))} className="w-full accent-purple-400" />
            </div>

            <div className="space-y-2">
              <div className="flex justify-between font-semibold text-slate-300">
                <span>Lowest Price Weight</span>
                <span className="font-mono text-emerald-400">{(price * 100).toFixed(0)}%</span>
              </div>
              <input type="range" min="0" max="1" step="0.05" value={price} onChange={e => setPrice(parseFloat(e.target.value))} className="w-full accent-emerald-400" />
            </div>

            <div className="space-y-2">
              <div className="flex justify-between font-semibold text-slate-300">
                <span>Fastest ETA Weight</span>
                <span className="font-mono text-amber-400">{(eta * 100).toFixed(0)}%</span>
              </div>
              <input type="range" min="0" max="1" step="0.05" value={eta} onChange={e => setEta(parseFloat(e.target.value))} className="w-full accent-amber-400" />
            </div>

            <div className="pt-4 flex items-center justify-between border-t border-slate-800">
              {saved ? (
                <span className="text-emerald-400 font-bold flex items-center gap-1">
                  <CheckCircle2 className="w-4 h-4" /> Config Saved Successfully!
                </span>
              ) : <div></div>}
              <button
                type="submit"
                className="px-6 py-3 rounded-2xl bg-cyan-500 hover:bg-cyan-400 text-slate-950 font-bold text-xs uppercase tracking-wider flex items-center space-x-2 shadow-lg shadow-cyan-500/20 transition-all transform active:scale-95"
              >
                <Save className="w-4 h-4" />
                <span>Save Formula Weights</span>
              </button>
            </div>

          </form>
        </div>

        {/* Global System Logs Stream */}
        <div className="p-6 rounded-3xl glass-panel border border-slate-800 space-y-4">
          <h3 className="text-sm font-bold text-white flex items-center gap-2 border-b border-slate-800 pb-3">
            <Terminal className="w-4 h-4 text-cyan-400" />
            Global System Telemetry Log Stream
          </h3>
          <div className="h-96 overflow-y-auto rounded-2xl bg-slate-950 p-4 font-mono text-[11px] text-slate-300 space-y-2 border border-slate-900">
            {logs && logs.length > 0 ? (
              logs.map((log) => (
                <div key={log.id} className="leading-relaxed">
                  <span className="text-slate-600">[{log.timestamp.substring(11, 19)}]</span>{' '}
                  <span className={log.logLevel === 'ERROR' ? 'text-rose-400 font-bold' : (log.logLevel === 'WARN' ? 'text-amber-400 font-bold' : 'text-cyan-400 font-bold')}>
                    [{log.logLevel}]
                  </span>{' '}
                  <span className="text-slate-300">{log.message}</span>
                </div>
              ))
            ) : (
              <div className="text-slate-600 text-center py-16">No system logs logged yet...</div>
            )}
          </div>
        </div>

      </div>

    </div>
  );
};
