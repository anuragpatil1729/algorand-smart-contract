#!/usr/bin/env bash
# AgentMesh Database Seeding Script

echo "=========================================="
echo "Seeding AgentMesh Database & AI Agents..."
echo "=========================================="

if command -v psql &> /dev/null; then
    psql -U postgres -d agentmeshdb -f database/schema/schema.sql
    psql -U postgres -d agentmeshdb -f database/seeds/agents_seed.sql
    echo "PostgreSQL database seeded successfully!"
else
    echo "psql not found locally, seeding will execute via Spring Boot embedded H2 / Docker init scripts."
fi
