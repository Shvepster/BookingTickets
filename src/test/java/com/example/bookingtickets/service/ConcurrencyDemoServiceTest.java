package com.example.bookingtickets.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.bookingtickets.dto.ConcurrencyResponseDto;
import org.junit.jupiter.api.Test;

class ConcurrencyDemoServiceTest {

  private final ConcurrencyDemoService service = new ConcurrencyDemoService();

  @Test
  void runRaceConditionDemo_ShouldKeepSafeCountersAccurate() {
    ConcurrencyResponseDto result = service.runCounter(50, 10000);
    long expectedCount = 500000L;

    assertEquals(expectedCount, result.getAtomicCount());

    assertTrue(result.getNonAtomicCount() <= expectedCount);
  }

  @Test
  void runCounter_ShouldHandleExceptionInThread() {
    assertThrows(RuntimeException.class, () -> {
      service.runCounter(-1, 100);
    });
  }

  @Test
  void executeConcurrent_ShouldHandleInterruption() {
    Thread.currentThread().interrupt();
    assertThrows(RuntimeException.class, () -> service.runCounter(1, 1));
  }
}