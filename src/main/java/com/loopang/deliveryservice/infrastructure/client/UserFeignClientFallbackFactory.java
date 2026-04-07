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
				log.error("[User Service Fallback] 사용자 단건 조회 중 장애 발생 - User ID: {}, 사유: {}",
						userId, cause.getMessage(), cause);
				throw new DeliveryException(
						HttpStatus.SERVICE_UNAVAILABLE,
						"사용자 서비스(user-service) 단건 조회에 실패했습니다. 잠시 후 다시 시도해 주세요.",
						"user-service"
				);
			}

			@Override
			public CommonResponse<List<UserData>> getUserDataList(String userType, UUID hubId) {
				log.error("[User Service Fallback] 사용자 목록 조회 중 장애 발생 - Type: {}, Hub ID: {}, 사유: {}",
						userType, hubId, cause.getMessage(), cause);
				throw new DeliveryException(
						HttpStatus.SERVICE_UNAVAILABLE,
						"사용자 서비스(user-service) 목록 조회에 실패했습니다. 잠시 후 다시 시도해 주세요.",
						"user-service"
				);
			}
		};
	}
}
