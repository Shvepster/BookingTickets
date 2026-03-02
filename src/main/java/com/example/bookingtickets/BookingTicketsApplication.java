package com.example.bookingtickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения для бронирования билетов.
 */
@SpringBootApplication
public class BookingTicketsApplication {

  /**
   * Точка входа в приложение.
   *
   * @param args аргументы командной строки.
   */
  public static void main(String[] args) {
    SpringApplication.run(BookingTicketsApplication.class, args);
  }
}