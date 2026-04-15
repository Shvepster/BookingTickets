package com.example.bookingtickets.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

    log.warn("Ошибка валидации: {}", errors);
    return buildResponse(HttpStatus.BAD_REQUEST, "Ошибка валидации данных", request, errors);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> notFound(
      NotFoundException exception,
      HttpServletRequest request
  ) {
    log.warn("Ресурс не найден: {}", exception.getMessage());
    return buildResponse(
        HttpStatus.NOT_FOUND,
        exception.getMessage(),
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
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .toList();

    return buildResponse(HttpStatus.BAD_REQUEST, "Нарушение ограничений", request, errors);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> illegalArgument(
      IllegalArgumentException exception,
      HttpServletRequest request
  ) {
    log.warn("Неверный аргумент: {}", exception.getMessage());
    return buildResponse(
        HttpStatus.BAD_REQUEST,
        exception.getMessage(),
        request,
        List.of(exception.getMessage())
    );
  }

  @ExceptionHandler({
      MethodArgumentTypeMismatchException.class,
      MissingServletRequestParameterException.class,
      HttpMessageNotReadableException.class
  })
  public ResponseEntity<ErrorResponse> badRequestExceptions(
      Exception exception,
      HttpServletRequest request
  ) {
    log.warn("Некорректный запрос: {}", exception.getMessage());
    return buildResponse(
        HttpStatus.BAD_REQUEST,
        "Некорректный формат запроса",
        request,
        List.of(exception.getMessage())
    );
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> methodNotSupported(
      HttpRequestMethodNotSupportedException exception,
      HttpServletRequest request
  ) {
    return buildResponse(
        HttpStatus.METHOD_NOT_ALLOWED,
        "HTTP метод не поддерживается",
        request,
        List.of(exception.getMessage())
    );
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> noResourceFound(
      NoResourceFoundException exception,
      HttpServletRequest request
  ) {
    return buildResponse(
        HttpStatus.NOT_FOUND,
        "Эндпоинт не найден",
        request,
        List.of("Проверьте правильность URL")
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> unexpected(
      Exception exception,
      HttpServletRequest request
  ) {
    log.error("Непредвиденная ошибка сервера: ", exception);
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Внутренняя ошибка сервера",
        request,
        List.of("Обратитесь к администратору, если ошибка повторяется")
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

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> emailAlreadyExists(
      EmailAlreadyExistsException exception,
      HttpServletRequest request
  ) {
    log.warn("Конфликт данных: {}", exception.getMessage());
    return buildResponse(
        HttpStatus.CONFLICT,
        "Конфликт данных",
        request,
        List.of(exception.getMessage())
    );
  }
}