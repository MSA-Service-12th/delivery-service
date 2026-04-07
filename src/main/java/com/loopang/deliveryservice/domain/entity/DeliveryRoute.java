package com.loopang.deliveryservice.domain.entity;

import com.loopang.common.domain.BaseUserEntity;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.vo.deliveryroute.CourierInfo;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRelation;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRouteStatus;
import com.loopang.deliveryservice.domain.vo.deliveryroute.RouteEdge;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLRestriction;

import java.util.Objects;
import java.util.UUID;

@Table(name = "p_delivery_route")
@Entity
@Getter
@SQLRestriction("deleted_at is null")
@NoArgsConstructor
public class DeliveryRoute extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryRouteId;

    @Version
    private Long version;

    // 배송 엔티티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Embedded
    private RouteEdge routeEdge;

    @Embedded
    private CourierInfo courierInfo;

    @Enumerated(EnumType.STRING)
    private DeliveryRelation deliveryRelation;  // 업체->허브 또는 허브->업체

    @Enumerated(EnumType.STRING)
    private DeliveryRouteStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    private DeliveryRoute(RouteEdge routeEdge) {
        this.routeEdge = routeEdge;
        this.status = DeliveryRouteStatus.WAITING_AT_HUB;
    }

    // 배송경로 생성
    public static DeliveryRoute create(RouteEdge routeEdge) {
        return DeliveryRoute.builder()
                .routeEdge(routeEdge)
                .build();
    }

    // 배송경로-배송 연관관계 동기화
    public void updateDelivery(Delivery delivery) {
        this.delivery = Objects.requireNonNull(delivery);
    }

    // 배송경로 예상 시간/예상 거리 업데이트
    public void updateExpected(int expectedTime, double expectedDistance) {
        if (this.routeEdge == null) {
            this.routeEdge = RouteEdge.from(0, null, null, expectedDistance, expectedTime);
        } else {
            this.routeEdge = RouteEdge.from(
                    this.routeEdge.getSequence(),
                    this.routeEdge.getFromLocationId(),
                    this.routeEdge.getToLocationId(),
                    expectedDistance,
                    expectedTime
            );
        }
    }

    // 배송경로별 배송담당자 배정
    public void assignCourier(CourierInfo courierInfo) {
        this.courierInfo = courierInfo;
    }

    public void updateRelation(DeliveryRelation relation) {
        this.deliveryRelation = relation;
    }

    // 배송경로 상태 전이
    public void transitToHub() {
        validateTransition(DeliveryRouteStatus.IN_TRANSIT_TO_HUB);
        this.status = DeliveryRouteStatus.IN_TRANSIT_TO_HUB;
    }

    public void arrivedAtDestination() {
        validateTransition(DeliveryRouteStatus.ARRIVED_AT_DEST_HUB);
        this.status = DeliveryRouteStatus.ARRIVED_AT_DEST_HUB;
    }

    public void transitToCompany() {
        validateTransition(DeliveryRouteStatus.IN_TRANSIT_TO_COMPANY);
        this.status = DeliveryRouteStatus.IN_TRANSIT_TO_COMPANY;
    }

    public void completed() {
        validateTransition(DeliveryRouteStatus.COMPLETED);
        this.status = DeliveryRouteStatus.COMPLETED;
    }

    public void cancel() {
        validateTransition(DeliveryRouteStatus.CANCELLED);
        this.status = DeliveryRouteStatus.CANCELLED;
    }

    // 강제 취소 (상태 전이 검증 생략)
    public void forceCancel() {
        this.status = DeliveryRouteStatus.CANCELLED;
    }

    private void validateTransition(DeliveryRouteStatus next) {
        if (this.isDeleted()) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_ALREADY_DELETED);
        }
        if (!this.status.checkTransition(next)) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_INVALID_STATUS_TRANSITION);
        }
    }

}
