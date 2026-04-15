package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для создания или обновления площадки")
public class VenueRequestDto {

  @Schema(description = "Название площадки", example = "Минск-Арена")
  @NotBlank(message = "Название площадки не может быть пустым")
  private String name;

  @Schema(description = "Физический адрес", example = "пр. Победителей, 111")
  @NotBlank(message = "Адрес не может быть пустым")
  private String address;
}