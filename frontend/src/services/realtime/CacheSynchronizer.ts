import { QueryClient } from '@tanstack/react-query';
import { EventBus, RealtimeEvent } from './EventBus';

export class CacheSynchronizer {
  private queryClient: QueryClient;
  private eventBus: EventBus;
  private unsubscribe: (() => void) | null = null;

  constructor(queryClient: QueryClient, eventBus: EventBus) {
    this.queryClient = queryClient;
    this.eventBus = eventBus;
  }

  public start(): void {
    this.stop();
    this.unsubscribe = this.eventBus.subscribe('*', (event) => this.handleEvent(event));
  }

  public stop(): void {
    if (this.unsubscribe) {
      this.unsubscribe();
      this.unsubscribe = null;
    }
  }

  private handleEvent(event: RealtimeEvent): void {
    console.log('[CacheSynchronizer] Event received:', event.eventType, event.workflowId);

    // 1. Invalidate System Metrics & Status
    this.queryClient.invalidateQueries({ queryKey: ['systemMetrics'] });
    this.queryClient.invalidateQueries({ queryKey: ['systemStatus'] });

    // 2. Specific Event Handling
    switch (event.eventType) {
      case 'PAYMENT_VERIFIED':
      case 'RECEIPT_GENERATED':
        this.queryClient.invalidateQueries({ queryKey: ['paymentsHistory'] });
        break;

      case 'TASK_STARTED':
      case 'TASK_COMPLETED':
      case 'TASK_FAILED':
      case 'WORKFLOW_STARTED':
      case 'WORKFLOW_COMPLETED':
        if (event.workflowId) {
          this.queryClient.invalidateQueries({ queryKey: ['workflowStatus', event.workflowId] });
          this.queryClient.invalidateQueries({ queryKey: ['workflowLogs', event.workflowId] });
          this.queryClient.invalidateQueries({ queryKey: ['workflowEvents', event.workflowId] });
        }
        break;

      case 'DISCOVERY_COMPLETED':
      case 'QUOTES_RECEIVED':
        this.queryClient.invalidateQueries({ queryKey: ['agentsList'] });
        break;
    }
  }
}
