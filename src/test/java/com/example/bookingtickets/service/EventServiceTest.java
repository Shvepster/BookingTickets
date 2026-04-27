package com.example.bookingtickets.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.bookingtickets.dto.EventRequestDto;
import com.example.bookingtickets.dto.EventResponseDto;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.model.Category;
import com.example.bookingtickets.model.Event;
import com.example.bookingtickets.model.Venue;
import com.example.bookingtickets.repository.CategoryRepository;
import com.example.bookingtickets.repository.EventRepository;
import com.example.bookingtickets.repository.VenueRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

  @Mock private EventRepository eventRepository;
  @Mock private VenueRepository venueRepository;
  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private EventService eventService;

  private Event event;
  private EventRequestDto requestDtoWithCategories;
  private EventRequestDto requestDtoWithoutCategories;

  @BeforeEach
  void setUp() {
    event = new Event();
    event.setId(1L);
    event.setTitle("Concert");
    event.setPrice(100.0);

    requestDtoWithCategories = new EventRequestDto("Concert", 100.0, 1L, List.of(1L), LocalDateTime.now());
    requestDtoWithoutCategories = new EventRequestDto("Standup", 50.0, 1L, null, LocalDateTime.now());
  }

  @Test
  void create_WithCategories_Success() {
    when(venueRepository.findById(1L)).thenReturn(Optional.of(new Venue()));
    when(categoryRepository.findAllByIdIn(any())).thenReturn(List.of(new Category()));
    when(eventRepository.save(any())).thenReturn(event);

    EventResponseDto response = eventService.create(requestDtoWithCategories);
    assertEquals("Concert", response.getTitle());
  }

  @Test
  void create_WithoutCategories_Success() { // Покрывает ветку if(categoryIds == null)
    when(venueRepository.findById(1L)).thenReturn(Optional.of(new Venue()));
    when(eventRepository.save(any())).thenReturn(event);

    EventResponseDto response = eventService.create(requestDtoWithoutCategories);
    assertEquals("Concert", response.getTitle());
    verify(categoryRepository, never()).findAllByIdIn(any());
  }

  @Test
  void create_ThrowsVenueNotFound() {
    when(venueRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> eventService.create(requestDtoWithCategories));
  }

  @Test
  void update_Success() {
    when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
    when(venueRepository.findById(1L)).thenReturn(Optional.of(new Venue()));
    when(eventRepository.save(any())).thenReturn(event);

    EventResponseDto response = eventService.update(1L, requestDtoWithoutCategories);
    assertEquals("Standup", response.getTitle());
  }

  @Test
  void update_ThrowsEventNotFound() {
    when(eventRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> eventService.update(1L, requestDtoWithCategories));
  }

  @Test
  void getById_Success() {
    when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
    assertNotNull(eventService.getById(1L));
  }

  @Test
  void getById_ThrowsNotFound() {
    when(eventRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> eventService.getById(1L));
  }

  @Test
  void delete_Success() {
    eventService.delete(1L);
    verify(eventRepository).deleteById(1L); // А также проверяет инвалидацию кэша!
  }

  @Test
  void getAll_Success() {
    when(eventRepository.findAll()).thenReturn(List.of(event));
    assertEquals(1, eventService.getAll().size());
  }

  @Test
  void searchByTitle_Success() {
    when(eventRepository.findByTitleContainingIgnoreCase("Con")).thenReturn(List.of(event));
    assertEquals(1, eventService.searchByTitle("Con").size());
  }

  @Test
  void getAllPaged_Success() {
    when(eventRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(event)));
    assertEquals(1, eventService.getAllPaged(PageRequest.of(0, 10)).getContent().size());
  }

  @Test
  void searchComplexEvents_Jpql_And_CacheHit() {
    PageRequest pageRequest = PageRequest.of(0, 10);
    when(eventRepository.findComplexByJpql(any(), any(), any())).thenReturn(new PageImpl<>(List.of(event)));

    Page<EventResponseDto> page1 = eventService.searchComplexEvents("V", "C", pageRequest, false);

    Page<EventResponseDto> page2 = eventService.searchComplexEvents("V", "C", pageRequest, false);

    assertEquals(1, page1.getContent().size());
    assertEquals(1, page2.getContent().size());
    verify(eventRepository, times(1)).findComplexByJpql(any(), any(), any());
  }

  @Test
  void searchComplexEvents_NativeQuery() {
    when(eventRepository.findComplexByNative(any(), any(), any())).thenReturn(new PageImpl<>(List.of(event)));

    eventService.searchComplexEvents("V", "C", PageRequest.of(0, 10), true);

    verify(eventRepository, times(1)).findComplexByNative(any(), any(), any());
  }
}