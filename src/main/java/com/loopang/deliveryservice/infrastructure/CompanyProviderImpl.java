package com.loopang.deliveryservice.infrastructure;

import com.loopang.common.response.CommonResponse;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.service.CompanyProvider;
import com.loopang.deliveryservice.domain.service.dto.CoordinateData;
import com.loopang.deliveryservice.infrastructure.client.CompanyFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CompanyProviderImpl implements CompanyProvider {

	private final CompanyFeignClient companyFeignClient;

	@Override
	public CoordinateData getCoordinate(UUID companyId) {
		CommonResponse<CoordinateData> response = companyFeignClient.getCoordinateData(companyId);
		if (response == null || response.getData() == null) {
			throw new DeliveryException(
					HttpStatus.SERVICE_UNAVAILABLE,
					"업체 위치 좌표 변환 서비스의 응답이 비어 있습니다.",
					"route-service"
			);
		}
		return response.getData();
	}
}
