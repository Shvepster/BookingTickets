package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.VenueRequestDto;
import com.example.bookingtickets.dto.VenueResponseDto;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.mapper.VenueMapper;
import com.example.bookingtickets.model.Venue;
import com.example.bookingtickets.repository.VenueRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueService {

  private final VenueRepository venueRepository;

  @Transactional
  public VenueResponseDto create(VenueRequestDto dto) {
    Venue venue = new Venue();
    venue.setName(dto.getName());
    venue.setAddress(dto.getAddress());
    return VenueMapper.toDto(venueRepository.save(venue));
  }

  @Transactional
  public VenueResponseDto update(Long id, VenueRequestDto dto) {
    Venue venue = venueRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Площадка не найдена"));
    venue.setName(dto.getName());
    venue.setAddress(dto.getAddress());
    return VenueMapper.toDto(venueRepository.save(venue));
  }

  @Transactional
  public void delete(Long id) {
    venueRepository.deleteById(id);
  }

  public VenueResponseDto getById(Long id) {
    return venueRepository.findById(id)
        .map(VenueMapper::toDto)
        .orElseThrow(() -> new NotFoundException("Площадка не найдена"));
  }

  public List<VenueResponseDto> getAll() {
    return venueRepository.findAll().stream().map(VenueMapper::toDto).toList();
  }

  public List<VenueResponseDto> searchByName(String name) {
    return venueRepository.findByNameContainingIgnoreCase(name).stream()
        .map(VenueMapper::toDto).toList();
  }
}