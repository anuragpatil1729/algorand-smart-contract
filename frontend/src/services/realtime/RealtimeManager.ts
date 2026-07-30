import { QueryClient } from '@tanstack/react-query';
import { EventBus, RealtimeEvent } from './EventBus';
import { HeartbeatMonitor } from './HeartbeatMonitor';
import { ReconnectManager, ConnectionStatus } from './ReconnectManager';
import { CacheSynchronizer } from './CacheSynchronizer';

export class RealtimeManager {
  private eventBus: EventBus;
  private heartbeatMonitor: HeartbeatMonitor;
  private reconnectManager: ReconnectManager;
  private cacheSynchronizer: CacheSynchronizer;
  private socket: WebSocket | null = null;
  private url: string;

  constructor(queryClient: QueryClient, url: string = 'ws://localhost:8080/ws') {
    this.url = url;
    this.eventBus = new EventBus();
    this.reconnectManager = new ReconnectManager();
    this.cacheSynchronizer = new CacheSynchronizer(queryClient, this.eventBus);

    this.heartbeatMonitor = new HeartbeatMonitor(() => {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        this.socket.send(JSON.stringify({ type: 'PING', timestamp: Date.now() }));
      }
    }, 15000);
  }

  public start(): void {
    this.cacheSynchronizer.start();
    this.connect();
  }

  public stop(): void {
    this.cacheSynchronizer.stop();
    this.heartbeatMonitor.stop();
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
  }

  public connect(): void {
    if (this.socket) return;

    try {
      this.socket = new WebSocket(this.url);

      this.socket.onopen = () => {
        console.log('[RealtimeManager] Live WebSocket Connected:', this.url);
        this.reconnectManager.setStatus('CONNECTED');
        this.heartbeatMonitor.start();
      };

      this.socket.onmessage = (event) => {
        try {
          const raw = JSON.parse(event.data);
          const realtimeEvent: RealtimeEvent = {
            eventType: raw.eventType || raw.type || 'WORKFLOW_STARTED',
            workflowId: raw.workflowId,
            taskId: raw.taskId,
            agentId: raw.agentId,
            details: raw.details || raw.message,
            payload: raw,
            timestamp: raw.timestamp || Date.now(),
          };
          this.eventBus.emit(realtimeEvent);
        } catch (e) {
          console.warn('[RealtimeManager] Failed to parse message:', event.data);
        }
      };

      this.socket.onerror = () => {
        this.reconnectManager.setStatus('RECONNECTING');
      };

      this.socket.onclose = () => {
        console.log('[RealtimeManager] WebSocket connection closed.');
        this.socket = null;
        this.heartbeatMonitor.stop();
        this.reconnectManager.scheduleReconnect(() => this.connect());
      };
    } catch (err) {
      this.reconnectManager.scheduleReconnect(() => this.connect());
    }
  }

  public getEventBus(): EventBus {
    return this.eventBus;
  }

  public getReconnectManager(): ReconnectManager {
    return this.reconnectManager;
  }

  public emitSimulatedEvent(event: RealtimeEvent): void {
    this.eventBus.emit(event);
  }
}
