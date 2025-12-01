package com.farfarcoder.deliveryservice.repository;

import com.farfarcoder.deliveryservice.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
