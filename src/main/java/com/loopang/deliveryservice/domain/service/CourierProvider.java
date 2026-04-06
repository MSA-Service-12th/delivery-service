package com.loopang.deliveryservice.domain.service;

import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.deliveryroute.CourierInfo;

import java.util.UUID;

public interface CourierProvider {

	CourierInfo getCourier(UUID hubId, CourierType type);
	CourierInfo getCourier(UUID courierId);
}
