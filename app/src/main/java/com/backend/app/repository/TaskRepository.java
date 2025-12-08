package com.backend.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.app.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
     List<Task> findTop5ByCompletedFalseOrderByCreatedAtDesc();
}
