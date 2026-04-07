package com.loopang.deliveryservice.infrastructure;

import com.loopang.deliveryservice.domain.service.RouteProvider;
import com.loopang.deliveryservice.domain.service.dto.request.RouteRequestData;
import com.loopang.deliveryservice.domain.service.dto.RouteResultData;
import com.loopang.deliveryservice.infrastructure.client.RouteFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteProviderImpl implements RouteProvider {

	private final RouteFeignClient routeFeignClient;

	@Override
	public RouteResultData getRouteResultData(RouteRequestData request) {
		return routeFeignClient.getRouteResultData(request).getData();
	}
}
