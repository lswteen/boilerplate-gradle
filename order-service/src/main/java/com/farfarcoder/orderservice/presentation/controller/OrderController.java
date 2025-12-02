package com.farfarcoder.orderservice.presentation.controller;

import com.farfarcoder.orderservice.business.model.OrderModel;
import com.farfarcoder.orderservice.presentation.dto.OrderRequest;
import com.farfarcoder.orderservice.presentation.dto.OrderResponse;
import com.farfarcoder.orderservice.presentation.facade.OrderPresentationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderPresentationFacade orderPresentationFacade;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderPresentationFacade.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderPresentationFacade.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderPresentationFacade.createOrder(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderPresentationFacade.updateOrder(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderPresentationFacade.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomerName(@PathVariable String customerName) {
        return ResponseEntity.ok(orderPresentationFacade.getOrdersByCustomerName(customerName));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable OrderModel.OrderStatus status) {
        return ResponseEntity.ok(orderPresentationFacade.getOrdersByStatus(status));
    }
}
