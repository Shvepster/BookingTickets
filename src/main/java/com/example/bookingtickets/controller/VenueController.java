package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.VenueRequestDto;
import com.example.bookingtickets.dto.VenueResponseDto;
import com.example.bookingtickets.service.VenueService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

  private final VenueService venueService;

  @GetMapping
  public List<VenueResponseDto> getAll() {
    return venueService.getAll();
  }

  @GetMapping("/{id}")
  public VenueResponseDto getById(@PathVariable Long id) {
    return venueService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public VenueResponseDto create(@RequestBody VenueRequestDto dto) {
    return venueService.create(dto);
  }

  @PutMapping("/{id}")
  public VenueResponseDto update(@PathVariable Long id, @RequestBody VenueRequestDto dto) {
    return venueService.update(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    venueService.delete(id);
  }
}