package com.example.orderbook;

import java.util.Arrays;
import java.util.stream.Collectors;

public class OrderBookHelper {

  public enum TransactionType {
    BUY,
    SELL;
  }

  public enum Ticker {
    GME,
    TSLA,
    SAVE;
  }

  public static boolean validateTicker(String ticker) {
    return Arrays.stream(Ticker.values())
        .map(Ticker::name)
        .collect(Collectors.toSet())
        .contains(ticker);
  }

  public static boolean validateTransactionType(String type) {
    return Arrays.stream(TransactionType.values())
        .map(TransactionType::name)
        .collect(Collectors.toSet())
        .contains(type);
  }
}
