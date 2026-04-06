package com.loopang.deliveryservice.infrastructure.client;

import com.loopang.common.response.CommonResponse;
import com.loopang.deliveryservice.domain.service.dto.UserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "user-service", contextId = "courierFeignClient", fallbackFactory = UserFeignClientFallbackFactory.class)
public interface UserFeignClient {

	@GetMapping("/internal/users/{userId}")
	CommonResponse<UserData> getUserData(@PathVariable("userId") UUID userId);

	@GetMapping("/internal/users")
	CommonResponse<List<UserData>> getUserDataList(@RequestParam("role") String userType, @RequestParam("hubId") UUID hubId);
}
