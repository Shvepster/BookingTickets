package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.TicketRequestDto;
import com.example.bookingtickets.dto.TicketResponseDto;
import com.example.bookingtickets.mapper.TicketMapper;
import com.example.bookingtickets.model.Event;
import com.example.bookingtickets.model.Ticket;
import com.example.bookingtickets.model.User;
import com.example.bookingtickets.repository.EventRepository;
import com.example.bookingtickets.repository.TicketRepository;
import com.example.bookingtickets.repository.UserRepository;
import java.util.ArrayList;
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

  private TicketResponseDto saveTicketInternal(TicketRequestDto dto) {
    User user = userRepository.findById(dto.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    Event event = eventRepository.findById(dto.getEventId())
        .orElseThrow(() -> new IllegalArgumentException("Мероприятие не найдено"));

    Ticket ticket = new Ticket();
    ticket.setSeatNumber(dto.getSeatNumber());
    ticket.setUser(user);
    ticket.setEvent(event);

    Ticket saved = ticketRepository.save(ticket);
    return TicketMapper.toDto(saved);
  }

  @Transactional
  public TicketResponseDto update(Long id, TicketRequestDto dto) {
    Ticket ticket = ticketRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Билет не найден"));
    ticket.setSeatNumber(dto.getSeatNumber());

    if (dto.getUserId() != null) {
      User user = userRepository.findById(dto.getUserId())
          .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
      ticket.setUser(user);
    }

    if (dto.getEventId() != null) {
      Event event = eventRepository.findById(dto.getEventId())
          .orElseThrow(() -> new IllegalArgumentException("Мероприятие не найдено"));
      ticket.setEvent(event);
    }

    Ticket saved = ticketRepository.save(ticket);
    return TicketMapper.toDto(saved);
  }

  @Transactional
  public void delete(Long id) {
    ticketRepository.deleteById(id);
  }

  public TicketResponseDto getById(Long id) {
    return ticketRepository.findById(id)
        .map(TicketMapper::toDto)
        .orElseThrow(() -> new IllegalArgumentException("Билет не найден"));
  }

  @Transactional
  public TicketResponseDto create(TicketRequestDto dto) {
    return saveTicketInternal(dto); // Вызываем приватный метод
  }

  public List<TicketResponseDto> getAll() {
    List<Ticket> tickets = ticketRepository.findAll();
    List<TicketResponseDto> result = new ArrayList<>();
    for (Ticket ticket : tickets) {
      result.add(TicketMapper.toDto(ticket));
    }
    return result;
  }

  public List<TicketResponseDto> searchByUserId(Long userId) {
    return ticketRepository.findByUserId(userId).stream()
        .map(TicketMapper::toDto)
        .toList();
  }

  @Transactional
  public void createMultiple(List<TicketRequestDto> dtos) {
    for (TicketRequestDto dto : dtos) {
      saveTicketInternal(dto);
    }
  }
}