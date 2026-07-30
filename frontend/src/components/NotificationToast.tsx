import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { CheckCircle2, AlertTriangle, ShieldCheck, Zap, X } from 'lucide-react';

export interface NotificationMessage {
  id: string;
  type: 'SUCCESS' | 'ERROR' | 'PAYMENT' | 'INFO';
  title: string;
  message: string;
}

interface NotificationToastProps {
  notifications: NotificationMessage[];
  onDismiss: (id: string) => void;
}

export const NotificationToast: React.FC<NotificationToastProps> = ({ notifications, onDismiss }) => {
  return (
    <div className="fixed bottom-6 right-6 z-50 flex flex-col space-y-3 pointer-events-none max-w-md w-full">
      <AnimatePresence>
        {notifications.map((toast) => (
          <motion.div
            key={toast.id}
            initial={{ opacity: 0, y: 20, scale: 0.95 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: -20, scale: 0.95 }}
            className="pointer-events-auto glass-panel p-4 border-slate-800 bg-slate-900/90 shadow-2xl flex items-start justify-between space-x-3"
          >
            <div className="flex items-start space-x-3">
              {toast.type === 'PAYMENT' && (
                <div className="p-2 rounded-xl bg-violet-500/10 border border-violet-500/30 text-violet-400">
                  <ShieldCheck className="w-5 h-5" />
                </div>
              )}
              {toast.type === 'SUCCESS' && (
                <div className="p-2 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-400">
                  <CheckCircle2 className="w-5 h-5" />
                </div>
              )}
              {toast.type === 'ERROR' && (
                <div className="p-2 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400">
                  <AlertTriangle className="w-5 h-5" />
                </div>
              )}
              {toast.type === 'INFO' && (
                <div className="p-2 rounded-xl bg-indigo-500/10 border border-indigo-500/30 text-indigo-400">
                  <Zap className="w-5 h-5" />
                </div>
              )}

              <div>
                <h4 className="text-xs font-bold text-slate-100 font-mono tracking-tight">
                  {toast.title}
                </h4>
                <p className="text-xs text-slate-400 mt-0.5 leading-relaxed font-sans">
                  {toast.message}
                </p>
              </div>
            </div>

            <button
              onClick={() => onDismiss(toast.id)}
              className="text-slate-500 hover:text-slate-300 p-1 transition-colors"
            >
              <X className="w-4 h-4" />
            </button>
          </motion.div>
        ))}
      </AnimatePresence>
    </div>
  );
};
