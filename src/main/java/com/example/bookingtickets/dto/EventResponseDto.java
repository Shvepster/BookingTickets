package com.example.bookingtickets.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для ответа с данными мероприятия.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDto {

  private Long id;

  private String title;

  private String category;

  private String formattedPrice;
}