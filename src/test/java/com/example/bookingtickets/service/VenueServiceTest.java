package com.example.bookingtickets.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.bookingtickets.dto.VenueRequestDto;
import com.example.bookingtickets.dto.VenueResponseDto;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.model.Venue;
import com.example.bookingtickets.repository.VenueRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

  @Mock private VenueRepository venueRepository;
  @InjectMocks private VenueService venueService;

  private Venue venue;
  private VenueRequestDto requestDto;

  @BeforeEach
  void setUp() {
    venue = new Venue();
    venue.setId(1L);
    venue.setName("Arena");
    requestDto = new VenueRequestDto("Arena", "Address 1");
  }

  @Test
  void create_Success() {
    when(venueRepository.save(any())).thenReturn(venue);
    VenueResponseDto response = venueService.create(requestDto);
    assertEquals("Arena", response.getName());
  }

  @Test
  void update_Success() {
    when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));
    when(venueRepository.save(any())).thenReturn(venue);
    assertEquals("Arena", venueService.update(1L, requestDto).getName());
  }

  @Test
  void update_ThrowsNotFound() {
    when(venueRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> venueService.update(1L, requestDto));
  }

  @Test
  void getById_Success() {
    when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));
    assertNotNull(venueService.getById(1L));
  }

  @Test
  void getById_ThrowsNotFound() {
    when(venueRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> venueService.getById(1L));
  }

  @Test
  void getAll_Success() {
    when(venueRepository.findAll()).thenReturn(List.of(venue));
    assertEquals(1, venueService.getAll().size());
  }

  @Test
  void searchByName_Success() {
    when(venueRepository.findByNameContainingIgnoreCase("Ar")).thenReturn(List.of(venue));
    assertEquals(1, venueService.searchByName("Ar").size());
  }

  @Test
  void delete_Success() {
    venueService.delete(1L);
    verify(venueRepository).deleteById(1L);
  }
}