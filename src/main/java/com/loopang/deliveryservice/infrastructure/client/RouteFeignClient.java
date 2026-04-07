package com.loopang.deliveryservice.infrastructure.client;

import com.loopang.common.response.CommonResponse;
import com.loopang.deliveryservice.domain.service.dto.request.RouteRequestData;
import com.loopang.deliveryservice.domain.service.dto.RouteResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "route-service", contextId = "routeFeignClient", fallbackFactory = RouteFeignClientFallbackFactory.class)
public interface RouteFeignClient {

	@PostMapping("/api/hub-routes/calculate")
	CommonResponse<RouteResultData> getRouteResultData(@RequestBody RouteRequestData request);
}
