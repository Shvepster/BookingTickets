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
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
@Tag(name = "Категории", description = "Управление категориями мероприятий")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  @Operation(summary = "Получить все категории")
  @ApiResponse(responseCode = "200", description = "Список успешно получен",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDto.class))))
  public List<CategoryResponseDto> getAll() {
    return categoryService.getAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить категорию по ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Категория найдена"),
      @ApiResponse(responseCode = "404", description = "Категория не найдена",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public CategoryResponseDto getById(
      @Parameter(description = "ID категории") @PathVariable @Positive Long id
  ) {
    return categoryService.getById(id);
  }

  @GetMapping("/search")
  @Operation(summary = "Поиск категорий по названию")
  public List<CategoryResponseDto> search(@RequestParam String name) {
    return categoryService.searchByName(name);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Создать новую категорию")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Категория создана"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public CategoryResponseDto create(@Valid @RequestBody CategoryRequestDto dto) {
    return categoryService.create(dto);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить категорию")
  public CategoryResponseDto update(
      @PathVariable @Positive Long id,
      @Valid @RequestBody CategoryRequestDto dto
  ) {
    return categoryService.update(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Удалить категорию")
  @ApiResponse(responseCode = "204", description = "Удалено успешно")
  public void delete(@PathVariable @Positive Long id) {
    categoryService.delete(id);
  }
}