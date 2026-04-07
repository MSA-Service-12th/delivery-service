package com.loopang.deliveryservice.domain.service;

import com.loopang.deliveryservice.domain.service.dto.UserData;

import java.util.List;
import java.util.UUID;

public interface UserProvider {

	UserData getUser(UUID userId);
	List<UserData> getCourierList(UUID hubId);
}
