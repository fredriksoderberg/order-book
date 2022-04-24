package com.example.orderbook.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_transaction")
@ToString
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;

  @Column(name = "ticker", nullable = false)
  String ticker;

  @Column(name = "volume", nullable = false)
  int volume;

  @Column(name = "price", nullable = false)
  double price;

  @Column(name = "currency", nullable = false)
  String currency;

  @Column(name = "type", nullable = false)
  String transactionType;

  @Column(name = "time", nullable = false)
  LocalDateTime time;
}
