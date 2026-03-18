package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.TicketRequestDto;
import com.example.bookingtickets.dto.TicketResponseDto;
import com.example.bookingtickets.service.TicketService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

  private final TicketService ticketService;

  @GetMapping
  public List<TicketResponseDto> getAll() {
    return ticketService.getAll();
  }

  @GetMapping("/{id}")
  public TicketResponseDto getById(@PathVariable Long id) {
    return ticketService.getById(id);
  }

  @GetMapping("/search")
  public List<TicketResponseDto> search(@RequestParam Long userId) {
    return ticketService.searchByUserId(userId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TicketResponseDto create(@RequestBody TicketRequestDto dto) {
    return ticketService.create(dto);
  }

  @PutMapping("/{id}")
  public TicketResponseDto update(@PathVariable Long id, @RequestBody TicketRequestDto dto) {
    return ticketService.update(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    ticketService.delete(id);
  }

  @PostMapping("/bulk")
  @ResponseStatus(HttpStatus.CREATED)
  public void createBulk(@RequestBody List<TicketRequestDto> dtos) {
    ticketService.createMultiple(dtos);
  }
}