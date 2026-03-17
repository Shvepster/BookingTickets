package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.EventRequestDto;
import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.mapper.EventMapper;
import com.example.bookingtickets.model.Category;
import com.example.bookingtickets.model.Event;
import com.example.bookingtickets.model.Venue;
import com.example.bookingtickets.repository.CategoryRepository;
import com.example.bookingtickets.repository.EventRepository;
import com.example.bookingtickets.repository.VenueRepository;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

  private final EventRepository eventRepository;
  private final VenueRepository venueRepository;
  private final CategoryRepository categoryRepository;

  @Transactional
  public EventResponseDto create(EventRequestDto dto) {
    Event event = new Event();
    event.setTitle(dto.getTitle());
    event.setPrice(dto.getPrice());
    event.setDate(dto.getEventDate());

    if (dto.getVenueId() != null) {
      Venue venue = venueRepository.findById(dto.getVenueId())
          .orElseThrow(() -> new IllegalArgumentException("Площадка не найдена"));
      event.setVenue(venue);
    }

    if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
      List<Category> categories = categoryRepository.findAllByIdIn(dto.getCategoryIds());
      if (categories.size() != dto.getCategoryIds().size()) {
        throw new IllegalArgumentException("Некоторые категории не найдены");
      }
      event.setCategories(new HashSet<>(categories));
    } else {
      event.setCategories(new HashSet<>());
    }

    Event saved = eventRepository.save(event);
    return EventMapper.toDto(saved);
  }

  @Transactional
  public EventResponseDto update(Long id, EventRequestDto dto) {
    Event event = eventRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Мероприятие не найдено"));

    event.setTitle(dto.getTitle());
    event.setPrice(dto.getPrice());
    event.setDate(dto.getEventDate());

    if (dto.getVenueId() != null) {
      Venue venue = venueRepository.findById(dto.getVenueId())
          .orElseThrow(() -> new IllegalArgumentException("Площадка не найдена"));
      event.setVenue(venue);
    } else {
      event.setVenue(null);
    }

    if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
      List<Category> categories = categoryRepository.findAllByIdIn(dto.getCategoryIds());
      if (categories.size() != dto.getCategoryIds().size()) {
        throw new IllegalArgumentException("Некоторые категории не найдены");
      }
      event.setCategories(new HashSet<>(categories));
    } else {
      event.setCategories(new HashSet<>());
    }

    Event updated = eventRepository.save(event);
    return EventMapper.toDto(updated);
  }

  @Transactional
  public void delete(Long id) {
    eventRepository.deleteById(id);
  }

  public EventResponseDto getById(Long id) {
    return eventRepository.findById(id)
        .map(EventMapper::toDto)
        .orElseThrow(() -> new IllegalArgumentException("Мероприятие не найдено"));
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
}