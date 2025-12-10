package com.backend.app.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.app.dto.SuccessMessage;
import com.backend.app.dto.TaskRequest;
import com.backend.app.dto.TaskResponse;
import com.backend.app.entity.Task;
import com.backend.app.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Log4j2
public class TaskController {

    private final TaskService service;

    @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResponse> create(@RequestBody @Valid TaskRequest request) {
        String requestId = UUID.randomUUID().toString();
        log.info("Creating task with requestId: {}", requestId);
        TaskResponse response = service.create(request, requestId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks() {
        String requestId = UUID.randomUUID().toString();
        return new ResponseEntity<>(service.getRecentTasks(requestId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        String requestId = UUID.randomUUID().toString();
        log.info("Fetching task {} with requestId: {}", id, requestId);
        TaskResponse resp = service.getTask(id, requestId);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PutMapping("/{id}/done")
    public ResponseEntity<SuccessMessage> markAsDone(@PathVariable Long id) {
        String requestId = UUID.randomUUID().toString();
        log.info("Marking task {} as done with requestId: {}", id, requestId);
        service.markAsDone(id, requestId);
        SuccessMessage successResponse = new SuccessMessage("Task marked as done successfully", UUID.randomUUID().toString());
        return new ResponseEntity<>(successResponse, HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody @Valid TaskRequest request) {
        String requestId = UUID.randomUUID().toString();
        log.info("Updating task {} with requestId: {}", id, requestId);
        TaskResponse response = service.updateTask(id, request, requestId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
