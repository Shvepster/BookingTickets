package com.example.bookingtickets.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
  private String title;
  private Double price;
  private Long venueId;
  private List<Long> categoryIds; // ID категорий
  private LocalDateTime eventDate;
}