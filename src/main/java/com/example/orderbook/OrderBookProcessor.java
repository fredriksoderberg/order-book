package com.example.orderbook;

import com.example.orderbook.entity.OrderEntity;
import com.example.orderbook.entity.OrderSummaryEntity;
import com.example.orderbook.exception.BadRequestException;
import com.example.orderbook.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class OrderBookProcessor {

  OrderBookService orderBookService;

  OrderBookProcessor(OrderBookService orderBookService) {
    this.orderBookService = orderBookService;
  }

  @GetMapping("/order/{id}")
  public OrderEntity getOrderById(@PathVariable Long id) {
    try {
      return orderBookService.getOrder(id);
    } catch (OrderNotFoundException onfe) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found", onfe);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error", e);
    }
  }

  @PostMapping("/order")
  public OrderEntity createOrder(@RequestBody OrderEntity order) {
    try {
      return orderBookService.createOrder(order);
    } catch (BadRequestException bre) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request", bre);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error", e);
    }
  }

  @GetMapping("order_summary/{ticker}")
  public OrderSummaryEntity getOrderSummaryByTicker(@PathVariable String ticker,
      @RequestParam String date) {
    try {
      return orderBookService.getSummary(ticker, date);
    } catch (BadRequestException bre) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request", bre);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error", e);
    }
  }
}


