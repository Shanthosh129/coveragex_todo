export interface Task {
  id: string;        // UUID from Spring Boot
  title: string;
  description?: string;
  completed: boolean; // or isCompleted, depending on your Java Entity
  createdAt: string;
}

export interface CreateTaskRequest {
  title: string;
  description: string;
}