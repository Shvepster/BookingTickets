package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для создания мероприятия")
public class EventRequestDto {

  @Schema(description = "Заголовок мероприятия", example = "Концерт группы Scorpions")
  @NotBlank(message = "Название мероприятия не может быть пустым")
  private String title;

  @Schema(description = "Цена билета", example = "150.0")
  @NotNull(message = "Цена должна быть указана")
  @Positive(message = "Цена должна быть больше нуля")
  private Double price;

  @Schema(description = "ID площадки проведения", example = "1")
  @NotNull(message = "ID площадки должен быть указан")
  private Long venueId;

  @Schema(description = "Список ID категорий", example = "[1, 3]")
  private List<Long> categoryIds;

  @Schema(description = "Дата и время начала", example = "2026-12-31T20:00:00")
  @NotNull(message = "Дата мероприятия должна быть указана")
  @Future(message = "Мероприятие не может быть в прошлом")
  private LocalDateTime eventDate;
}