package com.loopang.deliveryservice.infrastructure.repository;

import com.loopang.deliveryservice.domain.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaDeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findAllByDeletedFalse();

}
