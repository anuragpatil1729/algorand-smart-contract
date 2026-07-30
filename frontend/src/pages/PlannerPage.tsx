import React, { useState } from 'react';
import ReactFlow, { 
  Background, 
  Controls, 
  MiniMap, 
  useNodesState, 
  useEdgesState
} from 'reactflow';
import 'reactflow/dist/style.css';
import { 
  Sparkles, 
  Play, 
  Sliders, 
  Layers, 
  RefreshCw 
} from 'lucide-react';
import { TaskNode } from '../components/TaskNode';
import { useRunPipelineMutation } from '../hooks/useDataHooks';

const nodeTypes = {
  taskNode: TaskNode,
};

export const PlannerPage: React.FC = () => {
  const [prompt, setPrompt] = useState('Create a startup landing page with logo, presentation deck, REST APIs, and automated QA');
  const [strategy, setStrategy] = useState('BALANCED');
  const [maxConcurrency, setMaxConcurrency] = useState(5);
  const [executionResult, setExecutionResult] = useState<any>(null);

  const runPipelineMutation = useRunPipelineMutation();

  const [nodes, setNodes, onNodesChange] = useNodesState([
    { id: 't1', type: 'taskNode', position: { x: 250, y: 30 }, data: { label: 'User & Domain Research', capability: 'RESEARCH', agentName: 'Research Agent', status: 'COMPLETED', price: 45.0 } },
    { id: 't2', type: 'taskNode', position: { x: 50, y: 180 }, data: { label: 'Pitch Deck & Architecture', capability: 'PITCH_DECK', agentName: 'PPT Agent', status: 'COMPLETED', price: 60.0 } },
    { id: 't3', type: 'taskNode', position: { x: 450, y: 180 }, data: { label: 'Brand Logo Design', capability: 'LOGO_DESIGN', agentName: 'Image Agent', status: 'COMPLETED', price: 50.0 } },
    { id: 't4', type: 'taskNode', position: { x: 250, y: 330 }, data: { label: 'React UI Code Generation', capability: 'FRONTEND', agentName: 'Coding Agent', status: 'RUNNING', price: 80.0 } },
    { id: 't5', type: 'taskNode', position: { x: 250, y: 480 }, data: { label: 'Automated QA Audit', capability: 'TESTING', agentName: 'Testing Agent', status: 'PENDING', price: 30.0 } }
  ] as any);

  const [edges, setEdges, onEdgesChange] = useEdgesState([
    { id: 'e1-2', source: 't1', target: 't2', animated: true, style: { stroke: '#8b5cf6', strokeWidth: 2 } },
    { id: 'e1-3', source: 't1', target: 't3', animated: true, style: { stroke: '#8b5cf6', strokeWidth: 2 } },
    { id: 'e2-4', source: 't2', target: 't4', animated: true, style: { stroke: '#6366f1', strokeWidth: 2 } },
    { id: 'e3-4', source: 't3', target: 't4', animated: true, style: { stroke: '#6366f1', strokeWidth: 2 } },
    { id: 'e4-5', source: 't4', target: 't5', animated: true, style: { stroke: '#10b981', strokeWidth: 2 } }
  ] as any);

  const presets = [
    'Create a startup landing page with logo, presentation deck & QA',
    'Build a Python FastAPI microservice with research benchmarks',
    'Full-stack React dashboard with authentication and security audit'
  ];

  const handleRunPipeline = () => {
    runPipelineMutation.mutate(
      { prompt, strategy, maxConcurrency },
      {
        onSuccess: (data) => {
          setExecutionResult(data);

          if (data?.plannerOutput?.taskList) {
            const dynamicNodes = data.plannerOutput.taskList.map((task: any, idx: number) => ({
              id: task.id || `task-${idx}`,
              type: 'taskNode',
              position: { x: 200 + (idx % 2) * 200, y: 50 + Math.floor(idx / 2) * 150 },
              data: {
                label: task.description || task.name || task.id,
                capability: task.requiredCapability || 'GENERAL',
                agentName: data.selectedAgents?.find((a: any) => a.taskId === task.id)?.selectedAgentName || 'Assigned Agent',
                status: 'COMPLETED',
                price: task.estimatedCost || 50.0
              }
            }));

            const dynamicEdges: any[] = [];
            data.plannerOutput.taskList.forEach((task: any) => {
              if (task.dependencies) {
                task.dependencies.forEach((depId: string) => {
                  dynamicEdges.push({
                    id: `e-${depId}-${task.id}`,
                    source: depId,
                    target: task.id,
                    animated: true,
                    style: { stroke: '#8b5cf6', strokeWidth: 2 }
                  });
                });
              }
            });

            if (dynamicNodes.length > 0) setNodes(dynamicNodes);
            if (dynamicEdges.length > 0) setEdges(dynamicEdges);
          }
        }
      }
    );
  };

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
              DYNAMIC PIPELINE
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

            {/* Prompt Input */}
            <div>
              <textarea
                value={prompt}
                onChange={(e) => setPrompt(e.target.value)}
                rows={4}
                className="w-full glass-input p-3.5 text-sm font-sans leading-relaxed resize-none"
                placeholder="Describe your workflow goals..."
              />
            </div>

            {/* Preset Buttons */}
            <div>
              <span className="text-[11px] font-mono text-slate-400 block mb-2">Preset Prompts:</span>
              <div className="space-y-1.5">
                {presets.map((preset, idx) => (
                  <button
                    key={idx}
                    onClick={() => setPrompt(preset)}
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
              disabled={runPipelineMutation.isPending}
              className="w-full glass-button py-3.5 flex items-center justify-center space-x-2 text-sm font-bold tracking-wide shadow-violet-600/40"
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

          {/* Estimation Metrics Card */}
          <div className="glass-panel p-4 border-slate-800/80 grid grid-cols-2 gap-3 text-center font-mono">
            <div className="glass-card p-3 border-slate-800">
              <span className="text-[10px] text-slate-400 uppercase block">Total Quoted Cost</span>
              <span className="text-lg font-bold text-emerald-400">$265.00 USDC</span>
            </div>
            <div className="glass-card p-3 border-slate-800">
              <span className="text-[10px] text-slate-400 uppercase block">Critical Path ETA</span>
              <span className="text-lg font-bold text-amber-400">70 Seconds</span>
            </div>
          </div>
        </div>

        {/* Right 7 Cols: React Flow Canvas */}
        <div className="lg:col-span-7">
          <div className="glass-panel p-4 border-slate-800/80 h-[580px] flex flex-col">
            <div className="flex items-center justify-between mb-3 px-2">
              <div className="flex items-center space-x-2">
                <Layers className="w-5 h-5 text-indigo-400" />
                <h3 className="text-sm font-bold text-white">Live Execution DAG Canvas</h3>
              </div>
              <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-violet-500/10 text-violet-300 border border-violet-500/20">
                DYNAMIC CANVAS
              </span>
            </div>

            <div className="flex-1 w-full h-full rounded-xl overflow-hidden border border-slate-800 bg-[#040711]">
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
                <MiniMap className="!bg-slate-950 !border-slate-800" nodeColor="#6366f1" />
              </ReactFlow>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
