package com.example.bookingtickets.mapper;

import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.model.Event;

/**
 * Маппер для преобразования сущности Event в DTO.
 */
public final class EventMapper {

  private EventMapper() {
  }

  /**
   * Преобразует сущность Event в EventResponseDto.
   *
   * @param event сущность мероприятия
   * @return объект DTO
   */
  public static EventResponseDto toDto(Event event) {
    if (event == null) {
      return null;
    }
    return new EventResponseDto(
        event.getId(),
        event.getTitle(),
        event.getCategory(),
        event.getPrice() + " ₽"
    );
  }
}