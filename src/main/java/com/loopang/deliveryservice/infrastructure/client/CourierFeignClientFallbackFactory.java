package com.loopang.deliveryservice.infrastructure.client;

import com.loopang.common.response.CommonResponse;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.service.dto.CourierData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class CourierFeignClientFallbackFactory implements FallbackFactory<CourierFeignClient> {

	@Override
	public CourierFeignClient create(Throwable cause) {
		return new CourierFeignClient() {
			@Override
			public CommonResponse<List<CourierData>> getCouriers(UUID hubId, String courierType) {
				log.error("[Courier Service Fallback] 배송담당자 목록 조회 중 장애 발생 - Hub ID: {}, Type: {}, 사유: {}",
						hubId, courierType, cause.getMessage(), cause);
				throw new DeliveryException(
						HttpStatus.SERVICE_UNAVAILABLE,
						"배송담당자 서비스(user-service) 목록 조회에 실패했습니다. 잠시 후 다시 시도해 주세요.",
						"user-service"
				);
			}

			@Override
			public CommonResponse<CourierData> getCourier(UUID courierId) {
				log.error("[Courier Service Fallback] 배송담당자 단건 조회 중 장애 발생 - Courier ID: {}, 사유: {}",
						courierId, cause.getMessage(), cause);
				throw new DeliveryException(
						HttpStatus.SERVICE_UNAVAILABLE,
						"배송담당자 서비스(user-service) 단건 조회에 실패했습니다. 잠시 후 다시 시도해 주세요.",
						"user-service"
				);
			}
		};
	}
}
