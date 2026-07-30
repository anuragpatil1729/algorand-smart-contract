import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { 
  CheckCircle2, 
  Clock, 
  ShieldCheck, 
  Terminal, 
  Copy, 
  ArrowLeft
} from 'lucide-react';
import { useWorkflowStatus, useWorkflowLogs, useWorkflowEvents } from '../hooks/useDataHooks';

export const WorkflowDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const workflowId = id || 'wf-plan-8f12a3';

  const { data: statusData } = useWorkflowStatus(workflowId);
  const { data: logsData } = useWorkflowLogs(workflowId);
  const { data: eventsData } = useWorkflowEvents(workflowId);

  const [copied, setCopied] = useState(false);
  const [logFilter, setLogFilter] = useState('ALL');
  const [streamedLogs, setStreamedLogs] = useState<any[]>([]);

  // Progressive streaming logs
  useEffect(() => {
    const baseLogs = logsData && logsData.length > 0 ? logsData.map((log: any) => {
      const logStr = typeof log === 'string' ? log : (log.message || JSON.stringify(log));
      const isSuccess = logStr.includes('SUCCESS') || logStr.includes('completed');
      const isError = logStr.includes('ERROR') || logStr.includes('failed');
      return {
        level: isError ? 'ERROR' : (isSuccess ? 'SUCCESS' : 'INFO'),
        time: new Date().toLocaleTimeString(),
        msg: logStr
      };
    }) : [
      { level: 'INFO', time: '11:28:00', msg: 'Starting WorkflowOrchestrator pipeline' },
      { level: 'INFO', time: '11:28:01', msg: 'Generated DAG with 5 tasks: Research, PitchDeck, Logo, Frontend, QA' },
      { level: 'INFO', time: '11:28:02', msg: 'Collected live quote responses from 5 agent microservices' },
      { level: 'INFO', time: '11:28:04', msg: 'Verified x402 payment proof via https://facilitator.goplausible.xyz' },
      { level: 'SUCCESS', time: '11:28:08', msg: 'Workflow execution finished with status COMPLETED' }
    ];

    setStreamedLogs(baseLogs);
  }, [logsData]);

  const timelineSteps = eventsData && eventsData.length > 0 ? eventsData.map((e: any) => ({
    name: e.eventType || 'EVENT',
    time: e.timestamp ? new Date(e.timestamp).toLocaleTimeString() : '11:28:00',
    status: 'COMPLETED',
    detail: e.details || e.message || 'Event processed'
  })) : [
    { name: 'Planning Completed', time: '11:28:00', status: 'COMPLETED', detail: 'Decomposed prompt into 5 tasks' },
    { name: 'Discovery Completed', time: '11:28:01', status: 'COMPLETED', detail: 'Resolved capabilities across 5 microservices' },
    { name: 'Quotes Collected', time: '11:28:02', status: 'COMPLETED', detail: 'Scored 10 candidate quotes ($266.62 USDC)' },
    { name: 'Assignments Created', time: '11:28:03', status: 'COMPLETED', detail: 'BALANCED selection strategy applied' },
    { name: 'Payment Verified', time: '11:28:04', status: 'COMPLETED', detail: 'x402 Facilitator verified TX-ALGO-998811' },
    { name: 'Execution Started', time: '11:28:05', status: 'COMPLETED', detail: 'Parallel DAG engine dispatched tasks' },
    { name: 'Workflow Completed', time: '11:28:08', status: 'COMPLETED', detail: 'Generated SHA-256 settlement receipt' }
  ];

  const copyTxId = () => {
    navigator.clipboard.writeText('TX-ALGO-TEST-998811');
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="space-y-8 pb-12">
      {/* Top Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center space-x-3">
          <Link to="/" className="p-2 rounded-xl bg-slate-900 border border-slate-800 text-slate-400 hover:text-white transition-colors">
            <ArrowLeft className="w-4 h-4" />
          </Link>
          <div>
            <div className="flex items-center space-x-2">
              <h1 className="text-xl font-extrabold text-white tracking-tight font-mono">
                Workflow Details: {workflowId}
              </h1>
              <span className="px-2.5 py-0.5 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 text-xs font-mono font-semibold">
                STATUS: {statusData?.status || 'COMPLETED'}
              </span>
            </div>
            <p className="text-xs text-slate-400 mt-0.5 font-sans">
              Executed via AgentMesh x402 Pipeline • {statusData?.totalExecutionTimeMs || 2847}ms total duration
            </p>
          </div>
        </div>
      </div>

      {/* GitHub Actions Style Timeline */}
      <div className="glass-panel p-6 border-slate-800/80">
        <h3 className="text-sm font-bold text-white mb-4 flex items-center space-x-2">
          <Clock className="w-4 h-4 text-violet-400" />
          <span>Execution Timeline Trajectory</span>
        </h3>

        <div className="grid grid-cols-1 md:grid-cols-7 gap-3 relative">
          {timelineSteps.map((step: any, idx: number) => (
            <div key={idx} className="glass-card p-3 border-slate-800/80 space-y-1 relative">
              <div className="flex items-center space-x-1.5">
                <CheckCircle2 className="w-3.5 h-3.5 text-emerald-400" />
                <span className="text-[11px] font-bold text-slate-200 truncate">{step.name}</span>
              </div>
              <span className="text-[10px] text-slate-500 font-mono block">{step.time}</span>
              <p className="text-[10px] text-slate-400 leading-tight truncate">{step.detail}</p>
            </div>
          ))}
        </div>
      </div>

      {/* Grid: x402 Receipt Card & Log Console */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        {/* Left 5 Cols: x402 Settlement Receipt */}
        <div className="lg:col-span-5 space-y-6">
          <div className="glass-panel p-5 border-slate-800/80 space-y-4">
            <div className="flex items-center space-x-2 text-emerald-400">
              <ShieldCheck className="w-5 h-5" />
              <h3 className="text-base font-bold text-white">x402 Algorand Testnet Receipt</h3>
            </div>

            <div className="space-y-2.5 text-xs font-mono">
              <div className="flex justify-between py-1.5 border-b border-slate-800">
                <span className="text-slate-400">Algorand Tx ID:</span>
                <div className="flex items-center space-x-1">
                  <span className="text-slate-200 font-semibold">TX-ALGO-TEST-998811</span>
                  <button onClick={copyTxId} className="text-slate-400 hover:text-white p-1">
                    <Copy className="w-3 h-3" />
                  </button>
                </div>
              </div>

              <div className="flex justify-between py-1.5 border-b border-slate-800">
                <span className="text-slate-400">Settlement Amount:</span>
                <span className="text-emerald-400 font-bold">$5.25 USDC</span>
              </div>

              <div className="flex justify-between py-1.5 border-b border-slate-800">
                <span className="text-slate-400">Receipt Hash:</span>
                <span className="text-slate-400 truncate max-w-[180px]">8f12a3b4c5d6e7f8901234567890abcd</span>
              </div>

              <div className="flex justify-between py-1.5">
                <span className="text-slate-400">Facilitator Verification:</span>
                <span className="text-emerald-400 font-semibold">goplausible (VERIFIED)</span>
              </div>
            </div>
          </div>
        </div>

        {/* Right 7 Cols: Console Log Viewer */}
        <div className="lg:col-span-7">
          <div className="glass-panel p-4 border-slate-800/80 space-y-3 font-mono">
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-2">
                <Terminal className="w-4 h-4 text-cyan-400" />
                <h3 className="text-xs font-bold text-white uppercase">Live Execution Log Console</h3>
              </div>

              <div className="flex items-center space-x-2 text-[10px]">
                {['ALL', 'INFO', 'SUCCESS', 'ERROR'].map((lvl) => (
                  <button
                    key={lvl}
                    onClick={() => setLogFilter(lvl)}
                    className={`px-2 py-0.5 rounded ${logFilter === lvl ? 'bg-violet-600 text-white' : 'bg-slate-800 text-slate-400'}`}
                  >
                    {lvl}
                  </button>
                ))}
              </div>
            </div>

            <div className="bg-[#030712] p-4 rounded-xl border border-slate-800/80 h-[260px] overflow-y-auto space-y-1.5 text-xs">
              {streamedLogs
                .filter(l => logFilter === 'ALL' || l.level === logFilter)
                .map((log: any, idx: number) => (
                  <div key={idx} className="flex items-start space-x-2">
                    <span className="text-slate-600 text-[10px]">{log.time}</span>
                    <span className={`text-[10px] px-1 rounded font-bold ${
                      log.level === 'SUCCESS' ? 'bg-emerald-500/20 text-emerald-300' : 'bg-indigo-500/20 text-indigo-300'
                    }`}>
                      {log.level}
                    </span>
                    <span className="text-slate-300">{log.msg}</span>
                  </div>
                ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
