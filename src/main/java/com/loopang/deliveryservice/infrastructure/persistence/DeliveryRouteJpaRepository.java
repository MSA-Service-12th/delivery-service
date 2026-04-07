package com.loopang.deliveryservice.infrastructure.persistence;

import com.loopang.deliveryservice.domain.entity.DeliveryRoute;
import com.loopang.deliveryservice.domain.repository.DeliveryRouteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryRouteJpaRepository extends JpaRepository<DeliveryRoute, UUID>, DeliveryRouteRepository {
}
