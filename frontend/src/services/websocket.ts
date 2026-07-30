export class WebSocketManager {
  private static instance: WebSocketManager;
  private socket: WebSocket | null = null;
  private listeners: Map<string, Set<(data: any) => void>> = new Map();
  private isConnecting: boolean = false;
  private reconnectAttempts: number = 0;
  private maxReconnectAttempts: number = 5;

  private constructor() {}

  public static getInstance(): WebSocketManager {
    if (!WebSocketManager.instance) {
      WebSocketManager.instance = new WebSocketManager();
    }
    return WebSocketManager.instance;
  }

  public connect(url: string = 'ws://localhost:8080/ws'): void {
    if (this.socket || this.isConnecting) return;
    this.isConnecting = true;

    try {
      this.socket = new WebSocket(url);

      this.socket.onopen = () => {
        console.log('WebSocket Connection Established:', url);
        this.isConnecting = false;
        this.reconnectAttempts = 0;
      };

      this.socket.onmessage = (event) => {
        try {
          const payload = JSON.parse(event.data);
          const topic = payload.topic || 'execution-events';
          const topicListeners = this.listeners.get(topic);
          if (topicListeners) {
            topicListeners.forEach((callback) => callback(payload));
          }
        } catch (e) {
          console.warn('Failed to parse WebSocket message:', event.data);
        }
      };

      this.socket.onerror = (error) => {
        console.warn('WebSocket error:', error);
        this.isConnecting = false;
      };

      this.socket.onclose = () => {
        console.log('WebSocket connection closed.');
        this.socket = null;
        this.isConnecting = false;
        this.scheduleReconnect(url);
      };
    } catch (err) {
      this.isConnecting = false;
      this.scheduleReconnect(url);
    }
  }

  private scheduleReconnect(url: string): void {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      const delay = Math.min(1000 * Math.pow(2, this.reconnectAttempts), 10000);
      setTimeout(() => this.connect(url), delay);
    }
  }

  public subscribe(topic: string, callback: (data: any) => void): () => void {
    if (!this.listeners.has(topic)) {
      this.listeners.set(topic, new Set());
    }
    this.listeners.get(topic)!.add(callback);

    return () => {
      const topicListeners = this.listeners.get(topic);
      if (topicListeners) {
        topicListeners.delete(callback);
      }
    };
  }
}
