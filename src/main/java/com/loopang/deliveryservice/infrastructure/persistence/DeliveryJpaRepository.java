package com.loopang.deliveryservice.infrastructure.persistence;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.repository.DeliveryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID>, DeliveryRepository {
    Optional<Delivery> findByOrderId(UUID orderId);
}
