package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о площадке в ответе")
public class VenueResponseDto {

  @Schema(description = "Уникальный ID площадки", example = "1")
  private Long id;

  @Schema(description = "Название площадки", example = "Минск-Арена")
  private String name;

  @Schema(description = "Адрес", example = "пр. Победителей, 111")
  private String address;

  @Schema(description = "Список ID мероприятий на этой площадке")
  private List<Long> eventIds;
}