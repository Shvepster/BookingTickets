package com.example.bookingtickets.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.bookingtickets.dto.ReportTaskResponseDto;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.model.ReportTask;
import com.example.bookingtickets.model.TaskStatus;
import com.example.bookingtickets.util.AsyncReportExecutor;
import com.example.bookingtickets.util.ReportTaskStorage;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  @Mock private ReportTaskStorage reportTaskStorage;
  @Mock private AsyncReportExecutor asyncReportExecutor;

  @InjectMocks private ReportService reportService;

  @Test
  void startReportGeneration_Success() {
    when(reportTaskStorage.createTask()).thenReturn("task-id");

    String resultId = reportService.startReportGeneration();

    assertEquals("task-id", resultId);
    verify(reportTaskStorage).createTask();
    verify(asyncReportExecutor).executeReportGeneration("task-id");
  }

  @Test
  void getTaskStatus_Success() {
    ReportTask task = new ReportTask("task-1");
    task.setStatus(TaskStatus.IN_PROGRESS);
    when(reportTaskStorage.getTask("task-1")).thenReturn(Optional.of(task));

    ReportTaskResponseDto response = reportService.getTaskStatus("task-1");

    assertEquals("task-1", response.getTaskId());
    assertEquals(TaskStatus.IN_PROGRESS, response.getStatus());
  }

  @Test
  void getTaskStatus_ThrowsNotFound() {
    when(reportTaskStorage.getTask("unknown")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> reportService.getTaskStatus("unknown"));
  }
}