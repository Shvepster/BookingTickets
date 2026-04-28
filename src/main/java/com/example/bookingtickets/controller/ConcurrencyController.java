package com.example.bookingtickets.controller;

import com.example.bookingtickets.dto.ConcurrencyResponseDto;
import com.example.bookingtickets.service.ConcurrencyDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/concurrency")
@RequiredArgsConstructor
@Tag(name = "Многопоточность", description = "Демонстрация Race Condition")
public class ConcurrencyController {

  private final ConcurrencyDemoService concurrencyDemoService;

  @GetMapping("/race-demo")
  @Operation(summary = "Запустить демо", description = "Показывает потерю данных без синхронизации")
  public ConcurrencyResponseDto runRaceDemo(
      @RequestParam(defaultValue = "50") int threads,
      @RequestParam(defaultValue = "1000") int incrementsPerThread
  ) {
    return concurrencyDemoService.runCounter(threads, incrementsPerThread);
  }
}