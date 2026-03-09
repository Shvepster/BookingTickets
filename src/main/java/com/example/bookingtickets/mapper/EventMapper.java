package com.example.bookingtickets.mapper;

import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.model.Category;
import com.example.bookingtickets.model.Event;
import java.util.List;
import java.util.stream.Collectors;

public final class EventMapper {

  private EventMapper() {}

  public static EventResponseDto toDto(Event event) {
    if (event == null) {
      return null;
    }
    return new EventResponseDto(
        event.getId(),
        event.getTitle(),
        event.getPrice() + " BYN",
        event.getVenue() != null ? event.getVenue().getName() : "Площадка не указана",
        event.getEventDate(),
        event.getCategories() == null ? null :
            event.getCategories().stream().map(Category::getName).collect(Collectors.toList())
    );
  }
}