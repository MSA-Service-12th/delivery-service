package com.loopang.deliveryservice.domain.service.dto;

import com.loopang.deliveryservice.domain.vo.UserType;

import java.util.UUID;

public record UserData(
		UUID userId,
		String userName,
		String email,
		String slackId,
		UUID hubId,
		String hubName,
		UserType role
) { }
