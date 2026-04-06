package com.loopang.deliveryservice.domain.service.dto;

import java.util.UUID;

public record UserData(
		UUID userId,
		String userName,
		String email,
		String slackId,
		UUID hubId,
		String hubName,
		String role
) { }
