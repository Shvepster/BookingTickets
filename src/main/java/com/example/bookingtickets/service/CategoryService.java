package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.CategoryRequestDto;
import com.example.bookingtickets.dto.CategoryResponseDto;
import com.example.bookingtickets.mapper.CategoryMapper;
import com.example.bookingtickets.model.Category;
import com.example.bookingtickets.repository.CategoryRepository;
import java.util.ArrayList;
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
    Category saved = categoryRepository.save(category);
    return CategoryMapper.toDto(saved);
  }

  @Transactional
  public CategoryResponseDto update(Long id, CategoryRequestDto dto) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
    category.setName(dto.getName());
    Category saved = categoryRepository.save(category);
    return CategoryMapper.toDto(saved);
  }

  @Transactional
  public void delete(Long id) {
    categoryRepository.deleteById(id);
  }

  public CategoryResponseDto getById(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
    return CategoryMapper.toDto(category);
  }

  public List<CategoryResponseDto> getAll() {
    List<Category> categories = categoryRepository.findAll();
    List<CategoryResponseDto> result = new ArrayList<>();
    for (Category category : categories) {
      result.add(CategoryMapper.toDto(category));
    }
    return result;
  }
}