package com.backend.app.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.app.exception.ProcessingException;
import com.backend.app.exception.ValidationException;
import com.backend.app.exception.ResourceNotFoundException;
import com.backend.app.dto.ErrorMsg;
import com.backend.app.dto.TaskRequest;
import com.backend.app.dto.TaskResponse;
import com.backend.app.entity.Task;
import com.backend.app.repository.TaskRepository;
import com.backend.app.service.TaskService;
import org.springframework.dao.DataAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;

    // create task
    @Override
    public TaskResponse create(TaskRequest request, String requestId) {
        List<ErrorMsg> errors = new ArrayList<>();
        try {
            if (request.title() == null || request.title().isBlank()) {
                errors.add(new ErrorMsg(1001, "Title is required"));
                throw new ValidationException(errors, requestId);
            }
            Task task = new Task();
            task.setTitle(request.title());
            task.setDescription(request.description());
            task.setCompleted(false);

            Task saved = repository.save(task);
            return mapToResponse(saved);

        } catch (ValidationException e) {
            log.error("Validation errors for requestId {}: {}", requestId, e.getErrors());
            throw e;
        }catch (Exception e) {
            log.error("Unexpected error occurred for requestId {}: {}", requestId, e.getMessage());
            throw new ProcessingException(List.of(new ErrorMsg(1000, e.getMessage())), requestId);
        }
    }

    // get recent tasks
    @Override
    public List<TaskResponse> getRecentTasks(String requestId) {
        try {
            return repository.findTop5ByCompletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
        } catch (Exception e) {
           log.error("Error fetching recent tasks: {}", e.getMessage());
           throw new ProcessingException(List.of(new ErrorMsg(1000, e.getMessage())), requestId);
        }
    }

    // mark task as done
    @Override
    public void markAsDone(Long id,String requestId) {
        List<ErrorMsg> errors = new ArrayList<>();
        try {
            if (id == null || id <= 0) {
                errors.add(new ErrorMsg(1002, "Invalid task id"));
                throw new ValidationException(errors, requestId);
            }

            Task task = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            List.of(new ErrorMsg(1004, "Task not found")), requestId));

            task.setCompleted(true);
            repository.save(task);
        } catch (ValidationException e) {
            log.error("Validation error marking task done for requestId {}: {}", requestId, e.getErrors());
            throw e;
        } catch (ResourceNotFoundException e) {
            log.error("Task not found for id {} requestId {}: {}", id, requestId, e.getErrors());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while marking task done for requestId {}: {}", requestId, e.getMessage());
            throw new ProcessingException(List.of(new ErrorMsg(1000, e.getMessage())), requestId);
        }
    }

    @Override
    public TaskResponse getTask(Long id, String requestId) {
        try {
            if (id == null || id <= 0) {
                throw new ValidationException(List.of(new ErrorMsg(1002, "Invalid task id")), requestId);
            }
            Task task = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(List.of(new ErrorMsg(1004, "Task not found")), requestId));
            return mapToResponse(task);
        }catch (ValidationException e) {
            log.error("Validation error fetching task {} for requestId {}: {}", id, requestId, e.getMessage());
            throw e;
        } 
        catch (ResourceNotFoundException e) {
            log.error("Error fetching task {} for requestId {}: {}", id, requestId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching task {} for requestId {}: {}", id, requestId, e.getMessage());
            throw new ProcessingException(List.of(new ErrorMsg(1000, e.getMessage())), requestId);
        }
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request, String requestId) {
        List<ErrorMsg> errors = new ArrayList<>();
        try {
            if (id == null || id <= 0) {
                errors.add(new ErrorMsg(1002, "Invalid task id"));
                throw new ValidationException(errors, requestId);
            }
            if (request.title() == null || request.title().isBlank()) {
                errors.add(new ErrorMsg(1001, "Title is required"));
                throw new ValidationException(errors, requestId);
            }

            Task task = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(List.of(new ErrorMsg(1004, "Task not found")), requestId));

            task.setTitle(request.title());
            task.setDescription(request.description());

            Task saved = repository.save(task);
            return mapToResponse(saved);
        } catch (ValidationException e) {
            log.error("Validation error updating task {} for requestId {}: {}", id, requestId, e.getMessage());
            throw e;
        } catch (ResourceNotFoundException e) {
            log.error("NotFound error updating task {} for requestId {}: {}", id, requestId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error updating task {} for requestId {}: {}", id, requestId, e.getMessage());
            throw new ProcessingException(List.of(new ErrorMsg(1000, e.getMessage())), requestId);
        }
    }

    // helper function to map Task entity to TaskResponse DTO
    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt());
    }
}
