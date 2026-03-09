package com.example.bookingtickets.mapper;

import com.example.bookingtickets.dto.TicketResponseDto;
import com.example.bookingtickets.model.Ticket;

public final class TicketMapper {

  private TicketMapper() {}

  public static TicketResponseDto toDto(Ticket ticket) {
    if (ticket == null) {
      return null;
    }
    return new TicketResponseDto(
        ticket.getId(),
        ticket.getSeatNumber(),
        ticket.getUser() != null ? ticket.getUser().getUsername() : "Не указан",
        ticket.getEvent() != null ? ticket.getEvent().getTitle() : "Не указано"
    );
  }
}