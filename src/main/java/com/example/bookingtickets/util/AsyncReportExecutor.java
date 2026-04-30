package com.example.bookingtickets.util;

import com.example.bookingtickets.model.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncReportExecutor {

  private final ReportTaskStorage reportTaskStorage;

  @Async("asyncExecutorPool")
  public void executeReportGeneration(String taskId) {
    log.info("Начата генерация отчета. Задача: {}", taskId);
    try {
      reportTaskStorage.updateStatus(taskId, TaskStatus.IN_PROGRESS);

      Thread.sleep(20000);

      reportTaskStorage.updateStatus(taskId, TaskStatus.DONE);
      log.info("Генерация отчета успешно завершена: {}", taskId);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      reportTaskStorage.updateStatus(taskId, TaskStatus.FAILED);
      log.error("Задача {} была прервана", taskId, exception);
    } catch (Exception exception) {
      reportTaskStorage.updateStatus(taskId, TaskStatus.FAILED);
      log.error("Ошибка при генерации отчета: {}", taskId, exception);
    }
  }
}