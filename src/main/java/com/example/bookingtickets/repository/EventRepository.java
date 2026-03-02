package com.example.bookingtickets.repository;

import com.example.bookingtickets.model.Event;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с мероприятиями.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  /**
   * Поиск мероприятий по категории без учета регистра.
   *
   * @param category название категории
   * @return список мероприятий
   */
  List<Event> findByCategoryIgnoreCase(String category);
}