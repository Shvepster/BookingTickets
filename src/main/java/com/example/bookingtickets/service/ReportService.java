package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.ReportTaskResponseDto;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.util.AsyncReportExecutor;
import com.example.bookingtickets.util.ReportTaskStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final ReportTaskStorage reportTaskStorage;
  private final AsyncReportExecutor asyncReportExecutor;

  public String startReportGeneration() {
    String taskId = reportTaskStorage.createTask();
    asyncReportExecutor.executeReportGeneration(taskId);
    return taskId;
  }

  public ReportTaskResponseDto getTaskStatus(String taskId) {
    return reportTaskStorage.getTask(taskId)
        .map(task -> new ReportTaskResponseDto(
            task.getId(),
            task.getStatus(),
            task.getCreatedAt(),
            task.getUpdatedAt()
        ))
        .orElseThrow(() -> new NotFoundException("Задача не найдена"));
  }
}