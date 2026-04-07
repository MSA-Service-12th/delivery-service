package com.loopang.deliveryservice.domain.repository;

import com.loopang.deliveryservice.domain.entity.Delivery;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    Optional<Delivery> findById(UUID deliveryId);
    Optional<Delivery> findByOrderId(UUID orderId);
    Delivery save(Delivery delivery);
}
