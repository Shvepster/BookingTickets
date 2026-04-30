package com.example.bookingtickets.util;

import com.example.bookingtickets.model.ReportTask;
import com.example.bookingtickets.model.TaskStatus;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class ReportTaskStorage {

  private final Map<String, ReportTask> tasks = new ConcurrentHashMap<>();

  public String createTask() {
    String taskId = UUID.randomUUID().toString();
    tasks.put(taskId, new ReportTask(taskId));
    return taskId;
  }

  public Optional<ReportTask> getTask(String taskId) {
    return Optional.ofNullable(tasks.get(taskId));
  }

  public void updateStatus(String taskId, TaskStatus status) {
    tasks.computeIfPresent(taskId, (id, task) -> {
      task.setStatus(status);
      task.setUpdatedAt(LocalDateTime.now());
      return task;
    });
  }
}