package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.UserRequestDto;
import com.example.bookingtickets.dto.UserResponseDto;
import com.example.bookingtickets.exception.EmailAlreadyExistsException;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.mapper.UserMapper;
import com.example.bookingtickets.model.User;
import com.example.bookingtickets.repository.UserRepository;
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
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new EmailAlreadyExistsException("Email уже занят!");
    }
    User user = new User();
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    return UserMapper.toDto(userRepository.save(user));
  }

  @Transactional
  public UserResponseDto update(Long id, UserRequestDto dto) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    return UserMapper.toDto(userRepository.save(user));
  }

  @Transactional
  public void delete(Long id) {
    userRepository.deleteById(id);
  }

  public UserResponseDto getById(Long id) {
    return userRepository.findById(id)
        .map(UserMapper::toDto)
        .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
  }

  public List<UserResponseDto> getAll() {
    return userRepository.findAll().stream().map(UserMapper::toDto).toList();
  }

  public List<UserResponseDto> searchByUsername(String username) {
    return userRepository.findByUsernameContainingIgnoreCase(username).stream()
        .map(UserMapper::toDto).toList();
  }
}