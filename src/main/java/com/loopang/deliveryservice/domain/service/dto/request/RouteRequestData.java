package com.loopang.deliveryservice.domain.service.dto.request;

import java.util.UUID;

public record RouteRequestData(
		UUID fromHubId,
		UUID toHubId
) {
	public static RouteRequestData create(UUID fromHubId, UUID toHubId) {
		return new RouteRequestData(fromHubId, toHubId);
	}
}
