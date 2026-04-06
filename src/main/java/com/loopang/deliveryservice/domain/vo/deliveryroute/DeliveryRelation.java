package com.loopang.deliveryservice.domain.vo.deliveryroute;

import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;

import java.util.UUID;

public enum DeliveryRelation {

	TO_HUB,		// 업체->허브
	HUB_TO_HUB,	// 허브->허브
	FROM_HUB	// 허브->업체

	;

	public static DeliveryRelation of(UUID departureId, UUID destinationId) {
		if (departureId != null && destinationId == null) {
			return TO_HUB;
		}
		if (departureId != null) {
			return HUB_TO_HUB;
		}
		if (destinationId != null) {
			return FROM_HUB;
		}
		throw new DeliveryException(DeliveryErrorCode.DELIVERY_ALREADY_DELETED);
	}
}
