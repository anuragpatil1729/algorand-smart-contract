import sys
import os

current_dir = os.path.dirname(os.path.abspath(__file__))
testing_agent_dir = os.path.abspath(os.path.join(current_dir, ".."))
agents_dir = os.path.abspath(os.path.join(testing_agent_dir, ".."))
workspace_dir = os.path.abspath(os.path.join(agents_dir, ".."))

for path in (testing_agent_dir, agents_dir, workspace_dir):
    if path not in sys.path:
        sys.path.insert(0, path)
