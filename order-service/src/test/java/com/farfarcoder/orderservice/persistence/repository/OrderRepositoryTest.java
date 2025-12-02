package com.farfarcoder.orderservice.persistence.repository;

import com.farfarcoder.orderservice.business.model.OrderModel;
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
                // @PrePersist/PreUpdate 동작 확인 (Entity 내부 로직)
                // save 호출 시점에 PrePersist가 동작하지 않을 수 있음 (flush 필요)
                // 하지만 여기서는 save 후 반환된 객체를 검증하므로, DB에 갔다온 것이 아니라면 null일 수 있음.
                // DataJpaTest는 기본적으로 트랜잭션 롤백이므로 flush를 명시하지 않으면 DB 반영 안될 수 있음.
                // 그러나 save()는 persist를 호출하므로 1차 캐시에 저장됨.
                // PrePersist는 persist 호출 시점에 실행됨.
                // 다만 totalAmount 계산 로직이 PreUpdate에만 있었는지 확인 필요.
                // Entity 코드에서 PreUpdate에만 calculateTotalAmount가 있었으므로, insert 시에는 계산 안될 수 있음.
                // Entity 수정 시 PrePersist 추가 필요. (이전 단계에서 확인 못했으나 추가하는 것이 좋음)
                // 일단 테스트는 실행해보고 실패하면 Entity 수정.
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
                                .status(Order.OrderStatus.PENDING)
                                .build();

                Order order2 = Order.builder()
                                .customerName("고객A")
                                .productName("상품2")
                                .quantity(1)
                                .price(new BigDecimal("2000"))
                                .status(Order.OrderStatus.PENDING)
                                .build();

                Order order3 = Order.builder()
                                .customerName("고객B")
                                .productName("상품3")
                                .quantity(1)
                                .price(new BigDecimal("3000"))
                                .status(Order.OrderStatus.PENDING)
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
