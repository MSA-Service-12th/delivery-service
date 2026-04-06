package com.loopang.deliveryservice.infrastructure.client;

import com.loopang.common.response.CommonResponse;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.service.dto.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {

	@Override
	public UserFeignClient create(Throwable cause) {
		return new UserFeignClient() {
			@Override
			public CommonResponse<UserData> getUserData(UUID userId) {
				log.error("[User service Fallback] User ID: {} 조회 중 장애 발생, 사유: {}",
						userId, cause.getMessage(), cause); // 발생위치 -> 파생위치를 알려줌 stackTrace
				throw new DeliveryException(
						HttpStatus.SERVICE_UNAVAILABLE,
						"User Service API 요청 처리 실패, 잠시 후 다시 시도해주세요.",
						"user-service"
				);
			}

			@Override
			public CommonResponse<List<UserData>> getUserDataList(String userType, UUID hubId) {
				log.error("[User service Fallback] Hub ID: {} 조회 중 장애 발생, 사유: {}",
						hubId, cause.getMessage(), cause); // 발생위치 -> 파생위치를 알려줌 stackTrace
				throw new DeliveryException(
						HttpStatus.SERVICE_UNAVAILABLE,
						"User Service API 요청 처리 실패, 잠시 후 다시 시도해주세요.",
						"user-service"
				);
			}
		};
	}
}
