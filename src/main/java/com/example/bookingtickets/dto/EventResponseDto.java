package com.example.bookingtickets.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Полная информация о мероприятии в ответе")
public class EventResponseDto {

  @Schema(description = "ID мероприятия", example = "1")
  private Long id;

  @Schema(description = "Заголовок", example = "Концерт Scorpions")
  private String title;

  @Schema(description = "Цена с указанием валюты", example = "150.0 BYN")
  private String formattedPrice;

  @Schema(description = "Название площадки проведения", example = "Минск-Арена")
  private String venueName;

  @Schema(description = "Дата и время начала", example = "2026-03-29 12:45")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime eventDate;

  @Schema(description = "Список названий категорий", example = "[\"Рок\", \"Шоу\"]")
  private List<String> categories;
}