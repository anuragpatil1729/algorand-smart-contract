export type EventType =
  | 'WORKFLOW_CREATED'
  | 'PLANNER_FINISHED'
  | 'DISCOVERY_STARTED'
  | 'DISCOVERY_COMPLETED'
  | 'QUOTES_RECEIVED'
  | 'ASSIGNMENT_CREATED'
  | 'PAYMENT_REQUIRED'
  | 'PAYMENT_VERIFIED'
  | 'WORKFLOW_STARTED'
  | 'TASK_STARTED'
  | 'TASK_PROGRESS'
  | 'TASK_COMPLETED'
  | 'TASK_FAILED'
  | 'RETRY_STARTED'
  | 'FALLBACK_STARTED'
  | 'WORKFLOW_COMPLETED'
  | 'WORKFLOW_FAILED'
  | 'RECEIPT_GENERATED';

export interface RealtimeEvent {
  eventType: EventType;
  workflowId?: string;
  taskId?: string;
  agentId?: string;
  details?: string;
  payload?: any;
  timestamp: number;
}

export type EventCallback = (event: RealtimeEvent) => void;

export class EventBus {
  private listeners: Map<string, Set<EventCallback>> = new Map();

  public subscribe(eventType: EventType | '*', callback: EventCallback): () => void {
    if (!this.listeners.has(eventType)) {
      this.listeners.set(eventType, new Set());
    }
    this.listeners.get(eventType)!.add(callback);

    return () => {
      const set = this.listeners.get(eventType);
      if (set) {
        set.delete(callback);
      }
    };
  }

  public emit(event: RealtimeEvent): void {
    const specificListeners = this.listeners.get(event.eventType);
    if (specificListeners) {
      specificListeners.forEach((callback) => callback(event));
    }

    const wildcardListeners = this.listeners.get('*');
    if (wildcardListeners) {
      wildcardListeners.forEach((callback) => callback(event));
    }
  }
}
