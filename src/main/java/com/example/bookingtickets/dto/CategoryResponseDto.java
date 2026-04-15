package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о категории в ответе")
public class CategoryResponseDto {

  @Schema(description = "Уникальный ID", example = "1")
  private Long id;

  @Schema(description = "Название категории", example = "Рок-музыка")
  private String name;
}