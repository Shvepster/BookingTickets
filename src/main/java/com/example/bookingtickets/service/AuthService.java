package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.AuthResponseDto;
import com.example.bookingtickets.dto.LoginRequestDto;
import com.example.bookingtickets.dto.UserRequestDto;
import com.example.bookingtickets.exception.EmailAlreadyExistsException;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.exception.OperationFailedException;
import com.example.bookingtickets.model.User;
import com.example.bookingtickets.repository.UserRepository;
import com.example.bookingtickets.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtUtils;

  @Transactional
  public AuthResponseDto register(UserRequestDto request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new EmailAlreadyExistsException("Этот Email уже занят");
    }

    if (userRepository.existsByUsername(request.getUsername())) {
      throw new OperationFailedException("Этот логин уже занят");
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword())); // Сохраняем безопасно!

    User savedUser = userRepository.save(user);

    // 4. Генерируем токен
    String token = jwtUtils.generateToken(savedUser.getId(), savedUser.getUsername());
    return new AuthResponseDto(
        token,
        savedUser.getId(),
        savedUser.getUsername(),
        savedUser.getEmail()
    );
  }

  @Transactional(readOnly = true)
  public AuthResponseDto login(LoginRequestDto request) {
    User user = request.getLogin().contains("@")
        ? userRepository.findByEmail(request.getLogin())
        .orElseThrow(() -> new NotFoundException("Пользователь не найден"))
        : userRepository.findByUsername(request.getLogin())
        .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new OperationFailedException("Неверный логин или пароль");
    }

    String token = jwtUtils.generateToken(user.getId(), user.getUsername());
    return new AuthResponseDto(
        token,
        user.getId(),
        user.getUsername(),
        user.getEmail()
    );
  }
}