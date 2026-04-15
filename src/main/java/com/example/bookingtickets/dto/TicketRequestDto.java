package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для покупки билета")
public class TicketRequestDto {

  @Schema(description = "Номер места", example = "Ряд 5, место 10")
  @NotBlank(message = "Место должно быть указано")
  private String seatNumber;

  @Schema(description = "ID пользователя", example = "1")
  @NotNull(message = "ID пользователя обязателен")
  private Long userId;

  @Schema(description = "ID мероприятия", example = "1")
  @NotNull(message = "ID мероприятия обязателен")
  private Long eventId;
}