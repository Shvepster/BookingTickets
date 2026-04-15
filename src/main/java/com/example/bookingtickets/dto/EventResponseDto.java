package com.example.bookingtickets.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ с данными мероприятия")
public class EventResponseDto {
  @Schema(description = "ID мероприятия", example = "1")
  private Long id;

  @Schema(description = "Заголовок", example = "Концерт Scorpions")
  private String title;

  @Schema(description = "Форматированная цена", example = "150.0 BYN")
  private String formattedPrice;

  @Schema(description = "Название площадки", example = "Минск-Арена")
  private String venueName;

  @Schema(description = "Дата проведения", example = "2025-12-31 20:00")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime eventDate;

  @Schema(description = "Список категорий", example = "[\"Рок\", \"Шоу\"]")
  private List<String> categories;
}