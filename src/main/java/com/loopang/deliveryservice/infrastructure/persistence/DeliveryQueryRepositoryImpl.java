package com.loopang.deliveryservice.infrastructure.persistence;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.entity.DeliveryRoute;
import com.loopang.deliveryservice.domain.repository.DeliveryQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.loopang.deliveryservice.domain.entity.QDelivery.delivery;
import static com.loopang.deliveryservice.domain.entity.QDeliveryRoute.deliveryRoute;

@Component
@RequiredArgsConstructor
public class DeliveryQueryRepositoryImpl implements DeliveryQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Delivery> findByDeliveryId(UUID deliveryId) {
		return Optional.ofNullable(queryFactory
				.selectFrom(delivery)
				.leftJoin(delivery.deliveryRoutes, deliveryRoute).fetchJoin()
				.where(delivery.deliveryId.eq(deliveryId))
				.fetchOne());
	}

	@Override
	public Page<Delivery> findAll(DeliveryQueryCondition condition, Pageable pageable) {
		BooleanBuilder builder = new BooleanBuilder();

		if (condition.getOrderId() != null) {
			builder.and(delivery.orderId.eq(condition.getOrderId()));
		}
		if (condition.getStatus() != null) {
			builder.and(delivery.status.eq(condition.getStatus()));
		}
		if (condition.getDepartureHubId() != null) {
			builder.and(delivery.origin.departureHubId.eq(condition.getDepartureHubId()));
		}
		if (condition.getReceiptHubId() != null) {
			builder.and(delivery.destination.receiptHubId.eq(condition.getReceiptHubId()));
		}
		if (condition.getHubCourierId() != null) {
			builder.and(delivery.hubCourierId.eq(condition.getHubCourierId()));
		}
		if (condition.getCompanyCourierId() != null) {
			builder.and(delivery.companyCourierId.eq(condition.getCompanyCourierId()));
		}
		
		// 배송 담당자 본인 관련 필터링 (OR 조건 적용)
		if (condition.getSearchCourierId() != null) {
			builder.and(delivery.hubCourierId.eq(condition.getSearchCourierId())
					.or(delivery.companyCourierId.eq(condition.getSearchCourierId())));
		}

		List<Delivery> content = queryFactory
				.selectFrom(delivery)
				.where(builder)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(delivery.createdAt.desc())
				.fetch();

		Long total = queryFactory
				.select(delivery.count())
				.from(delivery)
				.where(builder)
				.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0L);
	}

	@Override
	public Optional<DeliveryRoute> findByDeliveryRouteId(UUID deliveryRouteId) {
		return Optional.ofNullable(queryFactory
				.selectFrom(deliveryRoute)
				.where(deliveryRoute.deliveryRouteId.eq(deliveryRouteId))
				.fetchOne());
	}

	@Override
	public List<DeliveryRoute> findByOrderIdAndDeliveryId(UUID orderId, UUID deliveryId) {
		return queryFactory
				.selectFrom(deliveryRoute)
				.join(deliveryRoute.delivery, delivery)
				.where(delivery.orderId.eq(orderId), delivery.deliveryId.eq(deliveryId))
				.orderBy(deliveryRoute.routeEdge.sequence.asc())
				.fetch();
	}
}
