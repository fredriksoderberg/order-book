package com.example.orderbook;

import com.example.orderbook.entity.OrderEntity;
import com.example.orderbook.entity.OrderRepository;
import com.example.orderbook.entity.OrderSummaryEntity;
import com.example.orderbook.exception.BadRequestException;
import com.example.orderbook.exception.OrderNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderBookService {

  OrderRepository orderRepository;

  Map<String, OrderSummaryEntity> summaryEntityMap = new HashMap<>();

  OrderBookService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }
  
  public OrderEntity getOrder(final long id) {
    log.info("Getting order: {}", id);
    return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
  }

  public OrderEntity createOrder(final OrderEntity order) {
    if (!OrderBookHelper.validateTicker(order.getTicker())) {
      throw new BadRequestException("Invalid ticker: " + order.getTicker());
    }

    if (!OrderBookHelper.validateTransactionType(order.getTransactionType())) {
      throw new BadRequestException("Invalid transaction type: " + order.getTransactionType());
    }

    OrderEntity orderToSave = order.toBuilder().time(LocalDateTime.now()).build();
    log.info("Creating order: {}", orderToSave.toString());
    OrderEntity savedOrder = orderRepository.save(orderToSave);
    updateOrderSummary(savedOrder);
    return savedOrder;
  }

  public OrderSummaryEntity getSummary(final String ticker, final String date) {
    if (!OrderBookHelper.validateTicker(ticker)) {
      throw new BadRequestException("Invalid ticker: " + ticker);
    }

    OrderSummaryEntity orderSummary = summaryEntityMap.get(buildOrderSummaryKey(ticker, date));

    if (orderSummary != null) {
      return orderSummary;
    } else {
      return buildOrderSummary(ticker, LocalDate.parse(date));
    }
  }

  private void updateOrderSummary(final OrderEntity order) {
    OrderSummaryEntity orderSummary = summaryEntityMap.get(
        buildOrderSummaryKey(order.getTicker(), order.getTime().toLocalDate().toString()));

    if (orderSummary != null) {
      switch (order.getTransactionType()) {
        case "BUY" -> {
          orderSummary.setMaxBuyPrice(Math.max(orderSummary.getMaxBuyPrice(), order.getPrice()));
          orderSummary.setMinBuyPrice(Math.min(orderSummary.getMinBuyPrice(), order.getPrice()));
          orderSummary.setBuyOrderCount(orderSummary.getBuyOrderCount() + 1L);
          orderSummary.setAverageBuyPrice(
              calculateAverage(orderSummary.getAverageBuyPrice(), order.getPrice(),
                  orderSummary.getBuyOrderCount()));
        }
        case "SELL" -> {
          orderSummary.setMaxSellPrice(Math.max(orderSummary.getMaxSellPrice(), order.getPrice()));
          orderSummary.setMinSellPrice(Math.min(orderSummary.getMinSellPrice(), order.getPrice()));
          orderSummary.setSellOrderCount(orderSummary.getSellOrderCount() + 1L);
          orderSummary.setAverageSellPrice(
              calculateAverage(orderSummary.getAverageSellPrice(), order.getPrice(),
                  orderSummary.getSellOrderCount()));
        }
      }
    }
  }

  protected OrderSummaryEntity buildOrderSummary(final String ticker, final LocalDate date) {
    List<OrderEntity> orders = orderRepository.findAllByTicker(ticker);

    DoubleSummaryStatistics buyStats = orders.stream()
        .filter(o -> o.getTransactionType().equals("BUY") 
            && date.isEqual(LocalDate.from(o.getTime())))
        .map(OrderEntity::getPrice)
        .mapToDouble(d -> d)
        .summaryStatistics();
    DoubleSummaryStatistics sellStats = orders.stream()
        .filter(o -> o.getTransactionType().equals("SELL")
            && date.isEqual(LocalDate.from(o.getTime())))
        .map(OrderEntity::getPrice)
        .mapToDouble(d -> d)
        .summaryStatistics();

    OrderSummaryEntity summaryEntity = OrderSummaryEntity.builder()
        .maxBuyPrice(buyStats.getMax())
        .minBuyPrice(buyStats.getMin())
        .averageBuyPrice(buyStats.getAverage())
        .maxSellPrice(sellStats.getMax())
        .minSellPrice(sellStats.getMin())
        .averageSellPrice(sellStats.getAverage())
        .buyOrderCount(buyStats.getCount())
        .sellOrderCount(sellStats.getCount())
        .ticker(ticker)
        .date(date)
        .build();
    
    summaryEntityMap.put(buildOrderSummaryKey(ticker, date.toString()), summaryEntity);
    return summaryEntity;
  }

  protected static String buildOrderSummaryKey(final String ticker, final String date) {
    return ticker + "-" + date;
  }

  protected static double calculateAverage(final double previousAverage, final double newValue,
      final long count) {
    return (previousAverage * count + newValue) / (count + 1);
  }
}
