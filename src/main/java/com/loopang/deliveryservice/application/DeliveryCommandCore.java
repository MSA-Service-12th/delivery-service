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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryCommandCore {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryRouteFactory deliveryRouteFactory;

    // 배송 및 동적 계산된 경로 통합 생성 (트랜잭션 보장)
    @Transactional
    public Delivery createWithCalculatedRoutes(OrderAcceptedPayload payload, RouteResultData routeResult,
                                              CourierInfo firstCourier, List<CourierInfo> hubCouriers, CourierInfo lastCourier) {
        
        // 1. 멱등성 가드: 이미 해당 주문에 대한 배송이 존재하는지 확인
        Optional<Delivery> existingDelivery = deliveryRepository.findByOrderId(payload.orderId());
        if (existingDelivery.isPresent()) {
            return existingDelivery.get();
        }

        try {
            // 2. 응용 계층의 Factory(Assembler)를 통해 애그리거트 생성
            Delivery delivery = deliveryRouteFactory.createWithRoutes(payload, routeResult, firstCourier, hubCouriers, lastCourier);

            // 3. DB 저장 (Cascade에 의해 Route도 함께 저장)
            return deliveryRepository.save(delivery);
        } catch (DataIntegrityViolationException e) {
            // 4. 동시성 이슈로 인해 유니크 제약 조건 위반 시, 이미 생성된 데이터를 찾아 반환
            return deliveryRepository.findByOrderId(payload.orderId())
                    .orElseThrow(() -> e); // 여전히 없다면 원래 예외 던짐
        }
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

        // 배송 엔티티 소프트 삭제
        delivery.delete(deliveryId);

        // 연관된 모든 배송 구간(Route)들도 함께 소프트 삭제 처리
        if (delivery.getDeliveryRoutes() != null) {
            delivery.getDeliveryRoutes().forEach(route -> route.delete(deliveryId));
        }
    }

    // 주문 기반 취소 (보상 트랜잭션용)
    @Transactional
    public void cancelByOrderId(UUID orderId, boolean force) {
        deliveryRepository.findByOrderId(orderId)
                .ifPresent(delivery -> {
                    if (force) {
                        delivery.forceCancel();
                    } else {
                        delivery.changeStatus(DeliveryStatus.CANCELLED);
                    }
                });
    }
}
