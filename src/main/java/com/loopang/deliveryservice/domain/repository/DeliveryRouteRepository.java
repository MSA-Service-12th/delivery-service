package com.loopang.deliveryservice.domain.repository;

import com.loopang.deliveryservice.domain.entity.DeliveryRoute;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRouteRepository {

    Optional<DeliveryRoute> findById(UUID deliveryRouteId);
    List<DeliveryRoute> findByOrderIdAndDeliveryId(UUID orderId, UUID deliveryId);
    DeliveryRoute save(DeliveryRoute route);
}
