package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ с данными площадки")
public class VenueResponseDto {
  @Schema(description = "ID площадки", example = "1")
  private Long id;

  @Schema(description = "Название", example = "Минск-Арена")
  private String name;

  @Schema(description = "Адрес", example = "пр. Победителей 111")
  private String address;

  @Schema(description = "Список ID запланированных мероприятий", example = "[1, 5, 8]")
  private List<Long> eventIds;
}