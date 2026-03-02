package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.service.EventService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с мероприятиями.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

  private final EventService service;

  /**
   * Конструктор контроллера.
   *
   * @param service сервис мероприятий
   */
  public EventController(EventService service) {
    this.service = service;
  }

  /**
   * Получение мероприятия по ID.
   *
   * @param id идентификатор
   * @return DTO мероприятия
   */
  @GetMapping("/{id}")
  public EventResponseDto getById(@PathVariable Long id) {
    return service.getById(id);
  }

  /**
   * Получение списка мероприятий с фильтрацией.
   *
   * @param category категория (опционально)
   * @return список DTO мероприятий
   */
  @GetMapping
  public List<EventResponseDto> getEvents(@RequestParam(required = false) String category) {
    if (category != null) {
      return service.getByCategory(category);
    }
    return service.getAll();
  }
}