package com.example.bookingtickets.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Единая структура ответа об ошибке")
public record ErrorResponse(
    @Schema(description = "HTTP статус код", example = "400")
    int status,

    @Schema(description = "Тип ошибки", example = "Bad Request")
    String error,

    @Schema(description = "Сообщение", example = "Ошибка валидации")
    String message,

    @Schema(description = "Путь", example = "/api/users")
    String path,

    @Schema(description = "Детальный список ошибок",
        example = "[\"email: Некорректный формат\"]")
    List<String> details,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp
) {
}