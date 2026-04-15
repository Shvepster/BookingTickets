package com.example.bookingtickets.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }
}