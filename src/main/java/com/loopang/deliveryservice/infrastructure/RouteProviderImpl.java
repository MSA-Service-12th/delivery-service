package com.loopang.deliveryservice.infrastructure;

import com.loopang.common.response.CommonResponse;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.service.RouteProvider;
import com.loopang.deliveryservice.domain.service.dto.request.RouteRequestData;
import com.loopang.deliveryservice.domain.service.dto.RouteResultData;
import com.loopang.deliveryservice.infrastructure.client.RouteFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteProviderImpl implements RouteProvider {

	private final RouteFeignClient routeFeignClient;

	@Override
	public RouteResultData getRouteResultData(RouteRequestData request) {
		CommonResponse<RouteResultData> response = routeFeignClient.getRouteResultData(request);
		if (response == null || response.getData() == null) {
			throw new DeliveryException(
					HttpStatus.SERVICE_UNAVAILABLE,
					"경로 계산 서비스의 응답이 비어 있습니다.",
					"route-service"
			);
		}
		return response.getData();
	}
}
