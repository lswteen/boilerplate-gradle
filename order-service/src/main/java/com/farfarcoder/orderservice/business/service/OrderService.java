package com.farfarcoder.orderservice.business.service;

import com.farfarcoder.orderservice.business.model.OrderModel;
import com.farfarcoder.orderservice.persistence.adapter.OrderPersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderPersistenceAdapter orderPersistenceAdapter;

    public List<OrderModel> getAllOrders() {
        return orderPersistenceAdapter.findAll();
    }

    public OrderModel getOrderById(Long id) {
        return orderPersistenceAdapter.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Transactional
    public OrderModel createOrder(OrderModel orderModel) {
        // 비즈니스 로직: 총액 계산
        orderModel.calculateTotalAmount();
        return orderPersistenceAdapter.save(orderModel);
    }

    @Transactional
    public OrderModel updateOrder(Long id, OrderModel updateInfo) {
        OrderModel existingOrder = getOrderById(id);

        // 비즈니스 로직: 업데이트 (불변 객체이므로 새로운 객체를 생성하여 저장해야 함)
        // 여기서는 Builder를 사용하여 기존 객체 기반으로 새로운 객체 생성
        OrderModel updatedOrder = OrderModel.builder()
                .id(existingOrder.getId())
                .customerName(existingOrder.getCustomerName()) // 고객명 변경 불가 정책이 있다면 유지
                .productName(updateInfo.getProductName() != null ? updateInfo.getProductName()
                        : existingOrder.getProductName())
                .quantity(updateInfo.getQuantity() != null ? updateInfo.getQuantity() : existingOrder.getQuantity())
                .price(updateInfo.getPrice() != null ? updateInfo.getPrice() : existingOrder.getPrice())
                .status(updateInfo.getStatus() != null ? updateInfo.getStatus() : existingOrder.getStatus())
                .description(updateInfo.getDescription() != null ? updateInfo.getDescription()
                        : existingOrder.getDescription())
                .createdAt(existingOrder.getCreatedAt())
                .build();

        // 총액 재계산
        updatedOrder.calculateTotalAmount();

        return orderPersistenceAdapter.save(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        // 존재 확인
        getOrderById(id);
        orderPersistenceAdapter.deleteById(id);
    }

    public List<OrderModel> getOrdersByCustomerName(String customerName) {
        return orderPersistenceAdapter.findByCustomerName(customerName);
    }

    public List<OrderModel> getOrdersByStatus(OrderModel.OrderStatus status) {
        return orderPersistenceAdapter.findByStatus(status);
    }
}
