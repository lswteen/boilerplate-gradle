package com.farfarcoder.orderservice.persistence.repository;

import com.farfarcoder.orderservice.persistence.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerName(String customerName);

    List<Order> findByStatus(Order.OrderStatus status);
}
