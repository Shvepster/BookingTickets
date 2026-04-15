package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные профиля пользователя в ответе")
public class UserResponseDto {

  @Schema(description = "Уникальный ID пользователя", example = "1")
  private Long id;

  @Schema(description = "Имя пользователя", example = "ivan_ivanov")
  private String username;

  @Schema(description = "Электронная почта", example = "ivan@example.com")
  private String email;

  @Schema(description = "Список ID всех билетов пользователя")
  private List<Long> ticketIds;
}