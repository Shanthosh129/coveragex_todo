import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '../apis/api';
import type{ Task, CreateTaskRequest } from '../types/task';
import { useState } from 'react';
import ErrorCard from './ErrorCard';

interface Props {
  task: Task;
  onClose: () => void;
}

export const EditTaskForm = ({ task, onClose }: Props) => {
  const queryClient = useQueryClient();
  
  const { register, handleSubmit, formState: { errors } } = useForm<CreateTaskRequest>({
    defaultValues: {
      title: task.title,
      description: task.description || ''
    }
  });

  const mutation = useMutation({
    mutationFn: (data: CreateTaskRequest) => api.update(task.id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tasks'] });
      onClose(); 
    },
    onError: (err: any) => {
      if (err && Array.isArray(err.messages)) setServerError(err.messages);
      else setServerError([String(err)]);
    }
  });

  const [serverError, setServerError] = useState<string[] | null>(null);

  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      {serverError && <ErrorCard messages={serverError} />}
      <div className="space-y-1">
        <label className="text-xs font-semibold text-slate-500 uppercase">Title</label>
        <input
          {...register("title", { required: "Title is required" })}
          className="w-full bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-700 rounded-lg px-3 py-2 text-slate-800 dark:text-slate-100 focus:ring-2 focus:ring-indigo-500 outline-none"
        />
        {errors.title && <span className="text-red-500 text-xs">{errors.title.message}</span>}
      </div>

      <div className="space-y-1">
        <label className="text-xs font-semibold text-slate-500 uppercase">Description</label>
        <textarea
          {...register("description")}
          rows={4}
          className="w-full bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-700 rounded-lg px-3 py-2 text-slate-800 dark:text-slate-100 focus:ring-2 focus:ring-indigo-500 outline-none resize-none"
        />
      </div>

      <div className="flex justify-end gap-2 mt-4">
        <button
          type="button"
          onClick={onClose}
          className="px-4 py-2 text-sm font-medium text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-700 rounded-lg transition-colors"
        >
          Cancel
        </button>
        <button
          type="submit"
          disabled={mutation.isPending}
          className="px-4 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow-lg shadow-indigo-500/20 transition-all disabled:opacity-50"
        >
          {mutation.isPending ? 'Saving...' : 'Save Changes'}
        </button>
      </div>
    </form>
  );
};