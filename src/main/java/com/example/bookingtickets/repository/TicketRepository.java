package com.example.bookingtickets.repository;

import com.example.bookingtickets.model.Ticket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  @Override
  @EntityGraph(attributePaths = {"user", "event"})
  List<Ticket> findAll();

  @Override
  @EntityGraph(attributePaths = {"user", "event"})
  Optional<Ticket> findById(Long id);

  @EntityGraph(attributePaths = {"user", "event"})
  List<Ticket> findByUserId(Long userId);
}