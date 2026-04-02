package com.loopang.deliveryservice.infrastructure.repository;

import com.loopang.deliveryservice.domain.route.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaDeliveryRouteRepository extends JpaRepository<DeliveryRoute, Long> {

    List<DeliveryRoute> findByDeliveryIdAndDeletedFalse(Long deliveryId);

}
