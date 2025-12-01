package com.farfarcoder.orderservice.persistence.repository;

import com.farfarcoder.orderservice.persistence.entity.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

        @Autowired
        private OrderRepository orderRepository;

        @Test
        @DisplayName("주문 저장 및 조회 테스트")
        void saveAndFindOrder() {
                // given
                Order order = Order.builder()
                                .customerName("테스트고객")
                                .productName("테스트상품")
                                .quantity(2)
                                .price(new BigDecimal("10000"))
                                .status(Order.OrderStatus.PENDING)
                                .build();

                // when
                Order savedOrder = orderRepository.save(order);

                // then
                assertThat(savedOrder.getId()).isNotNull();
                assertThat(savedOrder.getCustomerName()).isEqualTo("테스트고객");
                assertThat(savedOrder.getTotalAmount()).isEqualByComparingTo(new BigDecimal("20000")); // @PrePersist 동작
                                                                                                       // 확인
                assertThat(savedOrder.getCreatedAt()).isNotNull();
                assertThat(savedOrder.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("고객명으로 주문 조회 테스트")
        void findByCustomerName() {
                // given
                Order order1 = Order.builder()
                                .customerName("고객A")
                                .productName("상품1")
                                .quantity(1)
                                .price(new BigDecimal("1000"))
                                .build();

                Order order2 = Order.builder()
                                .customerName("고객A")
                                .productName("상품2")
                                .quantity(1)
                                .price(new BigDecimal("2000"))
                                .build();

                Order order3 = Order.builder()
                                .customerName("고객B")
                                .productName("상품3")
                                .quantity(1)
                                .price(new BigDecimal("3000"))
                                .build();

                orderRepository.saveAll(List.of(order1, order2, order3));

                // when
                List<Order> orders = orderRepository.findByCustomerName("고객A");

                // then
                assertThat(orders).hasSize(2);
                assertThat(orders).extracting("productName")
                                .containsExactlyInAnyOrder("상품1", "상품2");
        }

        @Test
        @DisplayName("주문 상태로 조회 테스트")
        void findByStatus() {
                // given
                Order order1 = Order.builder()
                                .customerName("고객1")
                                .productName("상품1")
                                .quantity(1)
                                .price(new BigDecimal("1000"))
                                .status(Order.OrderStatus.PENDING)
                                .build();

                Order order2 = Order.builder()
                                .customerName("고객2")
                                .productName("상품2")
                                .quantity(1)
                                .price(new BigDecimal("2000"))
                                .status(Order.OrderStatus.CONFIRMED)
                                .build();

                orderRepository.saveAll(List.of(order1, order2));

                // when
                List<Order> pendingOrders = orderRepository.findByStatus(Order.OrderStatus.PENDING);

                // then
                assertThat(pendingOrders).hasSize(1);
                assertThat(pendingOrders.get(0).getCustomerName()).isEqualTo("고객1");
                assertThat(pendingOrders.get(0).getStatus()).isEqualTo(Order.OrderStatus.PENDING);
        }
}
