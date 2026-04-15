package com.example.bookingtickets.repository;

import com.example.bookingtickets.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByEmail(String email);

  @EntityGraph(attributePaths = {"tickets"})
  Optional<User> findByUsername(String username);

  @Override
  @EntityGraph(attributePaths = {"tickets"})
  List<User> findAll();

  @Override
  @EntityGraph(attributePaths = {"tickets"})
  Optional<User> findById(Long id);

  @EntityGraph(attributePaths = {"tickets"})
  List<User> findByUsernameContainingIgnoreCase(String username);
}