package com.backend.app.integration;

import com.backend.app.dto.TaskRequest;
import com.backend.app.entity.Task;
import com.backend.app.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper; 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class TaskIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TaskRepository repository;

    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testCreateAndFetchTask() throws Exception {

        TaskRequest req = new TaskRequest("Integration", "Testing");

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Integration"));
    }

    @Test
    void testUpdateTaskIntegration() throws Exception {
        // First create a task
        TaskRequest createReq = new TaskRequest("Original Task", "Original description");
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated());

        // Fetch the task to get its ID
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Original Task"))
                .andExpect(jsonPath("$[0].description").value("Original description"));

        // Assume ID is 1 for simplicity, or we can parse it
        Long taskId = 1L;

        // Update the task
        TaskRequest updateReq = new TaskRequest("Updated Task", "Updated description");
        mockMvc.perform(put("/api/v1/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    void testMarkAsDoneIntegration() throws Exception {

        Task t = new Task();
        t.setTitle("Task");
        t.setDescription("Test");
        t.setCompleted(false);

        Task saved = repository.save(t);

        mockMvc.perform(put("/api/v1/tasks/" + saved.getId() + "/done"))
                .andExpect(status().isNoContent());
    }
}
