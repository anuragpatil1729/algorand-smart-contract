export type ConnectionStatus = 'CONNECTED' | 'RECONNECTING' | 'OFFLINE';

export class ReconnectManager {
  private attempts: number = 0;
  private maxAttempts: number = 6;
  private listeners: Set<(status: ConnectionStatus) => void> = new Set();
  private currentStatus: ConnectionStatus = 'OFFLINE';

  public onStatusChange(callback: (status: ConnectionStatus) => void): () => void {
    this.listeners.add(callback);
    callback(this.currentStatus);
    return () => {
      this.listeners.delete(callback);
    };
  }

  public setStatus(status: ConnectionStatus): void {
    this.currentStatus = status;
    if (status === 'CONNECTED') {
      this.attempts = 0;
    }
    this.listeners.forEach((callback) => callback(status));
  }

  public getStatus(): ConnectionStatus {
    return this.currentStatus;
  }

  public scheduleReconnect(reconnectFn: () => void): void {
    if (this.attempts >= this.maxAttempts) {
      this.setStatus('OFFLINE');
      return;
    }

    this.setStatus('RECONNECTING');
    this.attempts++;
    const delay = Math.min(1000 * Math.pow(2, this.attempts), 10000);
    setTimeout(reconnectFn, delay);
  }
}
