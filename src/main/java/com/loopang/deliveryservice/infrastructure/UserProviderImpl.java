package com.loopang.deliveryservice.infrastructure;

import com.loopang.deliveryservice.domain.service.UserProvider;
import com.loopang.deliveryservice.domain.service.dto.UserData;
import com.loopang.deliveryservice.domain.vo.UserType;
import com.loopang.deliveryservice.infrastructure.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserProviderImpl implements UserProvider {

	private final UserFeignClient userFeignClient;

	@Override
	public UserData getUser(UUID userId) {
		return userFeignClient.getUserData(userId).getData();
	}

	@Override
	public List<UserData> getUserList(UserType userType, UUID hubId) {
		return userFeignClient.getUserDataList(userType.name(), hubId).getData();
	}
}
