package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о купленном билете в ответе")
public class TicketResponseDto {

  @Schema(description = "ID билета", example = "10")
  private Long id;

  @Schema(description = "Место в зале", example = "Ряд 5, место 12")
  private String seatNumber;

  @Schema(description = "Имя владельца билета", example = "admin")
  private String username;

  @Schema(description = "Название мероприятия", example = "Концерт Scorpions")
  private String eventTitle;
}