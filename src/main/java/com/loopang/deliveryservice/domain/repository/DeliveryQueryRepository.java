package com.loopang.deliveryservice.domain.repository;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.entity.DeliveryRoute;
import com.loopang.deliveryservice.infrastructure.persistence.DeliveryQueryCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryQueryRepository {
    Optional<Delivery> findByDeliveryId(UUID deliveryId);
    Page<Delivery> findAll(DeliveryQueryCondition condition, Pageable pageable);
    Optional<DeliveryRoute> findByDeliveryRouteId(UUID deliveryRouteId);
    List<DeliveryRoute> findByOrderIdAndDeliveryId(UUID orderId, UUID deliveryId);
}
