package com.loopang.deliveryservice.domain.service.dto;

import java.util.UUID;

public record CourierData(
	UUID courierId,
	UUID userId,
	String userName,
	String email,
	String slackId,
	UUID hubId,
	String hubName,
	String deliveryChargeType,
	int deliveryTurn,
	boolean enabled
) { }
