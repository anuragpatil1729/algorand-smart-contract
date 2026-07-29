import os
import tempfile
import pytest
from agents.shared.config.settings import load_config

def test_load_yaml_config():
    yaml_content = """
agent:
  id: test-agent-99
  name: Custom Test Agent
  port: 9090
  version: 2.0.0
  capabilities:
    - TESTING
  taskTypes:
    - Unit Testing
"""
    with tempfile.NamedTemporaryFile("w+", suffix=".yaml", delete=False) as tmp:
        tmp.write(yaml_content)
        tmp_path = tmp.name

    try:
        config = load_config(tmp_path)
        assert config.agent.id == "test-agent-99"
        assert config.agent.name == "Custom Test Agent"
        assert config.agent.port == 9090
        assert config.agent.version == "2.0.0"
        assert "TESTING" in config.agent.capabilities
    finally:
        if os.path.exists(tmp_path):
            os.remove(tmp_path)

def test_env_var_overrides(monkeypatch):
    monkeypatch.setenv("AGENT_NAME", "Env Override Agent")
    monkeypatch.setenv("AGENT_PORT", "9999")
    config = load_config()
    assert config.agent.name == "Env Override Agent"
    assert config.agent.port == 9999
