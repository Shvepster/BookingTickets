package com.example.bookingtickets.dto;

import com.example.bookingtickets.model.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Статус асинхронной задачи генерации отчета")
public class ReportTaskResponseDto {

  @Schema(description = "ID задачи", example = "123e4567-e89b-12d3-a456-426614174000")
  private String taskId;

  @Schema(description = "Статус", example = "IN_PROGRESS")
  private TaskStatus status;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedAt;
}