package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.mapper.EventMapper;
import com.example.bookingtickets.repository.EventRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления мероприятиями.
 */
@Service
public class EventService {

  private final EventRepository repository;

  /**
   * Конструктор сервиса.
   *
   * @param repository репозиторий мероприятий
   */
  public EventService(EventRepository repository) {
    this.repository = repository;
  }

  /**
   * Возвращает список всех мероприятий.
   *
   * @return список DTO мероприятий
   */
  public List<EventResponseDto> getAll() {
    return repository.findAll().stream()
        .map(EventMapper::toDto)
        .toList(); // Исправлено здесь (вместо .collect(Collectors.toList()))
  }

  /**
   * Возвращает мероприятие по идентификатору.
   *
   * @param id идентификатор мероприятия
   * @return DTO мероприятия
   */
  public EventResponseDto getById(Long id) {
    return repository.findById(id)
        .map(EventMapper::toDto)
        .orElseThrow(() -> new IllegalArgumentException("Мероприятие не найдено"));
  }

  /**
   * Возвращает список мероприятий по категории.
   *
   * @param category категория
   * @return список DTO мероприятий
   */
  public List<EventResponseDto> getByCategory(String category) {
    return repository.findByCategoryIgnoreCase(category).stream()
        .map(EventMapper::toDto)
        .toList(); // Исправлено здесь (вместо .collect(Collectors.toList()))
  }
}