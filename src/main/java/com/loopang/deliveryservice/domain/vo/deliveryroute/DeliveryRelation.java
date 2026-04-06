package com.loopang.deliveryservice.domain.vo.deliveryroute;

import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;

import java.util.UUID;

public enum DeliveryRelation {

	HUB_BY_HUB,		// 허브->허브
	HUB_BY_COMPANY	// 업체->허브 또는 허브->업체

	;

	public static DeliveryRelation of(UUID departureId, UUID destinationId) {
		if (departureId == null && destinationId == null) {
			throw new DeliveryException(DeliveryErrorCode.DELIVERY_ALREADY_DELETED);
		}
		return (departureId != null && destinationId != null)
				? HUB_BY_COMPANY
				: HUB_BY_HUB;
	}
}
