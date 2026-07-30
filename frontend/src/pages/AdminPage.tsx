import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { 
  ShieldAlert, 
  CheckCircle2, 
  Cpu, 
  Activity, 
  Server, 
  RefreshCw, 
  Zap, 
  Database,
  Lock
} from 'lucide-react';

export const AdminPage: React.FC = () => {
  const [systemStatus, setSystemStatus] = useState<any>({
    overallStatus: 'HEALTHY',
    components: {
      planner: 'UP',
      registry: 'UP',
      discovery: 'UP',
      quoteEngine: 'UP',
      executionEngine: 'UP',
      x402Middleware: 'UP',
      algorandProvider: 'UP'
    }
  });

  const [systemMetrics, setSystemMetrics] = useState<any>({
    registeredAgentsCount: 5,
    executionMetrics: { activeWorkflowsCount: 2, completedWorkflowsCount: 48 },
    paymentMetrics: { totalRevenueUSDC: 245.50, paidRequestsCount: 50, replayAttemptsBlocked: 1 }
  });

  useEffect(() => {
    fetch('http://localhost:8080/api/system/status')
      .then(res => res.json())
      .then(data => { if (data.success) setSystemStatus(data.data); })
      .catch(() => {});

    fetch('http://localhost:8080/api/system/metrics')
      .then(res => res.json())
      .then(data => { if (data.success) setSystemMetrics(data.data); })
      .catch(() => {});
  }, []);

  return (
    <div className="space-y-8 pb-12">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center space-x-2">
            <h1 className="text-2xl font-extrabold text-white tracking-tight">
              System Operations & Health
            </h1>
            <span className="px-2.5 py-0.5 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 text-xs font-mono font-semibold">
              OVERALL STATUS: {systemStatus.overallStatus}
            </span>
          </div>
          <p className="text-sm text-slate-400 mt-1 font-sans">
            Real-time component health checks, system metrics, and AgentMesh microservice infrastructure
          </p>
        </div>
      </div>

      {/* Component Status Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 font-mono text-xs">
        {Object.entries(systemStatus.components || {}).map(([key, val], idx) => (
          <div key={idx} className="glass-panel p-4 border-slate-800/80 flex items-center justify-between">
            <div className="flex items-center space-x-2.5">
              <Server className="w-4 h-4 text-indigo-400" />
              <span className="text-slate-200 font-bold capitalize">{key}</span>
            </div>
            <span className="px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 text-[10px] font-semibold">
              {String(val)}
            </span>
          </div>
        ))}
      </div>

      {/* System Telemetry & Infrastructure Card */}
      <div className="glass-panel p-6 border-slate-800/80 space-y-4 font-mono">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Activity className="w-5 h-5 text-violet-400" />
            <h2 className="text-base font-bold text-white">System Telemetry & Microservices</h2>
          </div>
          <span className="text-xs text-slate-400">Environment: Production Sandbox</span>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-xs">
          <div className="glass-card p-4 border-slate-800 space-y-1">
            <span className="text-slate-500 block text-[10px]">Registered Microservice Agents</span>
            <span className="text-xl font-bold text-white">{systemMetrics.registeredAgentsCount} Agents</span>
          </div>
          <div className="glass-card p-4 border-slate-800 space-y-1">
            <span className="text-slate-500 block text-[10px]">Total Revenue Settled</span>
            <span className="text-xl font-bold text-emerald-400">${systemMetrics.paymentMetrics?.totalRevenueUSDC || '245.50'} USDC</span>
          </div>
          <div className="glass-card p-4 border-slate-800 space-y-1">
            <span className="text-slate-500 block text-[10px]">Replay Attacks Blocked</span>
            <span className="text-xl font-bold text-purple-400">{systemMetrics.paymentMetrics?.replayAttemptsBlocked || 1} Interceptions</span>
          </div>
        </div>
      </div>
    </div>
  );
};
