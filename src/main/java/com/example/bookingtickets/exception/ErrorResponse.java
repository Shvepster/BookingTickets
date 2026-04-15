package com.example.bookingtickets.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
    int status,
    String error,
    String message,
    String path,
    List<String> details,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp
) {
}