package com.example.bookingtickets.service;

import com.example.bookingtickets.dto.ConcurrencyResponseDto;
import com.example.bookingtickets.exception.OperationFailedException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConcurrencyDemoService {

  private static final long EXECUTION_TIMEOUT_SECONDS = 30L;

  public ConcurrencyResponseDto runCounter(int threads, int incrementsPerThread) {
    long expectedCount = (long) threads * incrementsPerThread;

    long nonAtomicCount = runNonAtomic(threads, incrementsPerThread);
    long atomicCount = runAtomic(threads, incrementsPerThread);

    return new ConcurrencyResponseDto(
        threads,
        incrementsPerThread,
        nonAtomicCount,
        atomicCount,
        expectedCount - nonAtomicCount
    );
  }

  private long runNonAtomic(int threads, int incrementsPerThread) {
    long[] counter = {0};
    executeConcurrent(threads, incrementsPerThread, () -> counter[0]++);
    return counter[0];
  }

  private long runAtomic(int threads, int incrementsPerThread) {
    AtomicLong counter = new AtomicLong();
    executeConcurrent(threads, incrementsPerThread, counter::incrementAndGet);
    return counter.get();
  }

  private void executeConcurrent(int threads, int incrementsPerThread, Runnable task) {
    try (ExecutorService executor = Executors.newFixedThreadPool(threads)) {
      CountDownLatch startLatch = new CountDownLatch(1);
      CountDownLatch doneLatch = new CountDownLatch(threads);
      AtomicReference<Exception> error = new AtomicReference<>();

      for (int i = 0; i < threads; i++) {
        executor.submit(() -> {
          try {
            startLatch.await();
            for (int j = 0; j < incrementsPerThread; j++) {
              task.run();
            }
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          } catch (Exception e) {
            error.compareAndSet(null, e);
          } finally {
            doneLatch.countDown();
          }
        });
      }

      startLatch.countDown();

      if (!doneLatch.await(EXECUTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
        log.error("Таймаут выполнения проверки Race condition");
      }

      if (error.get() != null) {
        throw new OperationFailedException("Ошибка выполнения тестов многопоточности", error.get());
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new OperationFailedException("Выполнение прервано", e);
    }
  }
}