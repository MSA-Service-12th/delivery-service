package com.loopang.deliveryservice.domain.service.dto;

import java.util.List;
import java.util.UUID;

public record RouteResultData(
		UUID fromHubId,
		UUID toHubId,
		List<PathData> path,
		List<RouteEdgeData> routeEdges,
		Double totalDistance,
		Double totalDuration
) { }
