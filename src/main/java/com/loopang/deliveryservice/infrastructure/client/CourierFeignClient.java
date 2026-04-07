package com.loopang.deliveryservice.infrastructure.client;

import com.loopang.common.response.CommonResponse;
import com.loopang.deliveryservice.domain.service.dto.CourierData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "user-service", contextId = "courierFeignClient", fallbackFactory = CourierFeignClientFallbackFactory.class)
public interface CourierFeignClient {

	@GetMapping("/internal/couriers")
	CommonResponse<List<CourierData>> getCouriers(@RequestParam("hubId") UUID hubId, @RequestParam("type") String courierType);

	@GetMapping("/internal/couriers/{courierId}")
	CommonResponse<CourierData> getCourier(@PathVariable("courierId") UUID courierId);
}
