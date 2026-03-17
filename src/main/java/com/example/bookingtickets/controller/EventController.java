package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.EventRequestDto;
import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.service.EventService;
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
import org.springframework.web.bind.annotation.RequestParam; // Добавлен импорт
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  @GetMapping
  public List<EventResponseDto> getAll() {
    return eventService.getAll();
  }

  @GetMapping("/{id}")
  public EventResponseDto getById(@PathVariable Long id) {
    return eventService.getById(id);
  }

  @GetMapping("/search")
  public List<EventResponseDto> search(@RequestParam String title) {
    return eventService.searchByTitle(title);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EventResponseDto create(@RequestBody EventRequestDto dto) {
    return eventService.create(dto);
  }

  @PutMapping("/{id}")
  public EventResponseDto update(@PathVariable Long id, @RequestBody EventRequestDto dto) {
    return eventService.update(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    eventService.delete(id);
  }
}