package com.example.bookingtickets.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.bookingtickets.dto.CategoryRequestDto;
import com.example.bookingtickets.dto.CategoryResponseDto;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.model.Category;
import com.example.bookingtickets.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @Mock private CategoryRepository categoryRepository;
  @InjectMocks private CategoryService categoryService;

  private Category category;
  private CategoryRequestDto requestDto;

  @BeforeEach
  void setUp() {
    category = new Category();
    category.setId(1L);
    category.setName("Rock");
    requestDto = new CategoryRequestDto("Rock");
  }

  @Test
  void create_Success() {
    when(categoryRepository.save(any())).thenReturn(category);
    CategoryResponseDto response = categoryService.create(requestDto);
    assertEquals("Rock", response.getName());
  }

  @Test
  void update_Success() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(categoryRepository.save(any())).thenReturn(category);
    CategoryResponseDto response = categoryService.update(1L, requestDto);
    assertEquals("Rock", response.getName());
  }

  @Test
  void update_ThrowsNotFound() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> categoryService.update(1L, requestDto));
  }

  @Test
  void delete_Success() {
    when(categoryRepository.existsById(1L)).thenReturn(true);
    categoryService.delete(1L);
    verify(categoryRepository).deleteById(1L);
  }

  @Test
  void delete_ThrowsNotFound() {
    when(categoryRepository.existsById(1L)).thenReturn(false);
    assertThrows(NotFoundException.class, () -> categoryService.delete(1L));
  }

  @Test
  void getById_Success() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    assertEquals("Rock", categoryService.getById(1L).getName());
  }

  @Test
  void getById_ThrowsNotFound() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> categoryService.getById(1L));
  }

  @Test
  void getAll_Success() {
    when(categoryRepository.findAll()).thenReturn(List.of(category));
    assertEquals(1, categoryService.getAll().size());
  }

  @Test
  void searchByName_Success() {
    when(categoryRepository.findByNameContainingIgnoreCase("Ro")).thenReturn(List.of(category));
    assertEquals(1, categoryService.searchByName("Ro").size());
  }
}