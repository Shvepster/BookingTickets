package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.AuthResponseDto;
import com.example.bookingtickets.dto.LoginRequestDto;
import com.example.bookingtickets.dto.UserRequestDto;
import com.example.bookingtickets.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Авторизация", description = "Регистрация и логин")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  @Operation(summary = "Войти по email или username")
  public AuthResponseDto login(@Valid @RequestBody LoginRequestDto request) {
    return authService.login(request);
  }

  @PostMapping("/register")
  @Operation(summary = "Зарегистрировать нового пользователя")
  public AuthResponseDto register(@Valid @RequestBody UserRequestDto request) {
    return authService.register(request);
  }
}