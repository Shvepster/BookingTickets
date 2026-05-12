package com.example.bookingtickets.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
  @NotBlank(message = "Логин (username или email) обязателен")
  private String login;

  @NotBlank(message = "Пароль обязателен")
  private String password;
}