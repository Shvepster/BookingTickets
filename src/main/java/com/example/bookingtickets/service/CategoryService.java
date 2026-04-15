package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.CategoryRequestDto;
import com.example.bookingtickets.dto.CategoryResponseDto;
import com.example.bookingtickets.exception.NotFoundException;
import com.example.bookingtickets.mapper.CategoryMapper;
import com.example.bookingtickets.model.Category;
import com.example.bookingtickets.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

  private final CategoryRepository categoryRepository;

  @Transactional
  public CategoryResponseDto create(CategoryRequestDto dto) {
    Category category = new Category();
    category.setName(dto.getName());
    return CategoryMapper.toDto(categoryRepository.save(category));
  }

  @Transactional
  public CategoryResponseDto update(Long id, CategoryRequestDto dto) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Категория не найдена"));
    category.setName(dto.getName());
    return CategoryMapper.toDto(categoryRepository.save(category));
  }

  @Transactional
  public void delete(Long id) {
    if (!categoryRepository.existsById(id)) {
      throw new NotFoundException("Категория не найдена");
    }
    categoryRepository.deleteById(id);
  }

  public CategoryResponseDto getById(Long id) {
    return categoryRepository.findById(id)
        .map(CategoryMapper::toDto)
        .orElseThrow(() -> new NotFoundException("Категория не найдена"));
  }

  public List<CategoryResponseDto> getAll() {
    return categoryRepository.findAll().stream()
        .map(CategoryMapper::toDto)
        .toList();
  }

  public List<CategoryResponseDto> searchByName(String name) {
    return categoryRepository.findByNameContainingIgnoreCase(name).stream()
        .map(CategoryMapper::toDto)
        .toList();
  }
}