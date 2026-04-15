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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
@Validated
@Tag(name = "Площадки", description = "Управление местами проведения событий")
public class VenueController {

  private final VenueService venueService;

  @GetMapping
  @Operation(summary = "Список всех площадок")
  public List<VenueResponseDto> getAll() {
    return venueService.getAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Площадка по ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Найдено"),
      @ApiResponse(responseCode = "404", description = "Площадка не найдена",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public VenueResponseDto getById(@PathVariable @Positive Long id) {
    return venueService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Добавить новую площадку")
  public VenueResponseDto create(@Valid @RequestBody VenueRequestDto dto) {
    return venueService.create(dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Удалить площадку")
  public void delete(@PathVariable @Positive Long id) {
    venueService.delete(id);
  }
}