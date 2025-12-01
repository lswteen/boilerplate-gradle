package com.farfarcoder.orderservice.controller;

import com.farfarcoder.orderservice.dto.OrderRequest;
import com.farfarcoder.orderservice.entity.Order;
import com.farfarcoder.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // 테스트 전 데이터 초기화
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 생성 성공 테스트")
    void createOrder() throws Exception {
        // given
        OrderRequest request = OrderRequest.builder()
                .customerName("테스트고객")
                .productName("테스트상품")
                .quantity(2)
                .price(new BigDecimal("10000"))
                .description("테스트 주문입니다")
                .status(Order.OrderStatus.PENDING)
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("테스트고객"))
                .andExpect(jsonPath("$.productName").value("테스트상품"))
                .andExpect(jsonPath("$.totalAmount").value(20000)) // 2 * 10000
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("주문 조회 성공 테스트")
    void getOrder() throws Exception {
        // given
        Order order = Order.builder()
                .customerName("조회고객")
                .productName("조회상품")
                .quantity(1)
                .price(new BigDecimal("50000"))
                .totalAmount(new BigDecimal("50000"))
                .status(Order.OrderStatus.CONFIRMED)
                .build();
        Order savedOrder = orderRepository.save(order);

        // when & then
        mockMvc.perform(get("/api/v1/order/{id}", savedOrder.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedOrder.getId()))
                .andExpect(jsonPath("$.customerName").value("조회고객"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @DisplayName("주문 수정 성공 테스트")
    void updateOrder() throws Exception {
        // given
        Order order = Order.builder()
                .customerName("수정전고객")
                .productName("수정전상품")
                .quantity(1)
                .price(new BigDecimal("10000"))
                .build();
        Order savedOrder = orderRepository.save(order);

        OrderRequest updateRequest = OrderRequest.builder()
                .customerName("수정후고객")
                .productName("수정후상품")
                .quantity(5)
                .price(new BigDecimal("20000"))
                .status(Order.OrderStatus.SHIPPED)
                .description("수정된 주문")
                .build();

        // when & then
        mockMvc.perform(put("/api/v1/order/{id}", savedOrder.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("수정후고객"))
                .andExpect(jsonPath("$.productName").value("수정후상품"))
                .andExpect(jsonPath("$.totalAmount").value(100000)) // 5 * 20000
                .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    @Test
    @DisplayName("주문 삭제 성공 테스트")
    void deleteOrder() throws Exception {
        // given
        Order order = Order.builder()
                .customerName("삭제고객")
                .productName("삭제상품")
                .quantity(1)
                .price(new BigDecimal("10000"))
                .build();
        Order savedOrder = orderRepository.save(order);

        // when & then
        mockMvc.perform(delete("/api/v1/order/{id}", savedOrder.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        // 삭제 확인
        mockMvc.perform(get("/api/v1/order/{id}", savedOrder.getId()))
                .andExpect(status().isNotFound()); // 404 Not Found
    }
}
