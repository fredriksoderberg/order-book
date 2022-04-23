package com.example.orderbook.entity;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
  List<OrderEntity> findAllByTicker(String ticker);
}
