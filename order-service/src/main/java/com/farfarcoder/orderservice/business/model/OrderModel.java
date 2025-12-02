package com.farfarcoder.orderservice.business.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class OrderModel {

    private Long id;
    private String customerName;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }

    // 비즈니스 로직: 총액 계산
    public void calculateTotalAmount() {
        if (this.quantity != null && this.price != null) {
            this.totalAmount = this.price.multiply(BigDecimal.valueOf(this.quantity));
        }
    }

    // 비즈니스 로직: 정보 업데이트 (불변 객체라면 새로운 객체를 반환해야 하지만, 편의상 일부 필드 변경 메서드 추가 또는 toBuilder
    // 사용)
    // 여기서는 toBuilder 패턴을 사용하는 것이 좋으나, Model 내부에서 상태 변경 메서드를 두는 방식도 가능합니다.
    // 하지만 @Builder만 있고 @Setter가 없으므로, 값을 바꾸려면 toBuilder()를 쓰거나 별도 메서드가 필요합니다.
}
