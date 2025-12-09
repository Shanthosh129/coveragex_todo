package com.backend.app.service;

import com.backend.app.dto.TaskRequest;
import com.backend.app.dto.TaskResponse;
import com.backend.app.entity.Task;
import com.backend.app.exception.ProcessingException;
import com.backend.app.exception.ResourceNotFoundException;
import com.backend.app.exception.ValidationException;
import com.backend.app.repository.TaskRepository;
import com.backend.app.service.serviceImpl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    private TaskService service;

    private static final String REQUEST_ID = "req-123";

    @BeforeEach
    void setup() {
        service = new TaskServiceImpl(repository);
    }

    // POSITIVE TEST CASES
    @Test
    @DisplayName("TC-001: Create Task - Success")
    void testCreateTask_Success() {

        TaskRequest request = new TaskRequest("Write Unit Tests", "Cover all scenarios");

        Task mockedSavedTask = new Task();
        mockedSavedTask.setId(1L);
        mockedSavedTask.setTitle("Write Unit Tests");
        mockedSavedTask.setDescription("Cover all scenarios");
        mockedSavedTask.setCompleted(false);

        when(repository.save(any(Task.class))).thenReturn(mockedSavedTask);

        TaskResponse response = service.create(request, REQUEST_ID);
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Write Unit Tests", response.title());
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("TC-003: Get Recent Tasks - Success")
    void testGetRecentTasks_Success() {

        Task t = new Task();
        t.setId(1L);
        t.setTitle("Test Task");

        when(repository.findTop5ByCompletedFalseOrderByCreatedAtDesc())
                .thenReturn(List.of(t));

        List<TaskResponse> result = service.getRecentTasks(REQUEST_ID);
        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).title());
    }

    @Test
    @DisplayName("TC-005: Mark As Done - Success")
    void testMarkAsDone_Success() {

        Task existing = new Task();
        existing.setId(1L);
        existing.setCompleted(false);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        service.markAsDone(1L, REQUEST_ID);
        assertTrue(existing.isCompleted());
        verify(repository, times(1)).save(existing);
    }

    // NEGATIVE TEST CASES (LOGIC)
    @Test
    @DisplayName("TC-006: Mark As Done - Task Not Found")
    void testMarkAsDone_NotFound() {

        when(repository.findById(99L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.markAsDone(99L, REQUEST_ID));
        assertEquals(REQUEST_ID, ex.getRequestId());
    }

    @Test
    @DisplayName("TC-007: Mark As Done - DB Error")
    void testMarkAsDone_DbError() {

        when(repository.findById(1L)).thenReturn(Optional.of(new Task()));
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("DB Connection Failed"));
        assertThrows(ProcessingException.class, () -> service.markAsDone(1L, REQUEST_ID));
    }

    @Test
    @DisplayName("TC-008: Mark As Done - Invalid Id")
    void testMarkAsDone_InvalidId() {
        assertThrows(ValidationException.class, () -> service.markAsDone(0L, REQUEST_ID));
        verify(repository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("TC-009: Create Task - Blank Title")
    void testCreateTask_BlankTitle_ShouldThrowValidationException() {
        TaskRequest request = new TaskRequest("", "Description");
        assertThrows(ValidationException.class, () -> service.create(request, REQUEST_ID));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("TC-010: Create Task - Null Title")
    void testCreateTask_NullTitle_ShouldThrowValidationException() {
        TaskRequest request = new TaskRequest(null, "Description");
        assertThrows(ValidationException.class, () -> service.create(request, REQUEST_ID));
        verify(repository, never()).save(any(Task.class));
    }

    // UPDATE TEST CASES
    @Test
    @DisplayName("TC-011: Update Task - Success")
    void testUpdateTask_Success() {

        Task existing = new Task();
        existing.setId(2L);
        existing.setTitle("Old Title");
        existing.setDescription("Old Desc");

        Task saved = new Task();
        saved.setId(2L);
        saved.setTitle("New Title");
        saved.setDescription("New Desc");

        when(repository.findById(2L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenReturn(saved);

        TaskResponse resp = service.updateTask(2L, new TaskRequest("New Title", "New Desc"), REQUEST_ID);
        assertNotNull(resp);
        assertEquals(2L, resp.id());
        assertEquals("New Title", resp.title());
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("TC-012: Update Task - Not Found")
    void testUpdateTask_NotFound() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.updateTask(42L, new TaskRequest("Title", "Desc"), REQUEST_ID));

        assertEquals(REQUEST_ID, ex.getRequestId());
    }

    @Test
    @DisplayName("TC-013: Update Task - Invalid Title")
    void testUpdateTask_InvalidTitle() {
        assertThrows(ValidationException.class,
                () -> service.updateTask(3L, new TaskRequest("", "Desc"), REQUEST_ID));

        verify(repository, never()).findById(anyLong());
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("TC-014: Update Task - DB Error")
    void testUpdateTask_DbError() {
        Task existing = new Task();
        existing.setId(5L);
        existing.setTitle("T");

        when(repository.findById(5L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("DB write failed"));

        assertThrows(ProcessingException.class,
                () -> service.updateTask(5L, new TaskRequest("Title", "Desc"), REQUEST_ID));
    }
}