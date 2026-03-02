package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.mapper.EventMapper;
import com.example.bookingtickets.repository.EventRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<EventResponseDto> getAll() {
        return repository.findAll().stream().map(EventMapper::toDto).collect(Collectors.toList());
    }

    public EventResponseDto getById(Long id) {
        return repository.findById(id).map(EventMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Мероприятие не найдено"));
    }

    public List<EventResponseDto> getByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category).stream()
                .map(EventMapper::toDto).collect(Collectors.toList());
    }
}