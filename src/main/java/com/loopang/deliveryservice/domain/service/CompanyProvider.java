package com.loopang.deliveryservice.domain.service;

import com.loopang.deliveryservice.domain.service.dto.CoordinateData;

import java.util.UUID;

public interface CompanyProvider {

	CoordinateData getCoordinate(UUID companyId);
}
