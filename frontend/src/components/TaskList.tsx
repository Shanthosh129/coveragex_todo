import { useState } from 'react'; // Import useState
import { Check, Clock, Edit2 } from 'lucide-react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '../apis/api';
import { Card } from './card';
import { Modal } from './Modal'; // Import Modal
import { EditTaskForm } from './EditTaskForm'; // Import EditForm
import type{ Task } from '../types/task';
import { motion, AnimatePresence } from 'framer-motion';

export const TaskList = () => {
  const queryClient = useQueryClient();
  const [editingTask, setEditingTask] = useState<Task | null>(null); // State for Modal

  const { data: tasks, isLoading } = useQuery({ queryKey: ['tasks'], queryFn: api.getRecent });

  const mutation = useMutation({
    mutationFn: api.markDone,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['tasks'] }),
  });

  if (isLoading) return <div className="text-center p-10 text-slate-400 animate-pulse">Loading tasks...</div>;

  if (!tasks || tasks.length === 0) {
    // ... (Keep your existing Empty State code here) ...
    return <div>No Tasks</div>; // Placeholder for brevity
  }

  return (
    <>
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
              // ADD CLICK HANDLER TO OPEN MODAL
              onClick={() => setEditingTask(task)}
              className="cursor-pointer" // Add cursor pointer
            >
              <Card className="group hover:border-indigo-400 dark:hover:border-indigo-500 transition-all duration-300 relative overflow-hidden">
                {/* Optional: Add a subtle edit hint overlay on hover */}
                <div className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity text-slate-400">
                  <Edit2 size={12} />
                </div>

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
                    onClick={(e) => {
                      e.stopPropagation(); // <--- CRITICAL: Stops modal from opening
                      mutation.mutate(task.id);
                    }}
                    className="shrink-0 flex items-center gap-2 px-4 py-2 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 text-sm font-medium hover:bg-green-500 hover:text-white dark:hover:bg-green-600 transition-all duration-300 shadow-sm z-10"
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

      {/* RENDER MODAL */}
      {editingTask && (
        <Modal 
          isOpen={!!editingTask} 
          onClose={() => setEditingTask(null)} 
          title="Edit Task"
        >
          <EditTaskForm 
            task={editingTask} 
            onClose={() => setEditingTask(null)} 
          />
        </Modal>
      )}
    </>
  );
};