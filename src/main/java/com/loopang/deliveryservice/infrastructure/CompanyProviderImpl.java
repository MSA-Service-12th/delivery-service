package com.loopang.deliveryservice.infrastructure;

import com.loopang.deliveryservice.domain.service.CompanyProvider;
import com.loopang.deliveryservice.domain.service.dto.CoordinateData;
import com.loopang.deliveryservice.infrastructure.client.CompanyFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CompanyProviderImpl implements CompanyProvider {

	private final CompanyFeignClient companyFeignClient;

	@Override
	public CoordinateData getCoordinate(UUID companyId) {
		return companyFeignClient.getCoordinateData(companyId);
	}
}
