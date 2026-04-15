package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.UserRequestDto;
import com.example.bookingtickets.dto.UserResponseDto;
import com.example.bookingtickets.exception.ErrorResponse;
import com.example.bookingtickets.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "Пользователи", description = "Управление профилями клиентов")
public class UserController {

  private final UserService userService;

  @GetMapping
  @Operation(summary = "Все пользователи")
  public List<UserResponseDto> getAll() {
    return userService.getAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Регистрация пользователя")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Создано"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
      @ApiResponse(responseCode = "409", description = "Email уже занят",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public UserResponseDto create(@Valid @RequestBody UserRequestDto dto) {
    return userService.create(dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Удалить профиль")
  public void delete(@PathVariable @Positive Long id) {
    userService.delete(id);
  }
}