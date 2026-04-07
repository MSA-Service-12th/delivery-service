package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.repository.DeliveryRepository;
import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
import com.loopang.deliveryservice.domain.event.payload.OrderAcceptedPayload;
import com.loopang.deliveryservice.domain.service.dto.RouteResultData;
import com.loopang.deliveryservice.domain.vo.deliveryroute.CourierInfo;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryCommandCore {

    private final DeliveryRepository deliveryRepository;

    // 배송 및 동적 계산된 경로 통합 생성 (트랜잭션 보장)
    @Transactional
    public Delivery createWithCalculatedRoutes(OrderAcceptedPayload payload, 
                                              RouteResultData routeResult,
                                              CourierInfo firstCourier, 
                                              List<CourierInfo> hubCouriers, 
                                              CourierInfo lastCourier) {
        
        // Delivery 엔티티의 팩토리 메서드를 통해 애그리거트 생성
        Delivery delivery = Delivery.createWithRoutes(payload, routeResult, firstCourier, hubCouriers, lastCourier);

        // DB 저장 (Cascade에 의해 Route도 함께 저장)
        return deliveryRepository.save(delivery);
    }

    // 배송 전체 상태 강제 변경 (트랜잭션)
    @Transactional
    public Delivery updateStatus(UUID deliveryId, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        delivery.changeStatus(status);
        return delivery;
    }

    // 삭제
    @Transactional
    public void deleteDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        delivery.delete(deliveryId);
    }

    // 주문 기반 취소 (보상 트랜잭션용)
    @Transactional
    public void cancelByOrderId(UUID orderId) {
        deliveryRepository.findByOrderId(orderId)
                .ifPresent(delivery -> delivery.changeStatus(DeliveryStatus.CANCELLED));
    }
}
