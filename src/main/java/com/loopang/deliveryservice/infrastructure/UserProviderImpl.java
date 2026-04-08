package com.loopang.deliveryservice.infrastructure;

import com.loopang.common.response.CommonResponse;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.service.UserProvider;
import com.loopang.deliveryservice.domain.service.dto.UserData;
import com.loopang.deliveryservice.domain.vo.UserType;
import com.loopang.deliveryservice.infrastructure.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
	public List<UserData> getCourierList(UUID hubId) {
		CommonResponse<List<UserData>> userDataList
				= userFeignClient.getUserDataList(UserType.DELIVERY.name(), hubId);
		if (userDataList == null || userDataList.getData() == null) {
			throw new DeliveryException(
					HttpStatus.SERVICE_UNAVAILABLE,
					"배송관리자 목록 조회 서비스의 응답이 비어 있습니다.",
					"user-service"
			);
		}
		return userDataList.getData();
	}
}
