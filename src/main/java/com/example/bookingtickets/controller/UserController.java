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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "4. Пользователи", description = "Управление профилями клиентов")
public class UserController {

  private final UserService userService;

  @GetMapping
  @Operation(summary = "Список всех пользователей")
  public List<UserResponseDto> getAll() {
    return userService.getAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Пользователь по ID")
  public UserResponseDto getById(@PathVariable @Positive Long id) {
    return userService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Регистрация")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Создан"),
      @ApiResponse(responseCode = "409", description = "Email занят",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public UserResponseDto create(@Valid @RequestBody UserRequestDto dto) {
    return userService.create(dto);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить профиль")
  public UserResponseDto update(
      @PathVariable @Positive Long id,
      @Valid @RequestBody UserRequestDto dto
  ) {
    return userService.update(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Удалить профиль")
  public void delete(@PathVariable @Positive Long id) {
    userService.delete(id);
  }
}