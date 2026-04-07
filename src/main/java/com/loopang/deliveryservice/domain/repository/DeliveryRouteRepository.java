package com.loopang.deliveryservice.domain.repository;

import com.loopang.deliveryservice.domain.entity.DeliveryRoute;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRouteRepository {
    Optional<DeliveryRoute> findById(UUID id);
    DeliveryRoute save(DeliveryRoute deliveryRoute);
}
