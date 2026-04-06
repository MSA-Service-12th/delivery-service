package com.loopang.deliveryservice.infrastructure.client;

import com.loopang.common.response.CommonResponse;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.service.dto.CourierData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CourierFeignClientFallbackFactory implements FallbackFactory<CourierFeignClient> {

	@Override
	public CourierFeignClient create(Throwable cause) {
		return new CourierFeignClient() {
			@Override
			public CommonResponse<CourierData> getCourier(UUID hubId, String courierType) {
				log.error("[Courier service Fallback] Hub ID: {} 조회 중 장애 발생, 사유: {}",
						hubId, cause.getMessage(), cause); // 발생위치 -> 파생위치를 알려줌 stackTrace
				throw new DeliveryException(
						HttpStatus.SERVICE_UNAVAILABLE,
						"Courier Service API 요청 처리 실패, 잠시 후 다시 시도해주세요.",
						"user-service"
				);
			}

			@Override
			public CommonResponse<CourierData> getCourier(UUID courierId) {
				log.error("[Courier service Fallback] Courier ID: {} 조회 중 장애 발생, 사유: {}",
						courierId, cause.getMessage(), cause); // 발생위치 -> 파생위치를 알려줌 stackTrace
				throw new DeliveryException(
						HttpStatus.SERVICE_UNAVAILABLE,
						"Courier Service API 요청 처리 실패, 잠시 후 다시 시도해주세요.",
						"user-service"
				);
			}
		};
	}
}
