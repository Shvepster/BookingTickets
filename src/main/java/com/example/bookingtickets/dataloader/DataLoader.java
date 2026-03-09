package com.example.bookingtickets.dataloader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

  @Override
  public void run(String... args) {
    // Временно пуст. Используем позже для тестирования транзакций (N+1).
  }
}