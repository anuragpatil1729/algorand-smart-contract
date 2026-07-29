FROM python:3.12-slim

WORKDIR /app

COPY shared /app/agents/shared
COPY ppt-agent/requirements.txt /app/requirements.txt

RUN pip install --no-cache-dir -r requirements.txt

COPY ppt-agent /app/agents/ppt-agent

ENV PYTHONPATH=/app
ENV PORT=8004

EXPOSE 8004

CMD ["python", "agents/ppt-agent/main.py"]
