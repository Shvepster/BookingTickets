package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ с профилем пользователя")
public class UserResponseDto {
  @Schema(description = "ID пользователя", example = "1")
  private Long id;

  @Schema(description = "Имя пользователя", example = "ivan_ivanov")
  private String username;

  @Schema(description = "Email", example = "ivan@example.com")
  private String email;

  @Schema(description = "Список ID билетов пользователя", example = "[10, 11, 12]")
  private List<Long> ticketIds;
}