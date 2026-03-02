package com.example.bookingtickets.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EventResponseDto {
    private Long id;
    private String title;
    private String category;
    private String formattedPrice;
}