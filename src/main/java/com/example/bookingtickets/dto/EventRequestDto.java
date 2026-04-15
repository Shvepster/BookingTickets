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

  @NotBlank(message = "Название не может быть пустым")
  private String title;

  @NotNull(message = "Цена обязательна")
  @Positive(message = "Цена должна быть > 0")
  private Double price;

  @NotNull(message = "ID площадки обязателен")
  private Long venueId;

  private List<Long> categoryIds;

  @NotNull(message = "Дата обязательна")
  @Future(message = "Дата должна быть в будущем")
  private LocalDateTime eventDate;
}