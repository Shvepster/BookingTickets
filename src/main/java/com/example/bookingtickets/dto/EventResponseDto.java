package com.example.bookingtickets.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class EventResponseDto {
  private Long id;
  private String title;
  private String formattedPrice; // Например "50 BYN"
  private String venueName;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime eventDate;
  private List<String> categories; // Названия категорий
}