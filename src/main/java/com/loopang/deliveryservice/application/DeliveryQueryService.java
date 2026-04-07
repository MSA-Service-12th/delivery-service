package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.repository.DeliveryQueryRepository;
import com.loopang.deliveryservice.domain.vo.UserType;
import com.loopang.deliveryservice.infrastructure.persistence.DeliveryQueryCondition;
import com.loopang.deliveryservice.presentation.dto.DeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryQueryService {

    private final DeliveryQueryRepository deliveryRepository;

    // 단건 조회
    public DeliveryResponseDto getDelivery(UUID deliveryId, String userId, String userRole) {
        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        // [권한 규칙 적용] 
        // 1. 모든 로그인 사용자 조회 가능 (기본)
        // 2. 단, 배송 담당자는 자신이 담당하는 배송만 조회 가능
        UserType type = UserType.from(userRole);
        if (type == UserType.DELIVERY) {
            UUID userUuid = UUID.fromString(userId);
            if (!userUuid.equals(delivery.getHubCourierId()) && !userUuid.equals(delivery.getCompanyCourierId())) {
                throw new DeliveryException(DeliveryErrorCode.DELIVERY_ACCESS_DENIED);
            }
        }

        return DeliveryResponseDto.from(delivery);
    }

    // 목록 조회
    public Page<DeliveryResponseDto> getDeliveries(DeliveryQueryCondition condition, Pageable pageable, String userId, String userRole) {
        UserType type = UserType.from(userRole);
        
        // [권한 규칙 적용] 배송 담당자는 자신의 담당 배송만 조회 가능하도록 필터링 강제
        if (type == UserType.DELIVERY) {
            condition.setSearchCourierId(UUID.fromString(userId));
        }

        return deliveryRepository.findAll(condition, pageable)
                .map(DeliveryResponseDto::from);
    }
}
