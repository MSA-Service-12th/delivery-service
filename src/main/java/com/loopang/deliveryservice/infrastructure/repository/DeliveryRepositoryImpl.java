package com.loopang.deliveryservice.infrastructure.repository;

import com.loopang.deliveryservice.domain.delivery.Delivery;
import com.loopang.deliveryservice.domain.delivery.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final JpaDeliveryRepository jpaRepository;

    @Override
    public Delivery findById(UUID deliveryId) {
        return jpaRepository.findByIdAndDeletedAtIsNull(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("배송 없음"));
    }

    @Override
    public List<Delivery> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void save(Delivery delivery) {
        jpaRepository.save(delivery);
    }
}
