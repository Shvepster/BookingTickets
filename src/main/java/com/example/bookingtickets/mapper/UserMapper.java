package com.example.bookingtickets.mapper;

import com.example.bookingtickets.dto.UserResponseDto;
import com.example.bookingtickets.model.User;
import java.util.stream.Collectors;

public final class UserMapper {

  private UserMapper() {}

  public static UserResponseDto toDto(User user) {
    if (user == null) {
      return null;
    }
    return new UserResponseDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getTickets() == null ? null :
            user.getTickets().stream().map(t -> t.getId()).collect(Collectors.toList())
    );
  }
}