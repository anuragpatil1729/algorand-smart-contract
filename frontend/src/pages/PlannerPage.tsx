import React, { useState } from 'react';
import ReactFlow, { 
  Background, 
  Controls, 
  useNodesState, 
  useEdgesState
} from 'reactflow';
import 'reactflow/dist/style.css';
import { 
  Sparkles, 
  Play, 
  Sliders, 
  Layers, 
  RefreshCw,
  DollarSign,
  Activity
} from 'lucide-react';
import { TaskNode } from '../components/TaskNode';
import { useRunPipelineMutation } from '../hooks/useDataHooks';

const nodeTypes = {
  taskNode: TaskNode,
};

export const PlannerPage: React.FC = () => {
  const [prompt, setPrompt] = useState('');
  const [strategy, setStrategy] = useState('BALANCED');
  const [maxConcurrency, setMaxConcurrency] = useState(5);
  
  const [executionResult, setExecutionResult] = useState<any>(null);

  const runPipelineMutation = useRunPipelineMutation();

  // Pure Backend Event-Driven Graph State: Initially empty []
  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);

  const presets = [
    'Create a startup landing page with logo, presentation deck & QA',
    'Build a Python FastAPI microservice with research benchmarks',
    'Full-stack React dashboard with authentication and security audit'
  ];

  const handleRunPipeline = () => {
    if (!prompt.trim()) return;

    // Reset canvas on execution start
    setNodes([]);
    setEdges([]);
    setExecutionResult(null);

    runPipelineMutation.mutate(
      { prompt, strategy, maxConcurrency },
      {
        onSuccess: (data) => {
          setExecutionResult(data);

          const taskList = data?.plannerOutput?.taskList || [];
          const assignments = data?.selectedAgents || [];

          // Render nodes directly from backend response without fallback defaults
          if (taskList.length > 0) {
            const dynamicNodes = taskList.map((task: any, idx: number) => {
              const assign = assignments.find((a: any) => a.taskId === task.id);
              return {
                id: task.id || `task-${idx}`,
                type: 'taskNode',
                position: { x: 150 + (idx % 2) * 220, y: 40 + Math.floor(idx / 2) * 160 },
                data: {
                  label: task.description || task.name || task.id,
                  capability: task.requiredCapability,
                  agentName: assign?.selectedAgentName || assign?.selectedAgentId,
                  status: data?.result?.status === 'COMPLETED' ? 'VERIFIED' : 'RUNNING',
                  price: assign?.quotedPrice || task.estimatedCost,
                  confidenceScore: assign?.confidenceScore
                }
              };
            });

            const dynamicEdges: any[] = [];
            taskList.forEach((task: any) => {
              if (task.dependencies) {
                task.dependencies.forEach((depId: string) => {
                  dynamicEdges.push({
                    id: `e-${depId}-${task.id}`,
                    source: depId,
                    target: task.id,
                    animated: true,
                    style: { stroke: '#10b981', strokeWidth: 2 }
                  });
                });
              }
            });

            setNodes(dynamicNodes);
            setEdges(dynamicEdges);
          }
        }
      }
    );
  };

  const selectedQuotes = executionResult?.selectedAgents || [];

  return (
    <div className="space-y-6 pb-12">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center space-x-2">
            <h1 className="text-2xl font-extrabold text-white tracking-tight">
              Workflow Builder & Live DAG
            </h1>
            <span className="px-2.5 py-0.5 rounded-full bg-gradient-to-r from-violet-500 to-indigo-500 text-white font-mono text-xs font-bold shadow-md shadow-violet-500/20">
              PURE BACKEND DATA
            </span>
          </div>
          <p className="text-sm text-slate-400 mt-1 font-sans">
            AI-driven prompt decomposition, multi-criteria quote scoring, and automated x402 DAG execution
          </p>
        </div>
      </div>

      {/* Main Split Layout */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
        {/* Left 5 Cols: Input & Configuration Controls */}
        <div className="lg:col-span-5 space-y-6">
          <div className="glass-panel p-5 border-slate-800/80 space-y-5">
            <div className="flex items-center space-x-2">
              <Sparkles className="w-5 h-5 text-cyan-400" />
              <h2 className="text-base font-bold text-white">Natural Language Prompt</h2>
            </div>

            {/* Prompt Input - Initialized Empty */}
            <div>
              <textarea
                value={prompt}
                onChange={(e) => {
                  setPrompt(e.target.value);
                  if (executionResult) {
                    setExecutionResult(null);
                    setNodes([]);
                    setEdges([]);
                  }
                }}
                rows={4}
                className="w-full glass-input p-3.5 text-sm font-sans leading-relaxed resize-none"
                placeholder="Describe the workflow you want to execute..."
              />
            </div>

            {/* Preset Buttons */}
            <div>
              <span className="text-[11px] font-mono text-slate-400 block mb-2">Preset Prompts:</span>
              <div className="space-y-1.5">
                {presets.map((preset, idx) => (
                  <button
                    key={idx}
                    onClick={() => {
                      setPrompt(preset);
                      setExecutionResult(null);
                      setNodes([]);
                      setEdges([]);
                    }}
                    className="w-full text-left text-xs font-sans p-2 rounded-xl bg-slate-950/40 hover:bg-slate-800/60 border border-slate-800/60 text-slate-300 hover:text-white transition-all truncate"
                  >
                    💡 {preset}
                  </button>
                ))}
              </div>
            </div>

            {/* Selection Strategy */}
            <div className="space-y-2">
              <label className="text-xs font-mono font-medium text-slate-300 flex items-center space-x-1.5">
                <Sliders className="w-3.5 h-3.5 text-violet-400" />
                <span>Agent Selection Strategy</span>
              </label>
              <select
                value={strategy}
                onChange={(e) => setStrategy(e.target.value)}
                className="w-full glass-input p-2.5 text-xs font-mono bg-slate-950 text-slate-200"
              >
                <option value="BALANCED">BALANCED (35% Rep, 20% Health, 15% Conf, 10% ETA, 10% Load, 10% Price)</option>
                <option value="LOWEST_PRICE">LOWEST_PRICE (Prioritize minimum USDC cost)</option>
                <option value="FASTEST_COMPLETION">FASTEST_COMPLETION (Prioritize lowest duration ETA)</option>
                <option value="HIGHEST_QUALITY">HIGHEST_QUALITY (Prioritize agent reputation & rating)</option>
              </select>
            </div>

            {/* Execute Button */}
            <button
              onClick={handleRunPipeline}
              disabled={runPipelineMutation.isPending || !prompt.trim()}
              className="w-full glass-button py-3.5 flex items-center justify-center space-x-2 text-sm font-bold tracking-wide shadow-violet-600/40 disabled:opacity-50"
            >
              {runPipelineMutation.isPending ? (
                <>
                  <RefreshCw className="w-4 h-4 text-white animate-spin" />
                  <span>Orchestrating Pipeline...</span>
                </>
              ) : (
                <>
                  <Play className="w-4 h-4 text-cyan-300 fill-cyan-300" />
                  <span>Execute Full x402 Pipeline</span>
                </>
              )}
            </button>
          </div>

          {/* Live Quote Stream Box from Backend */}
          {selectedQuotes.length > 0 && (
            <div className="glass-panel p-4 border-slate-800/80 space-y-2">
              <h3 className="text-xs font-bold text-white font-mono flex items-center space-x-2">
                <DollarSign className="w-4 h-4 text-emerald-400" />
                <span>Backend Agent Quotes & Assignments</span>
              </h3>
              <div className="space-y-1.5 font-mono text-xs">
                {selectedQuotes.map((q: any, idx: number) => (
                  <div key={idx} className="glass-card p-2 border-slate-800 flex items-center justify-between">
                    <span className="text-slate-300 text-[11px]">{q.selectedAgentName || q.selectedAgentId}</span>
                    <span className="text-emerald-400 font-bold text-[11px]">
                      {q.quotedPrice ? `$${q.quotedPrice.toFixed(2)} USDC` : ''}
                    </span>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Estimation Metrics Card */}
          {executionResult && (
            <div className="glass-panel p-4 border-slate-800/80 grid grid-cols-2 gap-3 text-center font-mono">
              <div className="glass-card p-3 border-slate-800">
                <span className="text-[10px] text-slate-400 uppercase block">Total Quoted Cost</span>
                <span className="text-lg font-bold text-emerald-400">
                  {executionResult.plannerOutput?.totalEstimatedCost
                    ? `$${executionResult.plannerOutput.totalEstimatedCost.toFixed(2)} USDC`
                    : 'N/A'}
                </span>
              </div>
              <div className="glass-card p-3 border-slate-800">
                <span className="text-[10px] text-slate-400 uppercase block">Execution Duration</span>
                <span className="text-lg font-bold text-amber-400">
                  {executionResult.executionTimeMs ? `${executionResult.executionTimeMs}ms` : 'N/A'}
                </span>
              </div>
            </div>
          )}
        </div>

        {/* Right 7 Cols: React Flow Canvas */}
        <div className="lg:col-span-7">
          <div className="glass-panel p-4 border-slate-800/80 h-[580px] flex flex-col">
            <div className="flex items-center justify-between mb-3 px-2">
              <div className="flex items-center space-x-2">
                <Layers className="w-5 h-5 text-indigo-400" />
                <h3 className="text-sm font-bold text-white">Execution DAG Canvas</h3>
              </div>
              <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-violet-500/10 text-violet-300 border border-violet-500/20">
                {nodes.length > 0 ? `${nodes.length} BACKEND TASKS` : 'CANVAS EMPTY'}
              </span>
            </div>

            <div className="flex-1 w-full h-full rounded-xl overflow-hidden border border-slate-800 bg-[#040711] relative">
              {nodes.length === 0 ? (
                <div className="absolute inset-0 flex flex-col items-center justify-center text-center p-6 space-y-3 z-10 bg-[#040711]">
                  <Activity className="w-10 h-10 text-slate-600 animate-pulse" />
                  <h4 className="text-sm font-bold text-slate-300 font-mono">No Workflow Generated Yet</h4>
                  <p className="text-xs text-slate-500 max-w-sm font-sans">
                    Submit a prompt on the left to execute the pipeline and render the backend DAG graph.
                  </p>
                </div>
              ) : (
                <ReactFlow
                  nodes={nodes}
                  edges={edges}
                  onNodesChange={onNodesChange}
                  onEdgesChange={onEdgesChange}
                  nodeTypes={nodeTypes}
                  fitView
                >
                  <Background color="#1e293b" gap={20} size={1} />
                  <Controls className="!bg-slate-900 !border-slate-800 !text-slate-300" />
                </ReactFlow>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
