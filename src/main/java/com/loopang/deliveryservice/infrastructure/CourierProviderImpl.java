package com.loopang.deliveryservice.infrastructure;

import com.loopang.deliveryservice.domain.service.CourierProvider;
import com.loopang.deliveryservice.domain.service.dto.CourierData;
import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.deliveryroute.CourierInfo;
import com.loopang.deliveryservice.infrastructure.client.CourierFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CourierProviderImpl implements CourierProvider {

	private final CourierFeignClient courierFeignClient;

	@Override
	public CourierInfo getCourier(UUID hubId, CourierType type) {
		CourierData data = courierFeignClient.getCourier(hubId, type.name()).getData();

		return CourierInfo.of(data);
	}

	@Override
	public CourierInfo getCourier(UUID courierId) {
		CourierData data = courierFeignClient.getCourier(courierId).getData();

		return CourierInfo.of(data);
	}
}
