package com.loopang.deliveryservice.infrastructure.client;

import com.loopang.deliveryservice.domain.exception.DeliveryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RouteFeignClientFallbackFactory implements FallbackFactory<RouteFeignClient> {

	@Override
	public RouteFeignClient create(Throwable cause) {
		return request -> {
			log.error("[Route Service Fallback] 허브 간 경로 계산 중 장애 발생 - From: {}, To: {}, 사유: {}",
					request.fromHubId(), request.toHubId(), cause.getMessage(), cause);
			
			throw new DeliveryException(
					HttpStatus.SERVICE_UNAVAILABLE,
					"경로 계산 서비스(route-service) 호출에 실패했습니다. 잠시 후 다시 시도해 주세요.",
					"route-service"
			);
		};
	}
}
