package com.loopang.deliveryservice.infrastructure.client;

import com.loopang.deliveryservice.domain.exception.DeliveryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompanyFeignClientFallbackFactory implements FallbackFactory<CompanyFeignClient> {

	@Override
	public CompanyFeignClient create(Throwable cause) {
		return companyId -> {
			log.error("[Company Service Fallback] 업체(회사) 조회 중 장애 발생 - Company ID: {}, 사유: {}",
					companyId, cause.getMessage(), cause);
			
			throw new DeliveryException(
					HttpStatus.SERVICE_UNAVAILABLE,
					"업체 서비스(company-service) 호출에 실패했습니다. 잠시 후 다시 시도해 주세요.",
					"company-service"
			);
		};
	}
}
