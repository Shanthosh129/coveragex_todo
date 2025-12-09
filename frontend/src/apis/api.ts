import axios from 'axios';
import type { CreateTaskRequest, Task } from '../types/task';

// URL of the Spring Boot backend API
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

  // Mark task as done 
  markDone: async (id: string) => {
    // Assuming your Spring Boot endpoint is /tasks/{id}/done
    const { data } = await axios.put<void>(`${API_URL}/${id}/done`);
    return data;
  },
  //update task
   update: async (id: string, data: CreateTaskRequest) => {
    const response = await axios.put<Task>(`${API_URL}/${id}`, data);
    return response.data;
  }
};