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
public class UserResponseDto {
  private Long id;
  private String username;
  private String email;
  private List<Long> ticketIds; // Можно хранить ID билетов пользователя
}