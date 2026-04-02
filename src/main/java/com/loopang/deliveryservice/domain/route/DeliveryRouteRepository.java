package com.loopang.deliveryservice.domain.route;

import java.util.List;

public interface DeliveryRouteRepository {
    DeliveryRoute findById(Long id);
    List<DeliveryRoute> findByDeliveryId(Long deliveryId);
    void save(DeliveryRoute route);
}
