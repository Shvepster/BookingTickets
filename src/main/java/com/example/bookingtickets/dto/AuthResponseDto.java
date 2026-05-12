package com.example.bookingtickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
  private String token;
  private Long userId;
  private String username;
  private String email;
}