package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.VenueRequestDto;
import com.example.bookingtickets.dto.VenueResponseDto;
import com.example.bookingtickets.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
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
  public VenueResponseDto getById(@PathVariable @Positive Long id) {
    return venueService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Создать площадку")
  public VenueResponseDto create(@Valid @RequestBody VenueRequestDto dto) {
    return venueService.create(dto);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить площадку")
  public VenueResponseDto update(
      @PathVariable @Positive Long id,
      @Valid @RequestBody VenueRequestDto dto
  ) {
    return venueService.update(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Удалить площадку")
  public void delete(@PathVariable @Positive Long id) {
    venueService.delete(id);
  }
}