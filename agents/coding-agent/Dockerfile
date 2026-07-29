FROM python:3.12-slim

WORKDIR /app

COPY shared /app/agents/shared
COPY coding-agent/requirements.txt /app/requirements.txt

RUN pip install --no-cache-dir -r requirements.txt

COPY coding-agent /app/agents/coding-agent

ENV PYTHONPATH=/app
ENV PORT=8002

EXPOSE 8002

CMD ["python", "agents/coding-agent/main.py"]
