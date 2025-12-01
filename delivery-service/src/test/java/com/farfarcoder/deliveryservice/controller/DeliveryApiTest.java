package com.farfarcoder.deliveryservice.controller;

import com.farfarcoder.deliveryservice.domain.Delivery;
import com.farfarcoder.deliveryservice.repository.DeliveryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeliveryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    void getDelivery() throws Exception {
        // given
        Delivery delivery = new Delivery("Seoul, Gangnam-gu", "PREPARING");
        deliveryRepository.save(delivery);

        // when & then
        mockMvc.perform(get("/deliveries/" + delivery.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Seoul, Gangnam-gu"))
                .andExpect(jsonPath("$.status").value("PREPARING"));
    }
}
