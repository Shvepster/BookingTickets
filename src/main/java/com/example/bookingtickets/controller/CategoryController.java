package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.CategoryRequestDto;
import com.example.bookingtickets.dto.CategoryResponseDto;
import com.example.bookingtickets.exception.ErrorResponse;
import com.example.bookingtickets.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RequestParam;
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
  @Operation(summary = "Все категории")
  public List<CategoryResponseDto> getAll() {
    return categoryService.getAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Категория по ID")
  public CategoryResponseDto getById(@PathVariable @Positive Long id) {
    return categoryService.getById(id);
  }

  @GetMapping("/search")
  @Operation(summary = "Поиск по названию")
  public List<CategoryResponseDto> search(@RequestParam @NotBlank String name) {
    return categoryService.searchByName(name);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Создать категорию")
  public CategoryResponseDto create(@Valid @RequestBody CategoryRequestDto dto) {
    return categoryService.create(dto);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить категорию")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Успешно"),
      @ApiResponse(responseCode = "404", description = "Не найдена")
  })
  public CategoryResponseDto update(
      @PathVariable @Positive Long id,
      @Valid @RequestBody CategoryRequestDto dto
  ) {
    return categoryService.update(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Удалить категорию")
  public void delete(@PathVariable @Positive Long id) {
    categoryService.delete(id);
  }
}