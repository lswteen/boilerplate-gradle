package com.farfarcoder.orderservice.presentation.dto;

import com.farfarcoder.orderservice.business.model.OrderModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    private String customerName;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private OrderModel.OrderStatus status;
    private String description;
}
