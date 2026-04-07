package com.loopang.deliveryservice.domain.service;

import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.deliveryroute.CourierInfo;

import java.util.UUID;

public interface CourierProvider {

	// 라운드로빈 방식으로 담당자 1인 선정
	CourierInfo getCourierByRoundRobin(UUID hubId, CourierType type);
	
	// 단건 조회
	CourierInfo getCourier(UUID courierId);
}
