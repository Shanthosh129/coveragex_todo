package com.backend.app.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.backend.app.dto.TaskRequest;
import com.backend.app.dto.TaskResponse;
import com.backend.app.service.TaskService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testCreateTask() throws Exception {
        TaskRequest request = new TaskRequest("Sample Task", "This is a sample task description");
        TaskResponse res = new TaskResponse(1L, "Sample Task", "This is a sample task description", null);
        when(service.create(eq(request), anyString())).thenReturn(res);
        mockMvc.perform(post("/api/v1/tasks")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample Task"));

    }

    @Test
    void testGetTasks() throws Exception {
        var res = new TaskResponse(1L, "Sample Task", "This is a sample task description", null);
        when(service.getRecentTasks(anyString())).thenReturn(List.of(res));
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testUpdateTask() throws Exception {
        Long taskId = 1L;
        TaskRequest request = new TaskRequest("Updated Task", "Updated description");
        TaskResponse taskResponse = new TaskResponse(1L, "Updated Task", "Updated description", null);

        when(service.updateTask(eq(taskId), eq(request), anyString())).thenReturn(taskResponse);

        mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }
}
