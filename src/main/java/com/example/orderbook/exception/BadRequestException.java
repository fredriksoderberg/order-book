package com.example.orderbook.exception;

public class BadRequestException extends RuntimeException {
  public BadRequestException(String data) {
    super("Bad request data: " + data);
  }
}
