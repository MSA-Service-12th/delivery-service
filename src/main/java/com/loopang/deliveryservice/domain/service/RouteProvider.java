package com.loopang.deliveryservice.domain.service;

import com.loopang.deliveryservice.domain.service.dto.request.RouteRequestData;
import com.loopang.deliveryservice.domain.service.dto.RouteResultData;

public interface RouteProvider {

	RouteResultData getRouteResultData(RouteRequestData request);
}
