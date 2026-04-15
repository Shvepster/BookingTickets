package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для регистрации пользователя")
public class UserRequestDto {

  @Schema(description = "Уникальное имя пользователя", example = "ivan_ivanov")
  @NotBlank(message = "Имя пользователя не должно быть пустым")
  private String username;

  @Schema(description = "Электронная почта", example = "ivan@example.com")
  @NotBlank(message = "Email не должен быть пустым")
  @Email(message = "Некорректный формат email адреса")
  private String email;
}