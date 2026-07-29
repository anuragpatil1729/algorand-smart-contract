FROM python:3.12-slim

WORKDIR /app

COPY shared /app/agents/shared
COPY testing-agent/requirements.txt /app/requirements.txt

RUN pip install --no-cache-dir -r requirements.txt

COPY testing-agent /app/agents/testing-agent

ENV PYTHONPATH=/app
ENV PORT=8005

EXPOSE 8005

CMD ["python", "agents/testing-agent/main.py"]
