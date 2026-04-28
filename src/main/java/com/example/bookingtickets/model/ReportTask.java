package com.example.bookingtickets.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportTask {

  private String id;
  private TaskStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public ReportTask(String id) {
    this.id = id;
    this.status = TaskStatus.PENDING;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = this.createdAt;
  }
}