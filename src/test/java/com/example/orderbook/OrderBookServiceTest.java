package com.example.orderbook;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.orderbook.entity.OrderEntity;
import com.example.orderbook.entity.OrderRepository;
import com.example.orderbook.entity.OrderSummaryEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {OrderBookService.class})
public class OrderBookServiceTest {
  
  @Autowired
  OrderBookService orderBookService;
  
  @MockBean
  OrderRepository orderRepository;
  
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
  
  @Test
  public void shouldBuildOrderSummary() {
    when(orderRepository.findAllByTicker(any())).thenReturn(buildOrders());
    OrderSummaryEntity orderSummary = orderBookService.buildOrderSummary("SAVE", LocalDate.parse("2022-01-01"));
    assertThat(orderSummary.getBuyOrderCount(), is(2L));
    assertThat(orderSummary.getAverageBuyPrice(), is(15.0));
    assertThat(orderSummary.getMaxBuyPrice(), is(20.0));
    assertThat(orderSummary.getMinBuyPrice(), is(10.0));
    assertThat(orderSummary.getTicker(), is("SAVE"));
    assertThat(orderSummary.getDate(), is(LocalDate.parse("2022-01-01")));
  }
  
  private List<OrderEntity> buildOrders() {
    return List.of(OrderEntity.builder()
          .id(1L)
          .currency("SEK")
          .transactionType("BUY")
          .volume(1)
          .price(10.0)
          .ticker("SAVE")
          .time(LocalDateTime.parse("2022-01-01T11:00:00"))
          .build(),
        OrderEntity.builder()
          .id(1L)
          .currency("SEK")
          .transactionType("BUY")
          .volume(1)
          .price(20.0)
          .ticker("SAVE")
          .time(LocalDateTime.parse("2022-01-01T11:00:00"))
          .build());
  }
}
