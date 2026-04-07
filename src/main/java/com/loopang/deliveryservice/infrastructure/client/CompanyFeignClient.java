package com.loopang.deliveryservice.infrastructure.client;

import com.loopang.deliveryservice.domain.service.dto.CoordinateData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company-service", fallbackFactory = CompanyFeignClientFallbackFactory.class)
public interface CompanyFeignClient {

	@GetMapping("/api/companies/{companyId}")
	CoordinateData getCoordinateData(@PathVariable("companyId") UUID companyId);
}
