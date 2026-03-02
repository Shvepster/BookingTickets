package com.example.bookingtickets.mapper;

import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.model.Event;

public final class EventMapper {
    private EventMapper() {}

    public static EventResponseDto toDto(Event event) {
        if (event == null) return null;
        return new EventResponseDto(
                event.getId(),
                event.getTitle(),
                event.getCategory(),
                event.getPrice() + " BYN"
        );
    }
}