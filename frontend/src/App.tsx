import React, { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Navbar } from './components/Navbar';
import { Sidebar } from './components/Sidebar';
import { NotificationToast, NotificationMessage } from './components/NotificationToast';
import { Dashboard } from './pages/Dashboard';
import { PlannerPage } from './pages/PlannerPage';
import { WorkflowDetails } from './pages/WorkflowDetails';
import { Marketplace } from './pages/Marketplace';
import { PaymentsPage } from './pages/PaymentsPage';
import { AnalyticsPage } from './pages/AnalyticsPage';
import { AdminPage } from './pages/AdminPage';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

export const App: React.FC = () => {
  const [notifications, setNotifications] = useState<NotificationMessage[]>([
    {
      id: 'init-1',
      type: 'SUCCESS',
      title: 'Algorand Router Active',
      message: 'AgentMesh backend connected with PyTeal Escrow Smart Contract & Atomic Transfer Group settlement.'
    }
  ]);

  const handleDismiss = (id: string) => {
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  };

  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <div className="flex flex-col min-h-screen bg-[#0B0F19] text-slate-100 font-sans">
          <Navbar />
          
          <div className="flex flex-1 overflow-hidden">
            <Sidebar />
            
            <main className="flex-1 overflow-y-auto bg-[#070b14] min-h-[calc(100vh-4rem)] p-6">
              <Routes>
                <Route path="/" element={<Dashboard />} />
                <Route path="/planner" element={<PlannerPage />} />
                <Route path="/workflows" element={<WorkflowDetails />} />
                <Route path="/workflows/:id" element={<WorkflowDetails />} />
                <Route path="/marketplace" element={<Marketplace />} />
                <Route path="/payments" element={<PaymentsPage />} />
                <Route path="/analytics" element={<AnalyticsPage />} />
                <Route path="/admin" element={<AdminPage />} />
              </Routes>
            </main>
          </div>

          <NotificationToast notifications={notifications} onDismiss={handleDismiss} />
        </div>
      </BrowserRouter>
    </QueryClientProvider>
  );
};

export default App;
