import React, { createContext, useContext, useState, useEffect } from 'react';

interface DemoModeContextType {
  demoMode: boolean;
  setDemoMode: (val: boolean) => void;
  toggleDemoMode: () => void;
}

const DemoModeContext = createContext<DemoModeContextType | undefined>(undefined);

export const DemoModeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [demoMode, setDemoModeState] = useState<boolean>(() => {
    const saved = localStorage.getItem('agentmesh_demo_mode');
    return saved !== null ? JSON.parse(saved) : true; // Default to Demo Mode active if backend offline
  });

  const setDemoMode = (val: boolean) => {
    setDemoModeState(val);
    localStorage.setItem('agentmesh_demo_mode', JSON.stringify(val));
  };

  const toggleDemoMode = () => {
    setDemoMode(!demoMode);
  };

  return (
    <DemoModeContext.Provider value={{ demoMode, setDemoMode, toggleDemoMode }}>
      {children}
    </DemoModeContext.Provider>
  );
};

export const useDemoMode = (): DemoModeContextType => {
  const context = useContext(DemoModeContext);
  if (!context) {
    throw new Error('useDemoMode must be used within a DemoModeProvider');
  }
  return context;
};
