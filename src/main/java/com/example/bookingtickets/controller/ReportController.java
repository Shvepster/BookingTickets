package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.ReportTaskResponseDto;
import com.example.bookingtickets.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Отчеты", description = "Асинхронная генерация отчетов")
public class ReportController {

  private final ReportService reportService;

  @PostMapping("/generate")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Operation(summary = "Запустить генерацию", description = "Запускает фоновую задачу")
  public String startReportGeneration() {
    return reportService.startReportGeneration();
  }

  @GetMapping("/{taskId}")
  @Operation(summary = "Статус задачи", description = "Возвращает текущий статус генерации")
  public ReportTaskResponseDto getTaskStatus(@PathVariable String taskId) {
    return reportService.getTaskStatus(taskId);
  }
}