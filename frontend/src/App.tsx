import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ThemeProvider } from './context/ThemeContext';
import { TaskForm } from './components/TaskForm';
import { TaskList } from './components/TaskList';
import { ThemeToggle } from './components/ThemeToggle';
import { LayoutDashboard } from 'lucide-react';

const queryClient = new QueryClient();

function App() {
  return (
    <ThemeProvider>
      <QueryClientProvider client={queryClient}>
        <div className="min-h-screen transition-colors duration-300 bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 font-sans selection:bg-indigo-500 selection:text-white">
          
          {/* Background Blobs (Decoration) */}
          <div className="fixed inset-0 z-0 overflow-hidden pointer-events-none">
            <div className="absolute top-0 left-0 w-[500px] h-[500px] bg-indigo-500/10 rounded-full blur-3xl opacity-50" />
            <div className="absolute bottom-0 right-0 w-[500px] h-[500px] bg-blue-500/10 rounded-full blur-3xl opacity-50" />
          </div>

          <div className="relative z-10 h-screen flex flex-col">
            
            {/* 1. Header Bar with Toggle in Top Right */}
            <header className="flex justify-between items-center px-8 py-4 bg-white/50 dark:bg-slate-900/50 backdrop-blur-sm border-b border-slate-200 dark:border-slate-800">
              <div className="flex items-center gap-3">
                <div className="bg-indigo-600 p-2 rounded-lg text-white shadow-lg shadow-indigo-500/20">
                  <LayoutDashboard size={20} />
                </div>
                <h1 className="text-xl font-bold tracking-tight">Task Manager</h1>
              </div>
              
              {/* Toggle Button */}
              <ThemeToggle />
            </header>

            {/* 2. Split Screen Layout (Left & Right) */}
            <div className="flex-1 grid grid-cols-1 lg:grid-cols-2 gap-0 overflow-hidden">
              
              {/* LEFT SIDE: Add Task */}
              <div className="p-8 lg:p-12 overflow-y-auto border-r border-slate-200 dark:border-slate-800 flex flex-col items-center">
                 <div className="w-full max-w-lg">
                    <h2 className="text-2xl font-bold mb-6 text-slate-800 dark:text-white flex items-center gap-2">
                      Add Task
                    </h2>
                    <TaskForm />
                 </div>
              </div>

              {/* RIGHT SIDE: View Task */}
              <div className="p-8 lg:p-12 overflow-y-auto bg-slate-100/50 dark:bg-slate-900/50 flex flex-col items-center">
                 <div className="w-full max-w-lg">
                    <div className="flex justify-between items-end mb-6">
                      <h2 className="text-2xl font-bold text-slate-800 dark:text-white">
                        View Tasks
                      </h2>
                      <span className="text-xs font-semibold px-2 py-1 bg-indigo-100 dark:bg-indigo-900 text-indigo-700 dark:text-indigo-300 rounded">
                        Recent 5
                      </span>
                    </div>
                    <TaskList />
                 </div>
              </div>

            </div>
          </div>
        </div>
      </QueryClientProvider>
    </ThemeProvider>
  );
}

export default App;