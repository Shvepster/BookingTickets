package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.TicketRequestDto;
import com.example.bookingtickets.dto.TicketResponseDto;
import com.example.bookingtickets.exception.ErrorResponse;
import com.example.bookingtickets.service.TicketService;
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
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Validated
@Tag(name = "Билеты", description = "Бронирование и покупка билетов")
public class TicketController {

  private final TicketService ticketService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Купить одиночный билет")
  public TicketResponseDto create(@Valid @RequestBody TicketRequestDto dto) {
    return ticketService.create(dto);
  }

  @PostMapping("/bulk")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Массовая покупка (Транзакционно)", description = "Откатывает всё при ошибке любого билета")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Все билеты куплены"),
      @ApiResponse(responseCode = "400", description = "Ошибка в данных"),
      @ApiResponse(responseCode = "404", description = "Пользователь или событие не найдены")
  })
  public void createBulk(@RequestBody List<@Valid TicketRequestDto> dtos) {
    ticketService.createMultiple(dtos);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Отменить бронь")
  public void delete(@PathVariable @Positive Long id) {
    ticketService.delete(id);
  }
}