import { useForm } from 'react-hook-form';
import { Plus, Type, AlignLeft } from 'lucide-react';
import { Card } from './Card';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '../apis/api';
import type{ CreateTaskRequest } from '../types/task';
import { useState } from 'react';
import ErrorCard from './ErrorCard';

export const TaskForm = () => {
  const queryClient = useQueryClient();
  const { register, handleSubmit, reset, formState: { errors } } = useForm<CreateTaskRequest>();

  const [serverError, setServerError] = useState<string[] | null>(null);

  const mutation = useMutation({
    mutationFn: api.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tasks'] });
      reset();
      setServerError(null);
    },
    onError: (err: any) => {
      if (err && Array.isArray(err.messages)) setServerError(err.messages);
      else setServerError([String(err)]);
    }
  });

  return (
    <Card className="p-8 bg-white dark:bg-slate-800 shadow-xl shadow-slate-200/50 dark:shadow-black/20">
      <form onSubmit={handleSubmit((d) => mutation.mutate(d))} className="space-y-6">
        {serverError && <ErrorCard messages={serverError} />}
        
        {/* Title Input */}
        <div className="space-y-2">
          <label className="text-xs font-bold text-slate-500 uppercase tracking-wider flex items-center gap-2">
            <Type size={14} className="text-indigo-500" /> 
            Task Title
          </label>
          <input
            {...register("title", { required: "Required" })}
            className="w-full bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-700 rounded-xl px-4 py-3 text-slate-800 dark:text-slate-100 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all placeholder:text-slate-400"
            placeholder="e.g. Review Code PRs"
          />
          {errors.title && <span className="text-red-500 text-xs font-medium">Title is required</span>}
        </div>

        {/* Description Input */}
        <div className="space-y-2">
          <label className="text-xs font-bold text-slate-500 uppercase tracking-wider flex items-center gap-2">
            <AlignLeft size={14} className="text-indigo-500" /> 
            Description
          </label>
          <textarea
            {...register("description")}
            rows={5}
            className="w-full bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-700 rounded-xl px-4 py-3 text-slate-800 dark:text-slate-100 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all resize-none placeholder:text-slate-400"
            placeholder="Add any details here..."
          />
        </div>

        {/* Action Button */}
        <button
          disabled={mutation.isPending}
          className="w-full mt-2 bg-gradient-to-r from-indigo-600 to-blue-600 hover:from-indigo-700 hover:to-blue-700 text-white font-bold py-4 rounded-xl shadow-lg shadow-indigo-500/30 transition-all active:scale-[0.98] flex justify-center items-center gap-2"
        >
          {mutation.isPending ? (
            'Creating...'
          ) : (
            <>
              <Plus size={20} /> Create Task
            </>
          )}
        </button>
      </form>
    </Card>
  );
};