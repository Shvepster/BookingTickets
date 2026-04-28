package com.example.bookingtickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Результаты тестирования Race Condition")
public class ConcurrencyResponseDto {

  @Schema(description = "Количество потоков", example = "50")
  private int threads;

  @Schema(description = "Инкрементов на поток", example = "1000")
  private int incrementsPerThread;

  @Schema(description = "Итоговое значение БЕЗ синхронизации", example = "49873")
  private long nonAtomicCount;

  @Schema(description = "Итоговое значение С синхронизацией", example = "50000")
  private long atomicCount;

  @Schema(description = "Количество потерянных обновлений (Race condition)", example = "127")
  private long lostUpdates;
}