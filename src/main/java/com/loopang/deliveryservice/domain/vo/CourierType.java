package com.loopang.deliveryservice.domain.vo;

import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;

import java.util.Arrays;

public enum CourierType {
	COMPANY,
	HUB,

	;

	public static CourierType find(String companyType) {
		return Arrays.stream(CourierType.values())
				.filter(type -> type.name().equals(companyType))
				.findFirst()
				.orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_INVALID_COURIER_TYPE));
	}
}
