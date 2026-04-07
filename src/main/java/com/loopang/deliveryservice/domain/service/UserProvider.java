package com.loopang.deliveryservice.domain.service;

import com.loopang.deliveryservice.domain.service.dto.UserData;
import com.loopang.deliveryservice.domain.vo.UserType;

import java.util.List;
import java.util.UUID;

public interface UserProvider {

	UserData getUser(UUID userId);
	List<UserData> getUserList(UserType userType, UUID hubId);
}
