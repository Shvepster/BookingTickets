package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.service.EventService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    // Требование: @PathVariable
    @GetMapping("/{id}")
    public EventResponseDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Требование: @RequestParam
    @GetMapping
    public List<EventResponseDto> getEvents(@RequestParam(required = false) String category) {
        if (category != null) {
            return service.getByCategory(category);
        }
        return service.getAll();
    }
}