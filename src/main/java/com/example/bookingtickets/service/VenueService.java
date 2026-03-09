package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.VenueRequestDto;
import com.example.bookingtickets.dto.VenueResponseDto;
import com.example.bookingtickets.mapper.VenueMapper;
import com.example.bookingtickets.model.Venue;
import com.example.bookingtickets.repository.VenueRepository;
import java.util.ArrayList;
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
    Venue saved = venueRepository.save(venue);
    return VenueMapper.toDto(saved);
  }

  @Transactional
  public VenueResponseDto update(Long id, VenueRequestDto dto) {
    Venue venue = venueRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Площадка не найдена"));
    venue.setName(dto.getName());
    venue.setAddress(dto.getAddress());
    Venue saved = venueRepository.save(venue);
    return VenueMapper.toDto(saved);
  }

  @Transactional
  public void delete(Long id) {
    venueRepository.deleteById(id);
  }

  public VenueResponseDto getById(Long id) {
    return venueRepository.findById(id)
        .map(VenueMapper::toDto)
        .orElseThrow(() -> new IllegalArgumentException("Площадка не найдена"));
  }

  public List<VenueResponseDto> getAll() {
    List<Venue> venues = venueRepository.findAll();
    List<VenueResponseDto> result = new ArrayList<>();
    for (Venue venue : venues) {
      result.add(VenueMapper.toDto(venue));
    }
    return result;
  }
}