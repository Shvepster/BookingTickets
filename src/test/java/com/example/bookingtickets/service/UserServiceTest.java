package com.example.bookingtickets.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.bookingtickets.dto.UserRequestDto;
import com.example.bookingtickets.dto.UserResponseDto;
import com.example.bookingtickets.exception.EmailAlreadyExistsException;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.model.User;
import com.example.bookingtickets.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder; // Мокаем бин шифрования
  @InjectMocks private UserService userService;

  private User user;
  private UserRequestDto requestDto;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setUsername("test_user");
    user.setEmail("test@test.com");
    user.setPassword("encoded_password");
    requestDto = new UserRequestDto("testUser", "test@mail.com", "password123");
  }

  @Test
  void create_Success() {
    when(userRepository.existsByEmail(any())).thenReturn(false);
    when(passwordEncoder.encode(any())).thenReturn("encoded_password");
    when(userRepository.save(any())).thenReturn(user);

    UserResponseDto response = userService.create(requestDto);
    assertNotNull(response);
    assertEquals("test_user", response.getUsername());
    verify(passwordEncoder).encode("password123");
  }

  @Test
  void create_ThrowsEmailAlreadyExists() {
    when(userRepository.existsByEmail(any())).thenReturn(true);
    assertThrows(EmailAlreadyExistsException.class, () -> userService.create(requestDto));
  }

  @Test
  void update_Success_WithPassword() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(passwordEncoder.encode(any())).thenReturn("new_encoded_password");
    when(userRepository.save(any())).thenReturn(user);

    UserResponseDto response = userService.update(1L, requestDto);
    assertNotNull(response);
    assertEquals("test_user", response.getUsername());
    verify(passwordEncoder).encode("password123");
  }

  @Test
  void update_ThrowsNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> userService.update(1L, requestDto));
  }

  @Test
  void getById_Success() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    UserResponseDto response = userService.getById(1L);
    assertEquals("test_user", response.getUsername());
  }

  @Test
  void getById_ThrowsNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> userService.getById(1L));
  }

  @Test
  void getAll_Success() {
    when(userRepository.findAll()).thenReturn(List.of(user));
    List<UserResponseDto> list = userService.getAll();
    assertEquals(1, list.size());
  }

  @Test
  void searchByUsername_Success() {
    when(userRepository.findByUsernameContainingIgnoreCase("test")).thenReturn(List.of(user));
    List<UserResponseDto> list = userService.searchByUsername("test");
    assertEquals(1, list.size());
  }

  @Test
  void delete_Success() {
    userService.delete(1L);
    verify(userRepository).deleteById(1L);
  }
}