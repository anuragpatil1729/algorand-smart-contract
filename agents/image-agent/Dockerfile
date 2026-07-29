FROM python:3.12-slim

WORKDIR /app

COPY shared /app/agents/shared
COPY image-agent/requirements.txt /app/requirements.txt

RUN pip install --no-cache-dir -r requirements.txt

COPY image-agent /app/agents/image-agent

ENV PYTHONPATH=/app
ENV PORT=8003

EXPOSE 8003

CMD ["python", "agents/image-agent/main.py"]
