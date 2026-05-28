package com.example.bookingtickets.dataloader;

import com.example.bookingtickets.model.Category;
import com.example.bookingtickets.model.Event;
import com.example.bookingtickets.model.Ticket;
import com.example.bookingtickets.model.User;
import com.example.bookingtickets.model.Venue;
import com.example.bookingtickets.repository.CategoryRepository;
import com.example.bookingtickets.repository.EventRepository;
import com.example.bookingtickets.repository.TicketRepository;
import com.example.bookingtickets.repository.UserRepository;
import com.example.bookingtickets.repository.VenueRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
// Выполняется только для профилей dev, local или профиля по умолчанию, защищая боевую базу данных
@Profile({"dev", "local", "default"})
public class DataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final VenueRepository venueRepository;
  private final CategoryRepository categoryRepository;
  private final EventRepository eventRepository;
  private final TicketRepository ticketRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(String... args) {
    if (userRepository.count() > 0) {
      log.info("Данные уже существуют в БД. Пропуск инициализации.");
      return;
    }

    log.info("Запуск генерации тестовых данных для СУБД");

    User admin = createUser("admin", "admin@example.com", "admin123");

    Venue arena = createVenue("Минск-Арена", "пр. Победителей, 111");
    Venue palace = createVenue("Дворец Республики", "Октябрьская площадь, 1");

    Category rock = createCategory("Рок");
    Category comedy = createCategory("Комедия");

    Event scorpions = createEvent("Концерт Scorpions", 150.0, LocalDateTime.now().plusDays(10),
        arena, Set.of(rock));
    createEvent("Стендап Шоу", 50.0, LocalDateTime.now().plusDays(5),
        palace, Set.of(comedy));

    createTicket("VIP-1", admin, scorpions);

    log.info("Тестовые данные успешно загружены! Создан аккаунт: admin / admin123");
  }

  private User createUser(String username, String email, String plainPassword) {
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(plainPassword));
    return userRepository.save(user);
  }

  private Venue createVenue(String name, String address) {
    Venue venue = new Venue();
    venue.setName(name);
    venue.setAddress(address);
    return venueRepository.save(venue);
  }

  private Category createCategory(String name) {
    Category category = new Category();
    category.setName(name);
    return categoryRepository.save(category);
  }

  private Event createEvent(String title, Double price, LocalDateTime date,
                            Venue venue, Set<Category> categories) {
    Event event = new Event();
    event.setTitle(title);
    event.setPrice(price);
    event.setDate(date);
    event.setVenue(venue);
    event.setCategories(categories);
    return eventRepository.save(event);
  }

  private void createTicket(String seatNumber, User user, Event event) {
    Ticket ticket = new Ticket();
    ticket.setSeatNumber(seatNumber);
    ticket.setUser(user);
    ticket.setEvent(event);
    ticketRepository.save(ticket);
  }

}