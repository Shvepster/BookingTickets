package com.example.bookingtickets.mapper;

import com.example.bookingtickets.dto.CategoryResponseDto;
import com.example.bookingtickets.model.Category;

public final class CategoryMapper {

  private CategoryMapper() {}

  public static CategoryResponseDto toDto(Category category) {
    if (category == null) {
      return null;
    }
    return new CategoryResponseDto(
        category.getId(),
        category.getName()
    );
  }
}