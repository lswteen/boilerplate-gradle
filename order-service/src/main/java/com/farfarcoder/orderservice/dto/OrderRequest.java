package com.farfarcoder.orderservice.dto;

import com.farfarcoder.orderservice.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    private String customerName;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private Order.OrderStatus status;
    private String description;
}
