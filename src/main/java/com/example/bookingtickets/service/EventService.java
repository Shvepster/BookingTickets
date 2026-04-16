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
    log.info("Инвалидация кэша мероприятий...");
    eventCache.clear();
  }

  @Transactional
  public EventResponseDto create(EventRequestDto dto) {
    Event event = new Event();
    fillEventData(event, dto);
    invalidateCache();
    return EventMapper.toDto(eventRepository.save(event));
  }

  @Transactional
  public EventResponseDto update(Long id, EventRequestDto dto) {
    Event event = eventRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));
    fillEventData(event, dto);
    invalidateCache();
    return EventMapper.toDto(eventRepository.save(event));
  }

  private void fillEventData(Event event, EventRequestDto dto) {
    event.setTitle(dto.getTitle());
    event.setPrice(dto.getPrice());
    event.setDate(dto.getEventDate());

    Venue venue = venueRepository.findById(dto.getVenueId())
        .orElseThrow(() -> new NotFoundException("Площадка не найдена"));
    event.setVenue(venue);

    if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
      List<Category> cats = categoryRepository.findAllByIdIn(dto.getCategoryIds());
      event.setCategories(new HashSet<>(cats));
    }
  }

  @Transactional
  public void delete(Long id) {
    eventRepository.deleteById(id);
    invalidateCache();
  }

  public EventResponseDto getById(Long id) {
    return eventRepository.findById(id)
        .map(EventMapper::toDto)
        .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));
  }

  public List<EventResponseDto> getAll() {
    return eventRepository.findAll().stream().map(EventMapper::toDto).toList();
  }

  public List<EventResponseDto> searchByTitle(String title) {
    return eventRepository.findByTitleContainingIgnoreCase(title).stream()
        .map(EventMapper::toDto).toList();
  }

  public Page<EventResponseDto> getAllPaged(Pageable pageable) {
    return eventRepository.findAll(pageable).map(EventMapper::toDto);
  }

  public Page<EventResponseDto> searchComplexEvents(
      String venue, String cat, Pageable page, boolean useNative) {

    EventSearchKey key = new EventSearchKey(
        venue,
        cat,
        page.getPageNumber(),
        page.getPageSize(),
        page.getSort().toString(),
        useNative
    );

    if (eventCache.containsKey(key)) {
      log.info("Возврат из кэша для: {}", key);
      return eventCache.get(key);
    }

    Page<Event> eventPage = useNative
        ? eventRepository.findComplexByNative(venue, cat, page)
        : eventRepository.findComplexByJpql(venue, cat, page);

    Page<EventResponseDto> dtoPage = eventPage.map(EventMapper::toDto);
    eventCache.put(key, dtoPage);
    return dtoPage;
  }
}