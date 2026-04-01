package com.example.bookingtickets.repository;

import com.example.bookingtickets.model.Event;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  @Query(
      "SELECT e FROM Event e "
          + "JOIN FETCH e.venue v "
          + "JOIN FETCH e.categories c "
          + "WHERE v.name = :venueName AND c.name = :categoryName"
  )
  Page<Event> findComplexByJpql(
      @Param("venueName") String venueName,
      @Param("categoryName") String categoryName,
      Pageable pageable
  );

  @Query(
      value = "SELECT e.* FROM events e "
          + "JOIN venues v ON e.venue_id = v.id "
          + "JOIN event_categories ec ON e.id = ec.event_id "
          + "JOIN categories c ON ec.category_id = c.id "
          + "WHERE v.name = :venueName AND c.name = :categoryName",
      countQuery = "SELECT count(*) FROM events e "
          + "JOIN venues v ON e.venue_id = v.id "
          + "JOIN event_categories ec ON e.id = ec.event_id "
          + "JOIN categories c ON ec.category_id = c.id "
          + "WHERE v.name = :venueName AND c.name = :categoryName",
      nativeQuery = true
  )
  Page<Event> findComplexByNative(
      @Param("venueName") String venueName,
      @Param("categoryName") String categoryName,
      Pageable pageable
  );
}