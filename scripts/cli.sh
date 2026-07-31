#!/usr/bin/env bash
# AgentMesh CLI Mode Launcher

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
echo "Starting AgentMesh CLI Interface..."
cd "$ROOT_DIR/frontend" && npm run cli
