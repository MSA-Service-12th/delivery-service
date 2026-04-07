package com.loopang.deliveryservice.domain.event.payload;

import com.loopang.deliveryservice.domain.vo.delivery.Destination;
import com.loopang.deliveryservice.domain.vo.delivery.Origin;

import java.util.UUID;

// 주문 승인 완료 시 주문 -> 배송 방향으로 이벤트 발송
public record OrderAcceptedPayload(
		UUID orderId,
		UUID supplierId,
		UUID supplierHubId,
		String supplierHubAddress,
		UUID receiverHubId,
		String receiverHubAddress,
		UUID receiverId,
		String receiverName,
		String receiverAddress,
		String receiverSlackId,
		UUID hubManagerId	// 허브관리자 ID
) {
	public Origin origin() {
		return Origin.of(supplierId, supplierHubId, supplierHubAddress);
	}

	public Destination destination() {
		return Destination.of(
				receiverHubId,
				receiverHubAddress,
				receiverId,
				receiverName,
				receiverAddress,
				receiverSlackId
		);
	}
}
