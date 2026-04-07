package com.loopang.deliveryservice.domain.entity;

import com.loopang.common.domain.BaseUserEntity;
import com.loopang.deliveryservice.domain.event.payload.OrderAcceptedPayload;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.service.dto.CourierData;
import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
import com.loopang.deliveryservice.domain.vo.delivery.Destination;
import com.loopang.deliveryservice.domain.vo.delivery.Origin;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRelation;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRouteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_delivery")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "delivery_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    // 출발지(공급업체) 허브 정보
    @Embedded
    private Origin origin;

    // 목적지(수령업체) 허브 정보
    @Embedded
    private Destination destination;

    @Column(name = "hub_manager_Id", nullable = false)
    private UUID hubManagerId;

    @Column(name = "hub_courier_id")
    private UUID hubCourierId;

    @Column(name = "company_courier_id")
    private UUID companyCourierId;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryRoute> deliveryRoutes;

    @Builder(access = AccessLevel.PRIVATE)
    private Delivery(UUID orderId, Origin origin, Destination destination, UUID hubManagerId) {
        this.orderId = orderId;
        this.status = DeliveryStatus.START_DELIVERY;
        this.origin = origin;
        this.destination = destination;
        this.hubManagerId = hubManagerId;
        this.deliveryRoutes = new ArrayList<>();
    }

    // 배송 엔티티 생성

    public static Delivery from(OrderAcceptedPayload payload) {
        return Delivery.builder()
                .orderId(payload.orderId())
                .origin(payload.origin())
                .destination(payload.destination())
                .hubManagerId(payload.hubManagerId())
                .build();
    }

    // 배송담당자 지정

    public void updateCurrentCourier(UUID courierId, CourierType type) {
        if (type == CourierType.HUB) {
            this.hubCourierId = courierId;
            this.companyCourierId = null;
        } else {
            this.companyCourierId = courierId;
            this.hubCourierId = null;
        }
    }

    public void updateCourierId(CourierData courierData) {
        if (CourierType.valueOf(courierData.deliveryChargeType()) == CourierType.HUB) {
            this.hubCourierId = courierData.courierId();
        } else {
            this.companyCourierId = courierData.courierId();
        }
    }

    // 배송상태 전이 관련

    public void onDelivery() {
        validateTransition(DeliveryStatus.ON_DELIVERY);
        this.status = DeliveryStatus.ON_DELIVERY;
    }

    public void complete() {
        validateTransition(DeliveryStatus.COMPLETED);
        this.status = DeliveryStatus.COMPLETED;
    }

    public void cancel() {
        validateTransition(DeliveryStatus.CANCELLED);
        this.status = DeliveryStatus.CANCELLED;
    }

    private void validateTransition(DeliveryStatus next) {
        if (this.isDeleted()) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_ALREADY_DELETED);
        }
        if (!this.status.checkTransition(next)) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_INVALID_STATUS_TRANSITION);
        }
    }

    // 배송경로 추가
    public void addDeliveryRoute(DeliveryRoute nextRoute) {
        this.deliveryRoutes.add(nextRoute);
        nextRoute.updateDelivery(this);
    }

    // [애그리거트 루트 기능] 특정 구간의 상태 변경 및 담당자 인계 제어
    public void updateRouteStatus(UUID routeId, DeliveryRouteStatus nextStatus) {
        DeliveryRoute targetRoute = this.deliveryRoutes.stream()
                .filter(r -> r.getDeliveryRouteId().equals(routeId))
                .findFirst()
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        // 1. 해당 구간의 상태 전이 수행
        applyStatusChange(targetRoute, nextStatus);

        // 2. 구간 완료 시 다음 구간 담당자로 인계
        if (nextStatus == DeliveryRouteStatus.COMPLETED) {
            handoverToNextCourier(targetRoute.getRouteEdge().getSequence());
        }
    }

    private void applyStatusChange(DeliveryRoute route, DeliveryRouteStatus nextStatus) {
        switch (nextStatus) {
            case IN_TRANSIT_TO_HUB -> route.transitToHub();
            case ARRIVED_AT_DEST_HUB -> route.arrivedAtDestination();
            case IN_TRANSIT_TO_COMPANY -> route.transitToCompany();
            case COMPLETED -> route.completed();
            case CANCELLED -> route.cancel();
            default -> throw new DeliveryException(DeliveryErrorCode.DELIVERY_INVALID_STATUS_TRANSITION);
        }
    }

    private void handoverToNextCourier(int currentSequence) {
        this.deliveryRoutes.stream()
                .filter(r -> r.getRouteEdge().getSequence() == currentSequence + 1)
                .findFirst()
                .ifPresent(nextRoute -> {
                    UUID nextCourierId = nextRoute.getCourierInfo().getCourierId();
                    CourierType type = (nextRoute.getDeliveryRelation() == DeliveryRelation.HUB_TO_HUB)
                            ? CourierType.HUB : CourierType.COMPANY;
                    this.updateCurrentCourier(nextCourierId, type);
                });
    }
}
