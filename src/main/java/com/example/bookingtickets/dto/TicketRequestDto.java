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

  @Schema(description = "Номер места или сектора", example = "Сектор А, ряд 10, место 5")
  @NotBlank(message = "Номер места должен быть указан")
  private String seatNumber;

  @Schema(description = "ID покупателя", example = "1")
  @NotNull(message = "ID пользователя должен быть указан")
  private Long userId;

  @Schema(description = "ID мероприятия", example = "1")
  @NotNull(message = "ID мероприятия должен быть указан")
  private Long eventId;
}