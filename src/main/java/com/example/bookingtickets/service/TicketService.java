package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.TicketRequestDto;
import com.example.bookingtickets.dto.TicketResponseDto;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.mapper.TicketMapper;
import com.example.bookingtickets.model.Event;
import com.example.bookingtickets.model.Ticket;
import com.example.bookingtickets.model.User;
import com.example.bookingtickets.repository.EventRepository;
import com.example.bookingtickets.repository.TicketRepository;
import com.example.bookingtickets.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

  private final TicketRepository ticketRepository;
  private final UserRepository userRepository;
  private final EventRepository eventRepository;

  @Transactional
  public TicketResponseDto create(TicketRequestDto dto) {
    User user = userRepository.findById(dto.getUserId())
        .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    Event event = eventRepository.findById(dto.getEventId())
        .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

    Ticket ticket = new Ticket();
    ticket.setSeatNumber(dto.getSeatNumber());
    ticket.setUser(user);
    ticket.setEvent(event);

    return TicketMapper.toDto(ticketRepository.save(ticket));
  }

  @Transactional
  public void createMultiple(List<TicketRequestDto> dtos) {
    for (TicketRequestDto dto : dtos) {
      create(dto);
    }
  }

  public TicketResponseDto getById(Long id) {
    return ticketRepository.findById(id)
        .map(TicketMapper::toDto)
        .orElseThrow(() -> new NotFoundException("Билет не найден"));
  }

  public List<TicketResponseDto> getAll() {
    return ticketRepository.findAll().stream().map(TicketMapper::toDto).toList();
  }

  public List<TicketResponseDto> searchByUserId(Long userId) {
    return ticketRepository.findByUserId(userId).stream()
        .map(TicketMapper::toDto).toList();
  }

  @Transactional
  public void delete(Long id) {
    ticketRepository.deleteById(id);
  }
}