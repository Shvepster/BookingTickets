package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.EventRequestDto;
import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.mapper.EventMapper;
import com.example.bookingtickets.model.Category;
import com.example.bookingtickets.model.Event;
import com.example.bookingtickets.model.Venue;
import com.example.bookingtickets.repository.CategoryRepository;
import com.example.bookingtickets.repository.EventRepository;
import com.example.bookingtickets.repository.VenueRepository;
import com.example.bookingtickets.service.cache.EventSearchKey;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

  private final EventRepository eventRepository;
  private final VenueRepository venueRepository;
  private final CategoryRepository categoryRepository;
  private final Map<EventSearchKey, Page<EventResponseDto>> eventCache = new HashMap<>();

  private void invalidateCache() {
    log.info("Очистка In-Memory кэша мероприятий...");
    eventCache.clear();
  }

  @Transactional
  public EventResponseDto create(EventRequestDto dto) {
    Event event = new Event();
    event.setTitle(dto.getTitle());
    event.setPrice(dto.getPrice());
    event.setDate(dto.getEventDate());

    if (dto.getVenueId() != null) {
      Venue venue = venueRepository.findById(dto.getVenueId())
          .orElseThrow(() -> new NotFoundException("Площадка не найдена"));
      event.setVenue(venue);
    }

    if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
      List<Category> categories = categoryRepository.findAllByIdIn(dto.getCategoryIds());
      if (categories.size() != dto.getCategoryIds().size()) {
        throw new NotFoundException("Некоторые категории не найдены");
      }
      event.setCategories(new HashSet<>(categories));
    } else {
      event.setCategories(new HashSet<>());
    }

    Event saved = eventRepository.save(event);
    invalidateCache();
    return EventMapper.toDto(saved);
  }

  @Transactional
  public EventResponseDto update(Long id, EventRequestDto dto) {
    Event event = eventRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

    event.setTitle(dto.getTitle());
    event.setPrice(dto.getPrice());
    event.setDate(dto.getEventDate());

    if (dto.getVenueId() != null) {
      Venue venue = venueRepository.findById(dto.getVenueId())
          .orElseThrow(() -> new NotFoundException("Площадка не найдена"));
      event.setVenue(venue);
    } else {
      event.setVenue(null);
    }

    if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
      List<Category> categories = categoryRepository.findAllByIdIn(dto.getCategoryIds());
      if (categories.size() != dto.getCategoryIds().size()) {
        throw new NotFoundException("Некоторые категории не найдены");
      }
      event.setCategories(new HashSet<>(categories));
    } else {
      event.setCategories(new HashSet<>());
    }

    Event updated = eventRepository.save(event);
    invalidateCache(); // <-- СБРОС КЭША ПРИ ОБНОВЛЕНИИ
    return EventMapper.toDto(updated);
  }

  @Transactional
  public void delete(Long id) {
    eventRepository.deleteById(id);
    invalidateCache(); // <-- СБРОС КЭША ПРИ УДАЛЕНИИ
  }

  public EventResponseDto getById(Long id) {
    return eventRepository.findById(id)
        .map(EventMapper::toDto)
        .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));
  }

  public List<EventResponseDto> getAll() {
    return eventRepository.findAll().stream()
        .map(EventMapper::toDto)
        .toList();
  }

  public List<EventResponseDto> searchByTitle(String title) {
    return eventRepository.findByTitleContainingIgnoreCase(title).stream()
        .map(EventMapper::toDto)
        .toList();
  }

  public Page<EventResponseDto> getAllPaged(Pageable pageable) {
    return eventRepository.findAll(pageable).map(EventMapper::toDto);
  }

  public Page<EventResponseDto> searchComplexEvents(
      String venueName, String categoryName, Pageable pageable, boolean useNative) {

    EventSearchKey key = new EventSearchKey(
        venueName, categoryName, pageable.getPageNumber(), pageable.getPageSize(), useNative
    );

    if (eventCache.containsKey(key)) {
      log.info("Данные найдены в кэше. Возврат из In-Memory индекса для ключа: {}", key);
      return eventCache.get(key);
    }

    log.info("Данные не найдены в кэше. Выполнение запроса к БД (useNative={})...", useNative);
    Page<Event> eventPage;

    if (useNative) {
      eventPage = eventRepository.findComplexByNative(venueName, categoryName, pageable);
    } else {
      eventPage = eventRepository.findComplexByJpql(venueName, categoryName, pageable);
    }

    Page<EventResponseDto> dtoPage = eventPage.map(EventMapper::toDto);

    eventCache.put(key, dtoPage);

    return dtoPage;
  }
}