package com.loopang.deliveryservice.domain.repository;

import com.loopang.deliveryservice.domain.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryQueryRepository {

	Optional<Delivery> findById(UUID deliveryId);
	Page<Delivery> findAll(Pageable pageable);
}
