export class HeartbeatMonitor {
  private timer: NodeJS.Timeout | null = null;
  private intervalMs: number;
  private onPing: () => void;

  constructor(onPing: () => void, intervalMs: number = 15000) {
    this.onPing = onPing;
    this.intervalMs = intervalMs;
  }

  public start(): void {
    this.stop();
    this.timer = setInterval(() => {
      this.onPing();
    }, this.intervalMs);
  }

  public stop(): void {
    if (this.timer) {
      clearInterval(this.timer);
      this.timer = null;
    }
  }
}
