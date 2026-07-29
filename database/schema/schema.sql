-- AgentMesh PostgreSQL Schema
-- Database tables for AI Multi-Agent Service Router with Algorand Atomic Payments

CREATE TABLE IF NOT EXISTS agents (
    id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    endpoint VARCHAR(255) NOT NULL,
    wallet_address VARCHAR(255) NOT NULL,
    rating DOUBLE PRECISION DEFAULT 4.5,
    success_rate DOUBLE PRECISION DEFAULT 95.0,
    health_status VARCHAR(50) DEFAULT 'UP',
    base_price DOUBLE PRECISION DEFAULT 50.0,
    supported_capabilities TEXT NOT NULL, -- Comma-separated list of capabilities
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS workflows (
    id VARCHAR(64) PRIMARY KEY,
    prompt TEXT NOT NULL,
    status VARCHAR(50) NOT NULL, -- PENDING_APPROVAL, APPROVED, RUNNING, COMPLETED, FAILED, CANCELLED
    total_price DOUBLE PRECISION DEFAULT 0.0,
    escrow_address VARCHAR(255),
    escrow_status VARCHAR(50) DEFAULT 'NOT_CREATED', -- NOT_CREATED, LOCKED, RELEASED, REFUNDED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tasks (
    id VARCHAR(64) PRIMARY KEY,
    workflow_id VARCHAR(64) NOT NULL REFERENCES workflows(id) ON DELETE CASCADE,
    task_type VARCHAR(100) NOT NULL,
    description TEXT,
    assigned_agent VARCHAR(64) REFERENCES agents(id),
    status VARCHAR(50) NOT NULL, -- PENDING, RUNNING, COMPLETED, FAILED, CANCELLED
    price DOUBLE PRECISION DEFAULT 0.0,
    dependencies TEXT, -- Comma-separated list of task IDs
    priority INT DEFAULT 1,
    estimated_complexity VARCHAR(50) DEFAULT 'MEDIUM',
    execution_time_ms BIGINT DEFAULT 0,
    output TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS quotes (
    id VARCHAR(64) PRIMARY KEY,
    task_id VARCHAR(64) NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    agent_id VARCHAR(64) NOT NULL REFERENCES agents(id),
    price DOUBLE PRECISION NOT NULL,
    estimated_time_seconds INT NOT NULL,
    confidence DOUBLE PRECISION NOT NULL,
    success_rate DOUBLE PRECISION NOT NULL,
    rating DOUBLE PRECISION NOT NULL,
    score DOUBLE PRECISION NOT NULL,
    selected BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS payments (
    id VARCHAR(64) PRIMARY KEY,
    workflow_id VARCHAR(64) NOT NULL REFERENCES workflows(id) ON DELETE CASCADE,
    escrow_wallet VARCHAR(255) NOT NULL,
    total_amount DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL, -- HELD_IN_ESCROW, DISBURSED, REFUNDED
    tx_group_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(64) PRIMARY KEY,
    payment_id VARCHAR(64) NOT NULL REFERENCES payments(id) ON DELETE CASCADE,
    tx_hash VARCHAR(255) NOT NULL,
    sender_wallet VARCHAR(255) NOT NULL,
    receiver_wallet VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    agent_id VARCHAR(64),
    status VARCHAR(50) NOT NULL, -- SUCCESS, FAILED, PENDING
    block_round BIGINT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS execution_logs (
    id VARCHAR(64) PRIMARY KEY,
    workflow_id VARCHAR(64) NOT NULL,
    task_id VARCHAR(64),
    agent_id VARCHAR(64),
    log_level VARCHAR(20) DEFAULT 'INFO',
    message TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS scoring_config (
    id VARCHAR(64) PRIMARY KEY,
    reputation_weight DOUBLE PRECISION DEFAULT 0.35,
    success_rate_weight DOUBLE PRECISION DEFAULT 0.25,
    confidence_weight DOUBLE PRECISION DEFAULT 0.20,
    price_weight DOUBLE PRECISION DEFAULT 0.10,
    eta_weight DOUBLE PRECISION DEFAULT 0.10,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default scoring configuration
INSERT INTO scoring_config (id, reputation_weight, success_rate_weight, confidence_weight, price_weight, eta_weight)
VALUES ('DEFAULT', 0.35, 0.25, 0.20, 0.10, 0.10)
ON CONFLICT (id) DO NOTHING;
