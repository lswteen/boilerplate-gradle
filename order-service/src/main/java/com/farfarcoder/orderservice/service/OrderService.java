package com.farfarcoder.orderservice.service;

import com.farfarcoder.orderservice.dto.OrderRequest;
import com.farfarcoder.orderservice.dto.OrderResponse;
import com.farfarcoder.orderservice.entity.Order;
import com.farfarcoder.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .productName(request.getProductName())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setCustomerName(request.getCustomerName());
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setStatus(request.getStatus());
        order.setDescription(request.getDescription());

        // 가격이나 수량이 변경되었으므로 총액 재계산 필요 (PreUpdate는 flush 시점에 실행됨)
        if (request.getQuantity() != null && request.getPrice() != null) {
            order.setTotalAmount(request.getPrice().multiply(java.math.BigDecimal.valueOf(request.getQuantity())));
        }

        Order updatedOrder = orderRepository.save(order);
        return OrderResponse.from(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    public List<OrderResponse> getOrdersByCustomerName(String customerName) {
        return orderRepository.findByCustomerName(customerName).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }
}
