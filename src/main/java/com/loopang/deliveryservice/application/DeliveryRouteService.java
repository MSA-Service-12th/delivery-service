package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.entity.DeliveryRoute;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.repository.DeliveryRouteRepository;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRouteStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryRouteService {

    private final DeliveryRouteRepository deliveryRouteRepository;

    /**
     * 배송 구간의 상태를 변경합니다.
     * 한 구간이 완료되면 애그리거트 루트(Delivery)가 다음 담당자로의 인계를 처리합니다.
     */
    public void updateRouteStatus(UUID routeId, DeliveryRouteStatus nextStatus) {
        // 1. 배송 구간 조회
        DeliveryRoute route = deliveryRouteRepository.findById(routeId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        // 2. 애그리거트 루트(Delivery) 획득
        Delivery delivery = route.getDelivery();

        // 3. 애그리거트 루트를 통해 상태 변경 및 비즈니스 규칙(담당자 인계 등) 수행
        delivery.updateRouteStatus(routeId, nextStatus);

        // Dirty Checking에 의해 변경사항이 DB에 반영됨
    }
}
