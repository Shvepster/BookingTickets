package com.example.bookingtickets.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.bookingtickets.dto.TicketRequestDto;
import com.example.bookingtickets.dto.TicketResponseDto;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.model.Event;
import com.example.bookingtickets.model.Ticket;
import com.example.bookingtickets.model.User;
import com.example.bookingtickets.repository.EventRepository;
import com.example.bookingtickets.repository.TicketRepository;
import com.example.bookingtickets.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

  @Mock private TicketRepository ticketRepository;
  @Mock private UserRepository userRepository;
  @Mock private EventRepository eventRepository;

  @InjectMocks private TicketService ticketService;

  private Ticket ticket;
  private TicketRequestDto requestDto;

  @BeforeEach
  void setUp() {
    ticket = new Ticket();
    ticket.setId(1L);
    ticket.setSeatNumber("A1");
    requestDto = new TicketRequestDto("A1", 1L, 1L);
  }

  @Test
  void create_Success() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
    when(eventRepository.findById(1L)).thenReturn(Optional.of(new Event()));
    when(ticketRepository.save(any())).thenReturn(ticket);

    TicketResponseDto response = ticketService.create(requestDto);
    assertEquals("A1", response.getSeatNumber());
  }

  @Test
  void create_ThrowsUserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> ticketService.create(requestDto));
  }

  @Test
  void create_ThrowsEventNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
    when(eventRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> ticketService.create(requestDto));
  }

  @Test
  void createMultiple_Success() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
    when(eventRepository.findById(1L)).thenReturn(Optional.of(new Event()));
    when(ticketRepository.save(any())).thenReturn(ticket);

    ticketService.createMultiple(List.of(requestDto, requestDto));
    verify(ticketRepository, times(2)).save(any()); // 100% покрытие Stream forEach
  }

  @Test
  void getById_Success() {
    when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
    assertNotNull(ticketService.getById(1L));
  }

  @Test
  void getById_ThrowsNotFound() {
    when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> ticketService.getById(1L));
  }

  @Test
  void delete_Success() {
    when(ticketRepository.existsById(1L)).thenReturn(true);
    ticketService.delete(1L);
    verify(ticketRepository).deleteById(1L);
  }

  @Test
  void delete_ThrowsNotFound() {
    when(ticketRepository.existsById(1L)).thenReturn(false);
    assertThrows(NotFoundException.class, () -> ticketService.delete(1L));
  }

  @Test
  void getAll_Success() {
    when(ticketRepository.findAll()).thenReturn(List.of(ticket));
    assertEquals(1, ticketService.getAll().size());
  }

  @Test
  void searchByUserId_Success() {
    when(ticketRepository.findByUserId(1L)).thenReturn(List.of(ticket));
    assertEquals(1, ticketService.searchByUserId(1L).size());
  }
}