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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final VenueRepository venueRepository;
  private final CategoryRepository categoryRepository;
  private final EventRepository eventRepository;
  private final TicketRepository ticketRepository;

  @Override
  @Transactional
  public void run(String... args) {
    if (userRepository.count() > 0) {
      log.info("Данные уже существуют в БД. Пропуск инициализации.");
      demonstrateNplusOneProblem();
      return;
    }

    log.info("Загрузка тестовых данных...");

    User user = new User();
    user.setUsername("admin");
    user.setEmail("admin@example.com");
    userRepository.save(user);

    Venue venue1 = new Venue();
    venue1.setName("Минск-Арена");
    venue1.setAddress("пр. Победителей, 111");
    venueRepository.save(venue1);

    Venue venue2 = new Venue();
    venue2.setName("Дворец Республики");
    venue2.setAddress("Октябрьская площадь, 1");
    venueRepository.save(venue2);

    Category catRock = new Category();
    catRock.setName("Рок");
    categoryRepository.save(catRock);

    Category catComedy = new Category();
    catComedy.setName("Комедия");
    categoryRepository.save(catComedy);

    Event event1 = new Event();
    event1.setTitle("Концерт Scorpions");
    event1.setPrice(150.0);
    event1.setDate(LocalDateTime.now().plusDays(10));
    event1.setVenue(venue1);
    event1.setCategories(Set.of(catRock));
    eventRepository.save(event1);

    Event event2 = new Event();
    event2.setTitle("Стендап Шоу");
    event2.setPrice(50.0);
    event2.setDate(LocalDateTime.now().plusDays(5));
    event2.setVenue(venue2);
    event2.setCategories(Set.of(catComedy));
    eventRepository.save(event2);

    Ticket ticket = new Ticket();
    ticket.setSeatNumber("VIP-1");
    ticket.setUser(user);
    ticket.setEvent(event1);
    ticketRepository.save(ticket);

    log.info("Тестовые данные успешно загружены!");

    demonstrateNplusOneProblem();
  }

  private void demonstrateNplusOneProblem() {
    log.info("НАЧАЛО ДЕМОНСТРАЦИИ N+1");
    List<Event> badEvents = eventRepository.findAllWithoutEntityGraph();
    for (Event e : badEvents) {
      log.info("Мероприятие: {}, Площадка: {}",
          e.getTitle(), e.getVenue().getName());
    }


    log.info("НАЧАЛО ДЕМОНСТРАЦИИ РЕШЕНИЯ С @EntityGraph");
    List<Event> goodEvents = eventRepository.findAll();
    for (Event e : goodEvents) {
      log.info("Мероприятие: {}, Площадка: {}", e.getTitle(), e.getVenue().getName());
    }
    log.info("КОНЕЦ ХОРОШЕГО ВАРИАНТА");
  }
}