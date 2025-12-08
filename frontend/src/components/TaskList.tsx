import { Check, Clock, Inbox } from 'lucide-react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '../apis/api'; // Ensure this matches your folder structure
import { Card } from './card';
import { motion, AnimatePresence } from 'framer-motion';

export const TaskList = () => {
  const queryClient = useQueryClient();
  const { data: tasks, isLoading } = useQuery({ queryKey: ['tasks'], queryFn: api.getRecent });

  const mutation = useMutation({
    mutationFn: api.markDone,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['tasks'] }),
  });

  if (isLoading) return <div className="text-center p-10 text-slate-400 animate-pulse">Loading tasks...</div>;

  // --- EMPTY STATE CARD ---
  if (!tasks || tasks.length === 0) {
    return (
      <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
        <Card className="p-10 flex flex-col items-center justify-center text-center min-h-[200px] border-dashed border-2 border-slate-300 dark:border-slate-700 shadow-none bg-transparent">
          <div className="p-4 bg-slate-100 dark:bg-slate-800 rounded-full mb-4 text-slate-400">
            <Inbox size={32} />
          </div>
          <h3 className="text-lg font-semibold text-slate-700 dark:text-slate-300">No Task added</h3>
          <p className="text-sm text-slate-500 dark:text-slate-500 mt-1">
            Use the form on the left to create one.
          </p>
        </Card>
      </motion.div>
    );
  }

  // --- LIST OF TASKS ---
  return (
    <div className="grid grid-cols-1 gap-4">
      <AnimatePresence mode="popLayout">
        {tasks.map((task, index) => (
          <motion.div
            key={task.id}
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -50 }}
            transition={{ delay: index * 0.05 }}
            layout
          >
            <Card className="group hover:border-indigo-400 dark:hover:border-indigo-500 transition-all duration-300">
              <div className="p-5 flex flex-col sm:flex-row gap-4 justify-between items-start sm:items-center">
                <div className="space-y-1">
                  <h3 className="font-bold text-lg text-slate-800 dark:text-slate-100 group-hover:text-indigo-600 dark:group-hover:text-indigo-400 transition-colors">
                    {task.title}
                  </h3>
                  {task.description && (
                    <p className="text-slate-600 dark:text-slate-400 text-sm line-clamp-2">
                      {task.description}
                    </p>
                  )}
                  <div className="flex items-center gap-1 text-xs text-slate-400 mt-2">
                    <Clock size={12} />
                    <span>Just now</span>
                  </div>
                </div>

                <button
                  onClick={() => mutation.mutate(task.id)}
                  className="shrink-0 flex items-center gap-2 px-4 py-2 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 text-sm font-medium hover:bg-green-500 hover:text-white dark:hover:bg-green-600 transition-all duration-300 shadow-sm"
                >
                  <Check size={16} />
                  <span>Done</span>
                </button>
              </div>
            </Card>
          </motion.div>
        ))}
      </AnimatePresence>
    </div>
  );
};