package com.loopang.deliveryservice.domain.service.dto;

import java.util.UUID;

public record RouteEdgeData(
		int sequence,
		UUID fromHubId,
		UUID toHubId,
		Double distance,
		Double duration
) { }
