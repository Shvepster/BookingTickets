package com.example.bookingtickets.dataloader;

import com.example.bookingtickets.model.Event;
import com.example.bookingtickets.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Компонент для начальной загрузки данных в базу.
 */
@Component
public class DataLoader implements CommandLineRunner {

  private final EventRepository repository;

  /**
   * Конструктор загрузчика данных.
   *
   * @param repository репозиторий мероприятий
   */
  public DataLoader(EventRepository repository) {
    this.repository = repository;
  }

  /**
   * Метод запускается при старте приложения и заполняет базу.
   *
   * @param args аргументы командной строки
   */
  @Override
  public void run(String... args) {
  }
}