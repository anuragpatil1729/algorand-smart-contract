import sys
from pathlib import Path

def pytest_pycollect_makemodule(module_path, parent):
    mod_path = Path(module_path).resolve()
    for p in mod_path.parents:
        if (p / "agent.py").exists():
            p_str = str(p)
            if p_str in sys.path:
                sys.path.remove(p_str)
            sys.path.insert(0, p_str)
            for mod in ["agent", "config", "base_agent"]:
                if mod in sys.modules:
                    del sys.modules[mod]
            break

def pytest_runtest_setup(item):
    test_path = Path(item.fspath).resolve()
    for p in test_path.parents:
        if (p / "agent.py").exists():
            p_str = str(p)
            if p_str in sys.path:
                sys.path.remove(p_str)
            sys.path.insert(0, p_str)
            for mod in ["agent", "config", "base_agent"]:
                if mod in sys.modules:
                    del sys.modules[mod]
            break
