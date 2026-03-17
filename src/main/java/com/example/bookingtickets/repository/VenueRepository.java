package com.example.bookingtickets.repository;

import com.example.bookingtickets.model.Venue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

  @Override
  @EntityGraph(attributePaths = {"events"})
  List<Venue> findAll();

  @Override
  @EntityGraph(attributePaths = {"events"})
  Optional<Venue> findById(Long id);

  @EntityGraph(attributePaths = {"events"})
  List<Venue> findByNameContainingIgnoreCase(String name);
}