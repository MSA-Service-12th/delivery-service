package com.loopang.deliveryservice.domain.service.dto;

import java.util.UUID;

public record PathData(
		int sequence,
		UUID hubId
) { }
