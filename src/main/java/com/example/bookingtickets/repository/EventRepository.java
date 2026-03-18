package com.example.bookingtickets.repository;

import com.example.bookingtickets.model.Event;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  @Override
  @EntityGraph(attributePaths = {"venue", "categories"})
  List<Event> findAll();

  @Override
  @EntityGraph(attributePaths = {"venue", "categories"})
  Optional<Event> findById(Long id);

  @EntityGraph(attributePaths = {"venue", "categories"})
  List<Event> findByTitleContainingIgnoreCase(String title);

  @org.springframework.data.jpa.repository.Query("SELECT e FROM Event e")
  List<Event> findAllWithoutEntityGraph();
}