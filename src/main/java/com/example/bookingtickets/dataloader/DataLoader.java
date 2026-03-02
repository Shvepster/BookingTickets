package com.example.bookingtickets.dataloader;

import com.example.bookingtickets.model.Event;
import com.example.bookingtickets.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final EventRepository repository;

    public DataLoader(EventRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        repository.save(new Event("Концерт рок-группы Rammstein", "Музыка", 500.0));
        repository.save(new Event("Дюна 2", "Кино", 60.0));
        repository.save(new Event("Гамлет", "Театр", 100.0));
    }
}