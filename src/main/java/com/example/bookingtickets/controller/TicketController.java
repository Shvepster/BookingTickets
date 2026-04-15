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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Validated
@Tag(name = "3. Билеты", description = "Покупка и Bulk-операции")
public class TicketController {

  private final TicketService ticketService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Купить билет")
  public TicketResponseDto create(@Valid @RequestBody TicketRequestDto dto) {
    return ticketService.create(dto);
  }

  @PostMapping("/bulk")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Массовая покупка (Транзакционно)",
      description = "Откатывает всё при ошибке любого билета в списке.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Успешно"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public void createBulk(@RequestBody List<@Valid TicketRequestDto> dtos) {
    ticketService.createMultiple(dtos);
  }

  @GetMapping("/{id}")
  public TicketResponseDto getById(@PathVariable @Positive Long id) {
    return ticketService.getById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable @Positive Long id) {
    ticketService.delete(id);
  }
}