package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.entity.DeliveryRoute;
import com.loopang.deliveryservice.domain.event.DeliveryEvents;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.repository.DeliveryRouteRepository;
import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
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
    private final DeliveryEvents deliveryEvents;

    public void updateRouteStatus(UUID routeId, DeliveryRouteStatus nextStatus) {
        // 1. 배송 구간 조회
        DeliveryRoute route = deliveryRouteRepository.findById(routeId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        // 2. 애그리거트 루트(Delivery) 획득
        Delivery delivery = route.getDelivery();

        // 3. 애그리거트 루트를 통해 상태 변경 및 비즈니스 규칙(담당자 인계 등) 수행
        delivery.updateRouteStatus(routeId, nextStatus);

        // 4. 배송이 최종 완료된 경우(마지막 구간 완료 시) 주문 도메인으로 알림 발행
        if (delivery.getStatus() == DeliveryStatus.COMPLETED) {
            deliveryEvents.statusUpdated(delivery);
        }
    }
}
