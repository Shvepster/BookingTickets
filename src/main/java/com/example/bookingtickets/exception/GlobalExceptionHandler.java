package com.example.bookingtickets.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> methodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpServletRequest request
  ) {
    List<String> errors = exception.getBindingResult()
        .getAllErrors()
        .stream()
        .map(error -> {
          if (error instanceof FieldError fieldError) {
            return fieldError.getField() + ": " + fieldError.getDefaultMessage();
          }
          return error.getDefaultMessage();
        })
        .toList();

    log.warn("Ошибка валидации для запроса {}: {}", request.getRequestURI(), errors);

    return buildResponse(HttpStatus.BAD_REQUEST, "Ошибка валидации", request, errors);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> notFound(
      NotFoundException exception,
      HttpServletRequest request
  ) {
    log.warn("Ресурс не найден: {}", exception.getMessage());
    return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request,
        List.of(exception.getMessage()));
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> emailAlreadyExists(
      EmailAlreadyExistsException exception,
      HttpServletRequest request
  ) {
    // Добавлено явное логирование WARN для демонстрации
    log.warn("Конфликт данных (409) на {}: {}", request.getRequestURI(), exception.getMessage());

    return buildResponse(
        HttpStatus.CONFLICT,
        "Конфликт данных",
        request,
        List.of(exception.getMessage())
    );
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> constraintViolation(
      ConstraintViolationException exception,
      HttpServletRequest request
  ) {
    List<String> errors = exception.getConstraintViolations()
        .stream()
        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
        .toList();

    log.warn("Ошибка параметров запроса: {}", errors);

    return buildResponse(HttpStatus.BAD_REQUEST, "Ошибка параметров", request, errors);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(
      Exception exception,
      HttpServletRequest request
  ) {
    log.error("КРИТИЧЕСКАЯ ОШИБКА на {}: {}", request.getRequestURI(),
        exception.getMessage(), exception);

    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Внутренняя ошибка сервера",
        request,
        List.of(exception.getMessage() != null ? exception.getMessage() : "Неизвестная ошибка")
    );
  }

  private ResponseEntity<ErrorResponse> buildResponse(
      HttpStatus status,
      String message,
      HttpServletRequest request,
      List<String> details
  ) {
    ErrorResponse response = new ErrorResponse(
        status.value(),
        status.getReasonPhrase(),
        message,
        request.getRequestURI(),
        details,
        LocalDateTime.now()
    );
    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(com.example.bookingtickets.exception.SeatAlreadyBookedException.class)
  public ResponseEntity<ErrorResponse> seatAlreadyBooked(
      com.example.bookingtickets.exception.SeatAlreadyBookedException exception,
      jakarta.servlet.http.HttpServletRequest request
  ) {
    log.warn("Попытка бронирования занятого места на {}: {}",
        request.getRequestURI(), exception.getMessage());
    return buildResponse(
        HttpStatus.CONFLICT,
        "Выбранное место уже занято",
        request,
        List.of(exception.getMessage())
    );
  }

  @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> dataIntegrityViolation(
      org.springframework.dao.DataIntegrityViolationException exception,
      jakarta.servlet.http.HttpServletRequest request
  ) {
    log.warn("Конфликт ограничений уникальности БД на {}: {}",
        request.getRequestURI(), exception.getMessage());
    String detailMessage = "Это место было забронировано другим пользователем "
        + "мгновением ранее. Пожалуйста, выберите другое место.";
    return buildResponse(
        HttpStatus.CONFLICT,
        "Конфликт бронирования",
        request,
        List.of(detailMessage)
    );
  }
}