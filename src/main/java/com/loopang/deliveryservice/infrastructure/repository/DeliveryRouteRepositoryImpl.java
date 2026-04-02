package com.loopang.deliveryservice.infrastructure.repository;

import com.loopang.deliveryservice.domain.route.DeliveryRoute;
import com.loopang.deliveryservice.domain.route.DeliveryRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeliveryRouteRepositoryImpl implements DeliveryRouteRepository {

    private final JpaDeliveryRouteRepository jpaRepository;

    @Override
    public DeliveryRoute findById(Long id) {
        return jpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("경로 없음"));
    }

    @Override
    public List<DeliveryRoute> findByDeliveryId(Long deliveryId) {
        return jpaRepository.findByDeliveryIdAndDeletedFalse(deliveryId);
    }

    @Override
    public void save(DeliveryRoute route) {
        jpaRepository.save(route);
    }
}
