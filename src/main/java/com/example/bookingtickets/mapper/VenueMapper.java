package com.example.bookingtickets.mapper;

import com.example.bookingtickets.dto.VenueResponseDto;
import com.example.bookingtickets.model.Venue;
import java.util.stream.Collectors;

public final class VenueMapper {

  private VenueMapper() {}

  public static VenueResponseDto toDto(Venue venue) {
    if (venue == null) {
      return null;
    }
    return new VenueResponseDto(
        venue.getId(),
        venue.getName(),
        venue.getAddress(),
        venue.getEvents() == null ? null :
            venue.getEvents().stream().map(e -> e.getId()).collect(Collectors.toList())
    );
  }
}