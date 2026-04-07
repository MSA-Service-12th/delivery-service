package com.loopang.deliveryservice.infrastructure.persistence;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.entity.DeliveryRoute;
import com.loopang.deliveryservice.domain.repository.DeliveryQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeliveryQueryRepositoryImpl implements DeliveryQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Delivery> findByDeliveryId(UUID deliveryId) {
		return Optional.empty();
	}

	@Override
	public Page<Delivery> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public Optional<DeliveryRoute> findByDeliveryRouteId(UUID deliveryRouteId) {
		return Optional.empty();
	}

	@Override
	public List<DeliveryRoute> findByOrderIdAndDeliveryId(UUID orderId, UUID deliveryId) {
		return List.of();
	}
}
