import React, { createContext, useContext, useEffect, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { RealtimeManager } from '../services/realtime/RealtimeManager';
import { ConnectionStatus } from '../services/realtime/ReconnectManager';

interface WebSocketContextType {
  status: ConnectionStatus;
  realtimeManager: RealtimeManager | null;
}

const WebSocketContext = createContext<WebSocketContextType>({
  status: 'OFFLINE',
  realtimeManager: null,
});

export const WebSocketProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const queryClient = useQueryClient();
  const [manager, setManager] = useState<RealtimeManager | null>(null);
  const [status, setStatus] = useState<ConnectionStatus>('CONNECTED'); // Default CONNECTED for smooth UX

  useEffect(() => {
    const realtimeManager = new RealtimeManager(queryClient);
    setManager(realtimeManager);
    realtimeManager.start();

    const unsubscribe = realtimeManager.getReconnectManager().onStatusChange((newStatus) => {
      setStatus(newStatus);
    });

    return () => {
      unsubscribe();
      realtimeManager.stop();
    };
  }, [queryClient]);

  return (
    <WebSocketContext.Provider value={{ status, realtimeManager: manager }}>
      {children}
    </WebSocketContext.Provider>
  );
};

export const useWebSocket = (): WebSocketContextType => useContext(WebSocketContext);
