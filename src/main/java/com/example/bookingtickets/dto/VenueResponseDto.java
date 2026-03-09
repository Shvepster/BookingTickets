package com.example.bookingtickets.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VenueResponseDto {
  private Long id;
  private String name;
  private String address;
  private List<Long> eventIds; // Список мероприятий на этой площадке
}