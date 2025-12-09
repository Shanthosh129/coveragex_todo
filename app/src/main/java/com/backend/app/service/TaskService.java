package com.backend.app.service;

import java.util.List;
import com.backend.app.dto.TaskRequest;
import com.backend.app.dto.TaskResponse;

public interface TaskService {
    TaskResponse create(TaskRequest request,String requestId);
    List<TaskResponse> getRecentTasks(String requestId);
    void markAsDone(Long id, String requestId);
    TaskResponse getTask(Long id, String requestId);
    TaskResponse updateTask(Long id, TaskRequest request, String requestId);
}
