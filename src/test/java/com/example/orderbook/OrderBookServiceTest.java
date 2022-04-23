package com.example.orderbook;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderBookServiceTest {

  OrderBookService orderBookService;

  @Test
  public void shouldGetOrderSummaryKey() {
    String ticker = "SAVE";
    String date = "2022-01-01";
    assertThat(OrderBookService.buildOrderSummaryKey(ticker, date), is("SAVE-2022-01-01"));
  }

  @Test
  public void shouldCalculateAverage() {
    double previousAverage = 10.0;
    double newValue = 10.0;
    long count = 2;
    assertThat(OrderBookService.calculateAverage(previousAverage, newValue, count), is(10.0));
  }
}
