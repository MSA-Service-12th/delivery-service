package com.loopang.deliveryservice.domain.event.payload;

import java.util.UUID;

// 주문 승인 완료 시 주문 -> 배송 방향으로 이벤트 발송
public record OrderAcceptedPayload(
		UUID orderId,
		UUID supplierHubId,
		String supplierHubAddress,
		UUID receiverHubId,
		String receiverHubAddress,
		UUID receiverId,
		String receiverName,
		String receiverAddress,
		String receiverSlackId,
		UUID hubChargeId	// 허브관리자 ID
) {

}
