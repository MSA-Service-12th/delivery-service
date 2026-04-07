package com.loopang.deliveryservice.domain.service;

import com.loopang.common.response.CommonResponse;
import com.loopang.deliveryservice.domain.service.dto.CoordinateData;

import java.util.UUID;

public interface CompanyProvider {

	CommonResponse<CoordinateData> getCoordinate(UUID companyId);
}
