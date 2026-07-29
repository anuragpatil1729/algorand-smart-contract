import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api, WorkflowResponse } from '../services/api';
import { Sparkles, ArrowRight, ShieldCheck, Cpu, Star, DollarSign, Layers, CheckCircle } from 'lucide-react';

export const PlannerPage: React.FC = () => {
  const navigate = useNavigate();
  const [prompt, setPrompt] = useState('');
  const [loading, setLoading] = useState(false);
  const [workflow, setWorkflow] = useState<WorkflowResponse | null>(null);

  const presets = [
    { title: 'Startup Landing Page', prompt: 'Create a modern startup landing page with logo, React components, and QA tests', icon: '🚀' },
    { title: 'Pitch Deck Strategy', prompt: 'Create a startup pitch deck with market research, slide structure, and brand visuals', icon: '📊' },
    { title: 'Competitor Intelligence', prompt: 'Research competitors in Web3 agent routing and produce market synthesis', icon: '🔎' },
    { title: 'Brand Logo & Identity', prompt: 'Generate logo design, color palette, and vector SVG visual assets', icon: '🎨' },
  ];

  const handleCreatePlan = async (inputPrompt?: string) => {
    const targetPrompt = inputPrompt || prompt;
    if (!targetPrompt.trim()) return;

    setLoading(true);
    try {
      const res = await api.createWorkflow(targetPrompt);
      setWorkflow(res);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleApproveAndExecute = async () => {
    if (!workflow) return;
    setLoading(true);
    try {
      await api.approveWorkflow(workflow.id);
      await api.executeWorkflow(workflow.id);
      navigate(`/workflows?id=${workflow.id}`);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 space-y-8 max-w-6xl mx-auto">
      
      {/* Header */}
      <div className="space-y-2">
        <h1 className="text-3xl font-black text-white flex items-center gap-3">
          <Sparkles className="w-8 h-8 text-cyan-400 animate-pulse" />
          Intelligent Workflow Planner
        </h1>
        <p className="text-sm text-slate-400">
          Enter a high-level natural language request. The Planner will decompose it into a task graph, query available microservices, score quotations, and set up an Algorand Escrow contract.
        </p>
      </div>

      {/* Preset Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {presets.map((preset, idx) => (
          <div
            key={idx}
            onClick={() => { setPrompt(preset.prompt); handleCreatePlan(preset.prompt); }}
            className="p-4 rounded-2xl glass-card border border-slate-800 cursor-pointer hover:border-cyan-500/50 space-y-2 group transition-all"
          >
            <div className="text-2xl">{preset.icon}</div>
            <h4 className="text-xs font-bold text-slate-200 group-hover:text-cyan-400 transition-colors">{preset.title}</h4>
            <p className="text-[11px] text-slate-400 line-clamp-2">{preset.prompt}</p>
          </div>
        ))}
      </div>

      {/* Prompt Input Box */}
      <div className="p-6 rounded-3xl glass-panel space-y-4 border border-slate-800">
        <label className="text-xs font-bold uppercase tracking-wider text-slate-300 block">
          Enter Request Prompt
        </label>
        <div className="flex flex-col sm:flex-row gap-3">
          <input
            type="text"
            value={prompt}
            onChange={(e) => setPrompt(e.target.value)}
            placeholder="e.g. Create a startup landing page with logo, React components, and QA tests..."
            className="flex-1 rounded-2xl bg-slate-950 border border-slate-800 px-5 py-3.5 text-sm text-white focus:outline-none focus:border-cyan-500 transition-colors font-medium"
          />
          <button
            onClick={() => handleCreatePlan()}
            disabled={loading || !prompt.trim()}
            className="px-6 py-3.5 rounded-2xl bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 text-white font-bold text-xs uppercase tracking-wider flex items-center justify-center space-x-2 transition-all disabled:opacity-50 shadow-lg shadow-cyan-500/20"
          >
            <span>{loading ? 'Decomposing...' : 'Generate Plan & Quotes'}</span>
            <ArrowRight className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Generated Task DAG Breakdown */}
      {workflow && (
        <div className="space-y-6 animate-in fade-in duration-300">
          
          <div className="flex items-center justify-between p-4 rounded-2xl bg-slate-900 border border-slate-800">
            <div>
              <span className="text-[10px] uppercase tracking-wider font-bold text-slate-500 block">Generated Plan ID</span>
              <span className="font-mono font-bold text-cyan-400 text-sm">{workflow.id}</span>
            </div>
            <div className="text-right">
              <span className="text-[10px] uppercase tracking-wider font-bold text-slate-500 block">Estimated Escrow Cost</span>
              <span className="font-mono font-extrabold text-emerald-400 text-lg">{workflow.totalPrice} ALGO</span>
            </div>
          </div>

          {/* Task Decomposition List */}
          <div className="space-y-4">
            <h3 className="text-sm font-bold text-slate-200 flex items-center gap-2">
              <Layers className="w-4 h-4 text-cyan-400" />
              Decomposed Task Graph & Quotation Scoring
            </h3>

            <div className="space-y-3">
              {workflow.tasks.map((task, idx) => {
                const winnerQuote = task.quotes?.find(q => q.selected) || task.quotes?.[0];
                return (
                  <div key={task.id} className="p-5 rounded-2xl glass-panel border border-slate-800 space-y-4">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center space-x-3">
                        <span className="flex h-7 w-7 items-center justify-center rounded-xl bg-cyan-500/10 text-cyan-400 font-bold text-xs border border-cyan-500/30">
                          {idx + 1}
                        </span>
                        <div>
                          <h4 className="text-sm font-bold text-slate-100">{task.description}</h4>
                          <span className="text-[10px] font-semibold text-slate-500 uppercase tracking-wider">
                            Type: {task.taskType} | Dependencies: {task.dependencies?.length ? task.dependencies.join(', ') : 'None'}
                          </span>
                        </div>
                      </div>
                      <span className="px-3 py-1 rounded-full text-xs font-mono font-bold text-emerald-400 bg-emerald-500/10 border border-emerald-500/30">
                        {winnerQuote ? `${winnerQuote.price} ALGO` : 'Pending'}
                      </span>
                    </div>

                    {/* Quotation Candidates Comparison */}
                    {task.quotes && task.quotes.length > 0 && (
                      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-3 pt-2">
                        {task.quotes.map((q) => (
                          <div
                            key={q.id}
                            className={`p-3 rounded-xl border text-xs space-y-1.5 transition-all ${
                              q.selected
                                ? 'bg-cyan-950/40 border-cyan-500/60 shadow-lg shadow-cyan-500/10'
                                : 'bg-slate-900/60 border-slate-800 opacity-70'
                            }`}
                          >
                            <div className="flex items-center justify-between font-bold">
                              <span className="text-slate-200 truncate">{q.agentName}</span>
                              {q.selected && (
                                <span className="flex items-center gap-1 text-[9px] bg-cyan-500/20 text-cyan-400 px-1.5 py-0.5 rounded-full border border-cyan-500/40">
                                  <CheckCircle className="w-2.5 h-2.5" /> Selected
                                </span>
                              )}
                            </div>
                            <div className="grid grid-cols-2 gap-1 text-[10px] text-slate-400">
                              <span>Price: <strong className="text-emerald-400">{q.price} ALGO</strong></span>
                              <span>ETA: <strong className="text-slate-200">{q.estimatedTimeSeconds}s</strong></span>
                              <span>Rating: <strong className="text-amber-400">{q.rating}★</strong></span>
                              <span>Score: <strong className="text-cyan-400">{q.score}</strong></span>
                            </div>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                );
              })}
            </div>
          </div>

          {/* Action Trigger */}
          <div className="p-6 rounded-3xl bg-gradient-to-r from-cyan-950/50 via-slate-900 to-indigo-950/50 border border-cyan-500/40 flex flex-col sm:flex-row items-center justify-between gap-4 shadow-2xl">
            <div className="space-y-1">
              <h4 className="text-sm font-bold text-white flex items-center gap-2">
                <ShieldCheck className="w-5 h-5 text-cyan-400" />
                Approve & Lock Funds in Algorand Escrow
              </h4>
              <p className="text-xs text-slate-400">
                {workflow.totalPrice} ALGO will be locked in PyTeal Escrow contract <code className="text-cyan-400 font-mono">AGENTMESH_ESCROW_CONTRACT_7X9V</code>.
              </p>
            </div>
            <button
              onClick={handleApproveAndExecute}
              disabled={loading}
              className="w-full sm:w-auto px-8 py-4 rounded-2xl bg-gradient-to-r from-emerald-500 to-cyan-500 hover:from-emerald-400 hover:to-cyan-400 text-slate-950 font-black text-xs uppercase tracking-wider shadow-xl shadow-emerald-500/20 transition-all transform active:scale-95 shrink-0"
            >
              {loading ? 'Initializing Algorand Escrow...' : 'Approve & Execute Workflow'}
            </button>
          </div>

        </div>
      )}

    </div>
  );
};
