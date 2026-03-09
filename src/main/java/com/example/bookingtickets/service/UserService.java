package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.UserRequestDto;
import com.example.bookingtickets.dto.UserResponseDto;
import com.example.bookingtickets.mapper.UserMapper;
import com.example.bookingtickets.model.User;
import com.example.bookingtickets.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public UserResponseDto create(UserRequestDto dto) {
    User user = new User();
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    User saved = userRepository.save(user);
    return UserMapper.toDto(saved);
  }

  @Transactional
  public UserResponseDto update(Long id, UserRequestDto dto) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    User saved = userRepository.save(user);
    return UserMapper.toDto(saved);
  }

  @Transactional
  public void delete(Long id) {
    userRepository.deleteById(id);
  }

  public UserResponseDto getById(Long id) {
    return userRepository.findById(id)
        .map(UserMapper::toDto)
        .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
  }

  public List<UserResponseDto> getAll() {
    List<User> users = userRepository.findAll();
    List<UserResponseDto> result = new ArrayList<>();
    for (User user : users) {
      result.add(UserMapper.toDto(user));
    }
    return result;
  }
}