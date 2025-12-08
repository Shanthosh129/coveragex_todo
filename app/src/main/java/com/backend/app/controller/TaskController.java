package com.backend.app.controller;

import java.util.List;
import java.util.UUID;

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
import com.backend.app.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Log4j2
public class TaskController {

    private final TaskService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid TaskRequest request) {
        String requestId = UUID.randomUUID().toString();
        log.info("Creating task with requestId: {}", requestId);
        service.create(request, requestId);
        SuccessMessage successResponse = new SuccessMessage("Task created successfully", requestId);
        return new ResponseEntity<>(successResponse,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks() {
        String requestId = UUID.randomUUID().toString();
        return new ResponseEntity<>(service.getRecentTasks(requestId), HttpStatus.OK);
    }

    @PutMapping("/{id}/done")
    public ResponseEntity<SuccessMessage> markAsDone(@PathVariable Long id) {
        service.markAsDone(id);
        SuccessMessage successResponse = new SuccessMessage("Task marked as done successfully", UUID.randomUUID().toString());
        return new ResponseEntity<>(successResponse, HttpStatus.NO_CONTENT);
    }
}
