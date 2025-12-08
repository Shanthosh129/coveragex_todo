import axios from 'axios';
import type { CreateTaskRequest, Task } from '../types/task';

// UPDATE THIS to match your Spring Boot Controller path
const API_URL = 'http://localhost:8080/api/v1/tasks';

export const api = {
  // Get recent 5 tasks
  getRecent: async () => {
    const { data } = await axios.get<Task[]>(API_URL);
    return data;
  },

  // Create a new task
  create: async (task: CreateTaskRequest) => {
    const { data } = await axios.post<Task>(API_URL, task);
    return data;
  },

  // Mark task as done (Using PATCH or PUT based on your Spring Boot)
  markDone: async (id: string) => {
    // Assuming your Spring Boot endpoint is /tasks/{id}/done
    const { data } = await axios.patch<void>(`${API_URL}/${id}/done`);
    return data;
  }
};