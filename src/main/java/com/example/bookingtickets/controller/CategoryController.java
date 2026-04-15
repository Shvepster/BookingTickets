package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.CategoryRequestDto;
import com.example.bookingtickets.dto.CategoryResponseDto;
import com.example.bookingtickets.exception.ErrorResponse;
import com.example.bookingtickets.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
@Tag(name = "1. Категории", description = "Управление категориями мероприятий")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  @Operation(summary = "Получить все категории",
      description = "Возвращает полный список всех доступных категорий")
  @ApiResponse(responseCode = "200", description = "Список успешно получен",
      content = @Content(array = @ArraySchema(
          schema = @Schema(implementation = CategoryResponseDto.class))))
  public List<CategoryResponseDto> getAll() {
    return categoryService.getAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить категорию по ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Категория найдена"),
      @ApiResponse(responseCode = "400", description = "Некорректный ID",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Категория не найдена",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public CategoryResponseDto getById(
      @Parameter(description = "ID категории", example = "1")
      @PathVariable @Positive(message = "ID должен быть положительным") Long id
  ) {
    return categoryService.getById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Удалить категорию")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Удалено успешно"),
      @ApiResponse(responseCode = "409", description = "Конфликт: категория связана",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public void delete(@PathVariable @Positive Long id) {
    categoryService.delete(id);
  }
}