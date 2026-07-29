FROM python:3.12-slim

WORKDIR /app

# Copy shared library requirements and install
COPY shared /app/agents/shared
COPY research-agent/requirements.txt /app/requirements.txt

RUN pip install --no-cache-dir -r requirements.txt

# Copy research agent source code
COPY research-agent /app/agents/research-agent

ENV PYTHONPATH=/app
ENV PORT=8001

EXPOSE 8001

CMD ["python", "agents/research-agent/main.py"]
