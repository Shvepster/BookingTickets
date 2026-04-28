package com.example.bookingtickets.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.bookingtickets.dto.ConcurrencyResponseDto;
import com.example.bookingtickets.exception.OperationFailedException;
import org.junit.jupiter.api.Test;

class ConcurrencyDemoServiceTest {

  private final ConcurrencyDemoService service = new ConcurrencyDemoService();

  @Test
  void runRaceConditionDemo_ShouldKeepSafeCountersAccurate() {
    ConcurrencyResponseDto result = service.runCounter(50, 1000);
    long expectedCount = 50000L;

    assertEquals(expectedCount, result.getAtomicCount());
    assertTrue(result.getNonAtomicCount() <= expectedCount);
  }

  @Test
  void runCounter_ShouldHandleExceptionInThread() {
    assertThrows(OperationFailedException.class, () -> {
      service.runCounter(-1, 100);
    });
  }

  @Test
  void executeConcurrent_ShouldHandleInterruption() {
    Thread.currentThread().interrupt();
    assertThrows(OperationFailedException.class, () -> service.runCounter(1, 1));
  }
}