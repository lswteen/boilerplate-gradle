package com.farfarcoder.orderservice.presentation.facade;

import com.farfarcoder.orderservice.business.model.OrderModel;
import com.farfarcoder.orderservice.business.service.OrderService;
import com.farfarcoder.orderservice.presentation.dto.OrderRequest;
import com.farfarcoder.orderservice.presentation.dto.OrderResponse;
import com.farfarcoder.orderservice.presentation.mapper.OrderDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderPresentationFacade {

    private final OrderService orderService;
    private final OrderDtoMapper orderDtoMapper;

    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders().stream()
                .map(orderDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        OrderModel orderModel = orderService.getOrderById(id);
        return orderDtoMapper.toResponse(orderModel);
    }

    public OrderResponse createOrder(OrderRequest request) {
        OrderModel orderModel = orderDtoMapper.toModel(request);
        OrderModel createdOrder = orderService.createOrder(orderModel);
        return orderDtoMapper.toResponse(createdOrder);
    }

    public OrderResponse updateOrder(Long id, OrderRequest request) {
        OrderModel updateInfo = orderDtoMapper.toModel(request);
        OrderModel updatedOrder = orderService.updateOrder(id, updateInfo);
        return orderDtoMapper.toResponse(updatedOrder);
    }

    public void deleteOrder(Long id) {
        orderService.deleteOrder(id);
    }

    public List<OrderResponse> getOrdersByCustomerName(String customerName) {
        return orderService.getOrdersByCustomerName(customerName).stream()
                .map(orderDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByStatus(OrderModel.OrderStatus status) {
        return orderService.getOrdersByStatus(status).stream()
                .map(orderDtoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
