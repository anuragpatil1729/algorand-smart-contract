import pytest
import asyncio
from agents.shared.models.schemas import AgentInfo
from agents.shared.config.settings import AgentConfig
from agents.shared.services.task_manager import TaskManager
from agents.shared.services.health_monitor import HealthMonitor
from agents.shared.services.registry_client import RegistryClient

def test_registry_client_standalone_fallback():
    async def _test():
        info = AgentInfo(agentId="agent-test-reg", name="Test Reg Agent", port=8999, capabilities=["TEST"])
        config = AgentConfig()
        tm = TaskManager()
        hm = HealthMonitor(info, tm)
        client = RegistryClient(info, config, hm)

        registered = await client.register()
        assert isinstance(registered, bool)

        heartbeat_sent = await client.send_heartbeat()
        assert isinstance(heartbeat_sent, bool)

        deregistered = await client.deregister()
        assert isinstance(deregistered, bool)

    asyncio.run(_test())
