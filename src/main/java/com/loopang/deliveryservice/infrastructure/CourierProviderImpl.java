package com.loopang.deliveryservice.infrastructure;

import com.loopang.deliveryservice.domain.service.CourierProvider;
import com.loopang.deliveryservice.domain.service.dto.CourierData;
import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.deliveryroute.CourierInfo;
import com.loopang.deliveryservice.infrastructure.client.CourierFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class CourierProviderImpl implements CourierProvider {

	private final CourierFeignClient courierFeignClient;
	private final Map<String, AtomicLong> counterMap = new ConcurrentHashMap<>();

	@Override
	public CourierInfo getCourierByRoundRobin(UUID hubId, CourierType type) {
		List<CourierData> candidates = courierFeignClient.getCouriers(hubId, type.name()).getData();

		if (candidates == null || candidates.isEmpty()) {
			throw new RuntimeException("해당 허브 및 타입에 가용한 배송담당자가 없습니다.");
		}

		String key = hubId.toString() + ":" + type.name();
		long currentCount = counterMap.computeIfAbsent(key, k -> new AtomicLong(0)).getAndIncrement();

		int index = (int) (currentCount % candidates.size());
		CourierData selected = candidates.get(index);

		return CourierInfo.of(selected);
	}

	@Override
	public CourierInfo getCourier(UUID courierId) {
		CourierData data = courierFeignClient.getCourier(courierId).getData();

		return CourierInfo.of(data);
	}
}
