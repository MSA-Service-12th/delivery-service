package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.entity.DeliveryRoute;
import com.loopang.deliveryservice.domain.event.DeliveryEvents;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.repository.DeliveryRouteRepository;
import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRelation;
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

    /**
     * 특정 배송 구간의 상태를 변경하고, 그에 따른 전체 배송 상태 동기화 및 담당자 인계를 수행합니다.
     * (기존 Delivery 엔티티에 과중되었던 오케스트레이션 로직을 애플리케이션 계층으로 분리)
     */
    public void updateRouteStatus(UUID routeId, DeliveryRouteStatus nextStatus) {
        // 1. 배송 구간 및 애그리거트 루트(Delivery) 조회
        DeliveryRoute route = deliveryRouteRepository.findById(routeId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));
        Delivery delivery = route.getDelivery();

        // 2. [순차 진행 검증] 이전 구간이 완료되었는지 확인
        if (requiresOrderedProgress(nextStatus) && hasUnfinishedPredecessor(delivery, route)) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_PREDECESSOR_NOT_COMPLETED);
        }

        // 3. 해당 구간의 상태 변경 수행 (Entity 내부 상태 전이 규칙 적용)
        applyRouteStatusChange(route, nextStatus);

        // 4. 전체 배송 상태(DeliveryStatus) 동기화
        syncOverallDeliveryStatus(delivery, route, nextStatus);

        // 5. 구간 완료 시 다음 구간 담당자로 인계
        if (nextStatus == DeliveryRouteStatus.COMPLETED) {
            handoverToNextCourier(delivery, route.getRouteEdge().getSequence());
        }

        // 6. 배송이 최종 완료된 경우 주문 도메인으로 알림 발행
        if (delivery.getStatus() == DeliveryStatus.COMPLETED) {
            deliveryEvents.statusUpdated(delivery);
        }
    }

    private boolean requiresOrderedProgress(DeliveryRouteStatus nextStatus) {
        return nextStatus == DeliveryRouteStatus.IN_TRANSIT_TO_HUB
                || nextStatus == DeliveryRouteStatus.ARRIVED_AT_DEST_HUB
                || nextStatus == DeliveryRouteStatus.IN_TRANSIT_TO_COMPANY
                || nextStatus == DeliveryRouteStatus.COMPLETED;
    }

    private boolean hasUnfinishedPredecessor(Delivery delivery, DeliveryRoute targetRoute) {
        int sequence = targetRoute.getRouteEdge().getSequence();
        return delivery.getDeliveryRoutes().stream()
                .filter(r -> r.getRouteEdge().getSequence() < sequence)
                .anyMatch(r -> r.getStatus() != DeliveryRouteStatus.COMPLETED);
    }

    private void applyRouteStatusChange(DeliveryRoute route, DeliveryRouteStatus nextStatus) {
        switch (nextStatus) {
            case IN_TRANSIT_TO_HUB -> route.transitToHub();
            case ARRIVED_AT_DEST_HUB -> route.arrivedAtDestination();
            case IN_TRANSIT_TO_COMPANY -> route.transitToCompany();
            case COMPLETED -> route.completed();
            case CANCELLED -> route.cancel();
            default -> throw new DeliveryException(DeliveryErrorCode.DELIVERY_INVALID_STATUS_TRANSITION);
        }
    }

    private void syncOverallDeliveryStatus(Delivery delivery, DeliveryRoute route, DeliveryRouteStatus nextStatus) {
        // 이동 시작 시 전체 상태를 '배송 중'으로 변경
        if (delivery.getStatus() == DeliveryStatus.START_DELIVERY && 
           (nextStatus == DeliveryRouteStatus.IN_TRANSIT_TO_HUB || nextStatus == DeliveryRouteStatus.IN_TRANSIT_TO_COMPANY)) {
            delivery.onDelivery();
        }

        // 마지막 구간 완료 시 전체 상태를 '배송 완료'로 변경
        if (nextStatus == DeliveryRouteStatus.COMPLETED && isLastRoute(delivery, route)) {
            delivery.complete();
        }

        // 구간 취소 시 전체 상태를 '배송 취소'로 변경
        if (nextStatus == DeliveryRouteStatus.CANCELLED) {
            delivery.cancel();
        }
    }

    private boolean isLastRoute(Delivery delivery, DeliveryRoute route) {
        int maxSequence = delivery.getDeliveryRoutes().stream()
                .mapToInt(r -> r.getRouteEdge().getSequence())
                .max()
                .orElse(0);
        return route.getRouteEdge().getSequence() == maxSequence;
    }

    private void handoverToNextCourier(Delivery delivery, int currentSequence) {
        delivery.getDeliveryRoutes().stream()
                .filter(r -> r.getRouteEdge().getSequence() == currentSequence + 1)
                .findFirst()
                .ifPresent(nextRoute -> {
                    UUID nextCourierId = nextRoute.getCourierInfo().getCourierId();
                    CourierType type = (nextRoute.getDeliveryRelation() == DeliveryRelation.HUB_TO_HUB)
                            ? CourierType.HUB : CourierType.COMPANY;
                    delivery.updateCurrentCourier(nextCourierId, type);
                });
    }
}
