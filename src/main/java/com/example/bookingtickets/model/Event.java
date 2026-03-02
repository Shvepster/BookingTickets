package com.example.bookingtickets.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность мероприятия.
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String category;

  private Double price;

  /**
   * Конструктор для создания мероприятия без ID.
   *
   * @param title название
   * @param category категория
   * @param price цена
   */
  public Event(String title, String category, Double price) {
    this.title = title;
    this.category = category;
    this.price = price;
  }
}