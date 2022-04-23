package com.example.orderbook.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
public class OrderSummaryEntity {
    double averageBuyPrice;
    double minBuyPrice;
    double maxBuyPrice;
    double averageSellPrice;
    double minSellPrice;
    double maxSellPrice;
    long buyOrderCount;
    long sellOrderCount;
    String ticker;
    LocalDate date;
}
