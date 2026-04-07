package com.loopang.deliveryservice.infrastructure.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompanyFeignClientFallbackFactory implements FallbackFactory<CompanyFeignClient> {

	@Override
	public CompanyFeignClient create(Throwable cause) {
		return null;
	}
}
