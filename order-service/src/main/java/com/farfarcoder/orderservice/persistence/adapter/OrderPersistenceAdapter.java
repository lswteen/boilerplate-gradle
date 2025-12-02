package com.farfarcoder.orderservice.persistence.adapter;

import com.farfarcoder.orderservice.business.model.OrderModel;
import com.farfarcoder.orderservice.persistence.entity.Order;
import com.farfarcoder.orderservice.persistence.mapper.OrderEntityMapper;
import com.farfarcoder.orderservice.persistence.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter {

    private final OrderRepository orderRepository;
    private final OrderEntityMapper orderEntityMapper;

    public OrderModel save(OrderModel orderModel) {
        Order order = orderEntityMapper.toEntity(orderModel);
        Order savedOrder = orderRepository.save(order);
        return orderEntityMapper.toModel(savedOrder);
    }

    public List<OrderModel> findAll() {
        return orderRepository.findAll().stream()
                .map(orderEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    public Optional<OrderModel> findById(Long id) {
        return orderRepository.findById(id)
                .map(orderEntityMapper::toModel);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    public List<OrderModel> findByCustomerName(String customerName) {
        return orderRepository.findByCustomerName(customerName).stream()
                .map(orderEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    // 상태값 매핑 필요 (Model.Status -> Entity.Status)
    public List<OrderModel> findByStatus(OrderModel.OrderStatus status) {
        // Enum 매핑이 필요하므로 Mapper 사용 권장되나, 여기서는 간단히 이름 기반 매핑 가정
        // 실제로는 Mapper를 통해 Enum 변환을 하거나, 직접 변환해야 함.
        // OrderEntityMapper가 Enum 매핑을 지원하도록 설정되어 있다고 가정.
        // 하지만 Repository 메서드는 Entity의 Enum을 요구함.
        Order.OrderStatus entityStatus = Order.OrderStatus.valueOf(status.name());
        return orderRepository.findByStatus(entityStatus).stream()
                .map(orderEntityMapper::toModel)
                .collect(Collectors.toList());
    }
}
