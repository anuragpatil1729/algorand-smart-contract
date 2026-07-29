#!/usr/bin/env bash
# AgentMesh Local Launch Script

echo "=========================================="
echo "Starting AgentMesh Multi-Agent Ecosystem"
echo "=========================================="

echo "[1/3] Launching Docker Compose stack..."
docker-compose up -d --build

echo "[2/3] Verifying agent endpoints..."
curl -s http://localhost:8001/health || true
curl -s http://localhost:8002/health || true
curl -s http://localhost:8003/health || true
curl -s http://localhost:8004/health || true
curl -s http://localhost:8005/health || true

echo "=========================================="
echo "AgentMesh Platform Ready!"
echo "Router API: http://localhost:8080"
echo "Frontend UI: http://localhost:3000"
echo "=========================================="
