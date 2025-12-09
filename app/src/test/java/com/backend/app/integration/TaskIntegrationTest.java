package com.backend.app.integration;

import com.backend.app.dto.TaskRequest;
import com.backend.app.entity.Task;
import com.backend.app.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper; 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class TaskIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TaskRepository repository;

    private ObjectMapper mapper;

    @BeforeEach
    void cleanup() {
        repository.deleteAll();
        mapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testCreateAndFetchTask() throws Exception {

        TaskRequest req = new TaskRequest("Integration", "Testing");

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Integration"));
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
