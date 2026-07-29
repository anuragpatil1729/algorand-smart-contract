import os
import sys

current_dir = os.path.dirname(os.path.abspath(__file__))
agents_dir = os.path.abspath(os.path.join(current_dir, ".."))
workspace_dir = os.path.abspath(os.path.join(agents_dir, ".."))
for path in (current_dir, agents_dir, workspace_dir):
    if path not in sys.path:
        sys.path.insert(0, path)

from agent import TestingAgent

agent = TestingAgent()
app = agent.app

if __name__ == "__main__":
    agent.run()
