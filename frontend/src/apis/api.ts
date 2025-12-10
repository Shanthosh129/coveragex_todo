import axios, { AxiosError } from 'axios';
import { toast } from 'react-toastify'
import type { CreateTaskRequest, Task } from '../types/task';

// URL of the Spring Boot backend API
const API_URL = '/api/v1/tasks';

// Helper to normalize backend error responses (your backend returns { errors: [{code,message}], requestId })
function normalizeAxiosError(err: unknown) {
  if (!err) return { messages: ['Unknown error'], status: undefined };
  if ((err as AxiosError).isAxiosError) {
    const ax = err as AxiosError;
    const status = ax.response?.status;
    const data = ax.response?.data as any;
    if (data && Array.isArray(data.errors)) {
      const messages = data.errors.map((e: any) => e.message || String(e));
      return { messages, status, requestId: data.requestId };
    }
    return { messages: [ax.message || 'Request failed'], status };
  }
  return { messages: [String(err)], status: undefined };
}

export const api = {
  // Get recent 5 tasks
  getRecent: async () => {
    try {
      const { data } = await axios.get<Task[]>(API_URL);
      return data;
    } catch (e) {
      const norm = normalizeAxiosError(e);
      toast.error((norm.messages || ['Unexpected error']).join('; '));
      throw norm;
    }
  },

  // Create a new task
  create: async (task: CreateTaskRequest) => {
    try {
      const { data } = await axios.post<Task>(API_URL, task);
      toast.success(`Created: ${data.title}`);
      return data;
    } catch (e) {
      const norm = normalizeAxiosError(e);
      toast.error((norm.messages || ['Failed to create']).join('; '));
      throw norm;
    }
  },

  // Mark task as done 
  markDone: async (id: string) => {
    try {
      const { data } = await axios.put<void>(`${API_URL}/${id}/done`);
      toast.success('Task marked as done');
      return data;
    } catch (e) {
      const norm = normalizeAxiosError(e);
      toast.error((norm.messages || ['Failed to mark as done']).join('; '));
      throw norm;
    }
  },

  // Update task
  update: async (id: string, payload: CreateTaskRequest) => {
    try {
      const response = await axios.put<Task>(`${API_URL}/${id}`, payload);
      toast.success(`Updated: ${response.data.title}`);
      return response.data;
    } catch (e) {
      const norm = normalizeAxiosError(e);
      toast.error((norm.messages || ['Failed to update']).join('; '));
      throw norm;
    }
  }
};