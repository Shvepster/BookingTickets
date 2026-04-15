package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.EventRequestDto;
import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.exception.ErrorResponse;
import com.example.bookingtickets.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Validated
@Tag(name = "2. Мероприятия", description = "Афиша и поиск")
public class EventController {

  private final EventService eventService;

  @GetMapping("/paged")
  @Operation(summary = "Мероприятия с пагинацией")
  public Page<EventResponseDto> getPaged(Pageable pageable) {
    return eventService.getAllPaged(pageable);
  }

  @GetMapping("/search-complex")
  @Operation(summary = "Сложный поиск (Блок 3)",
      description = "Поиск по площадке и категории. Использует кэш.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Успешно"),
      @ApiResponse(responseCode = "400", description = "Ошибка параметров",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public Page<EventResponseDto> searchComplex(
      @RequestParam String venueName,
      @RequestParam String categoryName,
      @Parameter(description = "Использовать Native SQL?")
      @RequestParam(defaultValue = "false") boolean useNative,
      Pageable pageable
  ) {
    return eventService.searchComplexEvents(venueName, categoryName, pageable, useNative);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Событие по ID")
  public EventResponseDto getById(@PathVariable @Positive Long id) {
    return eventService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Создать мероприятие")
  public EventResponseDto create(@Valid @RequestBody EventRequestDto dto) {
    return eventService.create(dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Удалить мероприятие")
  public void delete(@PathVariable @Positive Long id) {
    eventService.delete(id);
  }
}