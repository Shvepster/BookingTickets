package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ с данными купленного билета")
public class TicketResponseDto {
  @Schema(description = "ID билета", example = "123")
  private Long id;

  @Schema(description = "Место", example = "Ряд 5, место 10")
  private String seatNumber;

  @Schema(description = "Имя владельца", example = "ivan_ivanov")
  private String username;

  @Schema(description = "Название события", example = "Концерт Scorpions")
  private String eventTitle;
}