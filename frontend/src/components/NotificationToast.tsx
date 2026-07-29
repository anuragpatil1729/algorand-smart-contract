import React from 'react';
import { Info, AlertCircle, CheckCircle, Zap } from 'lucide-react';

export interface NotificationMessage {
  id: string;
  type: 'INFO' | 'SUCCESS' | 'WARN' | 'ERROR';
  title: string;
  message: string;
}

interface NotificationToastProps {
  notifications: NotificationMessage[];
  onDismiss: (id: string) => void;
}

export const NotificationToast: React.FC<NotificationToastProps> = ({ notifications, onDismiss }) => {
  if (notifications.length === 0) return null;

  return (
    <div className="fixed bottom-6 right-6 z-50 space-y-3 max-w-md w-full pointer-events-none">
      {notifications.map((n) => {
        let icon = <Zap className="w-5 h-5 text-cyan-400" />;
        let border = 'border-cyan-500/40 bg-slate-900/95';

        if (n.type === 'SUCCESS') {
          icon = <CheckCircle className="w-5 h-5 text-emerald-400" />;
          border = 'border-emerald-500/40 bg-slate-900/95';
        } else if (n.type === 'WARN') {
          icon = <AlertCircle className="w-5 h-5 text-amber-400" />;
          border = 'border-amber-500/40 bg-slate-900/95';
        } else if (n.type === 'ERROR') {
          icon = <AlertCircle className="w-5 h-5 text-rose-400" />;
          border = 'border-rose-500/40 bg-slate-900/95';
        }

        return (
          <div
            key={n.id}
            className={`pointer-events-auto p-4 rounded-2xl border ${border} shadow-2xl backdrop-blur-xl flex items-start space-x-3 transition-all animate-in slide-in-from-bottom-5 duration-300`}
          >
            <div className="shrink-0 mt-0.5">{icon}</div>
            <div className="flex-1 text-xs">
              <h5 className="font-bold text-slate-100">{n.title}</h5>
              <p className="text-slate-400 mt-0.5 leading-relaxed">{n.message}</p>
            </div>
            <button
              onClick={() => onDismiss(n.id)}
              className="text-slate-500 hover:text-slate-300 text-xs p-1"
            >
              ✕
            </button>
          </div>
        );
      })}
    </div>
  );
};
