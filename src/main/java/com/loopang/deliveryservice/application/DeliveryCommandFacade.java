package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.event.DeliveryEvents;
import com.loopang.deliveryservice.domain.event.payload.OrderAcceptedPayload;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.service.CourierProvider;
import com.loopang.deliveryservice.domain.service.RouteProvider;
import com.loopang.deliveryservice.domain.service.dto.RouteResultData;
import com.loopang.deliveryservice.domain.service.dto.request.RouteRequestData;
import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.UserType;
import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
import com.loopang.deliveryservice.domain.vo.deliveryroute.CourierInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryCommandFacade implements DeliveryCommandService {

	private final DeliveryCommandCore deliveryCommandCore;
	private final DeliveryEvents deliveryEvents;
	private final CourierProvider courierProvider;
	private final RouteProvider routeProvider;

	@Override
	public void createDelivery(OrderAcceptedPayload payload) {
		// 1. 외부 서비스 호출 (route-service) - 트랜잭션 외부에서 수행
		RouteResultData routeResult = routeProvider.getRouteResultData(
				new RouteRequestData(payload.supplierHubId(), payload.receiverHubId())
		);

		// 2. 외부 서비스 호출 (user-service) - 구간별 배송담당자 후보 배정 (라운드로빈)
		// [First] 출발지 -> 출발허브 (COMPANY)
		CourierInfo c1 = courierProvider.getCourierByRoundRobin(payload.supplierHubId(), CourierType.COMPANY);

		// [Transit] 허브 -> 허브 (HUB, 계산된 경로 엣지에 대해 각각 배정)
		List<CourierInfo> hubCouriers = routeResult.routeEdges().stream()
				.map(edge -> courierProvider.getCourierByRoundRobin(edge.fromHubId(), CourierType.HUB))
				.toList();

		// [Last] 도착허브 -> 목적지 (COMPANY)
		CourierInfo c3 = courierProvider.getCourierByRoundRobin(payload.receiverHubId(), CourierType.COMPANY);

		// 3. DB 트랜잭션 작업 위임 (Core 호출)
		Delivery savedDelivery = deliveryCommandCore.createWithCalculatedRoutes(payload, routeResult, c1, hubCouriers, c3);

		// 4. 메시지 발행 (Kafka) - 트랜잭션 완료 후 수행
		deliveryEvents.created(savedDelivery);
	}

	@Override
	public void deleteDelivery(UUID deliveryId, UUID userId, String userRole) {
		Delivery delivery = deliveryCommandCore.findById(deliveryId);

		// 마스터 관리자 및 해당 허브 관리자만 삭제 가능
		validateModification(delivery, userId, userRole);

		deliveryCommandCore.deleteDelivery(deliveryId);
	}

	@Override
	public void updateDeliveryStatus(UUID deliveryId, DeliveryStatus status, UUID userId, String userRole) {
		Delivery delivery = deliveryCommandCore.findById(deliveryId);

		// 마스터 관리자, 해당 허브 관리자, 그리고 해당 배송 담당자만 현재 배송상태 변경 가능
		validateModification(delivery, userId, userRole);

		// 1. Core를 통해 상태 변경 (트랜잭션)
		Delivery updatedDelivery = deliveryCommandCore.updateStatus(deliveryId, status);

		// 2. 상태 변경 이벤트 발행
		if (updatedDelivery.getStatus() == DeliveryStatus.COMPLETED) {
			deliveryEvents.statusUpdated(updatedDelivery);
		}
	}

	@Override
	public void handleOrderRollback(OrderAcceptedPayload payload, boolean force) {
		// 주문 기반으로 배송을 찾아 취소 처리 (보상 트랜잭션)
		deliveryCommandCore.cancelByOrderId(payload.orderId(), force);
	}

	private void validateModification(Delivery delivery, UUID userId, String userRole) {
		UserType type = UserType.from(userRole);
		if (type == UserType.MASTER) return;
		
		// 해당 허브 관리자인지 확인
		if (type == UserType.HUB && userId.equals(delivery.getHubManagerId())) {
			return;
		}
		
		// 해당 배송 담당자인지 확인
		if (type == UserType.DELIVERY
				&& (userId.equals(delivery.getHubCourierId()) || userId.equals(delivery.getCompanyCourierId()))) {
			return;
		}

		throw new DeliveryException(DeliveryErrorCode.DELIVERY_FORBIDDEN);
	}
}
