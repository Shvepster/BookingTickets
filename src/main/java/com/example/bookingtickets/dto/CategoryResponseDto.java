package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ с данными категории")
public class CategoryResponseDto {
  @Schema(description = "ID категории", example = "1")
  private Long id;

  @Schema(description = "Название категории", example = "Рок-музыка")
  private String name;
}