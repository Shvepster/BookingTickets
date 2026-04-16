package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.VenueRequestDto;
import com.example.bookingtickets.dto.VenueResponseDto;
import com.example.bookingtickets.exception.ErrorResponse;
import com.example.bookingtickets.service.VenueService;
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
@RequestMapping("/api/venues")
@RequiredArgsConstructor
@Validated
@Tag(name = "5. Площадки", description = "Управление площадками")
public class VenueController {

  private final VenueService venueService;

  @GetMapping
  @Operation(summary = "Все площадки")
  public List<VenueResponseDto> getAll() {
    return venueService.getAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Площадка по ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Успешно"),
      @ApiResponse(responseCode = "404", description = "Площадка не найдена",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public VenueResponseDto getById(@PathVariable @Positive Long id) {
    return venueService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Создать площадку")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Создана"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public VenueResponseDto create(@Valid @RequestBody VenueRequestDto dto) {
    return venueService.create(dto);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить площадку")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Обновлено"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Площадка не найдена",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public VenueResponseDto update(
      @PathVariable @Positive Long id,
      @Valid @RequestBody VenueRequestDto dto
  ) {
    return venueService.update(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Удалить площадку")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Удалено"),
      @ApiResponse(responseCode = "404", description = "Площадка не найдена",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public void delete(@PathVariable @Positive Long id) {
    venueService.delete(id);
  }
}