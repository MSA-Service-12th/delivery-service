package com.loopang.deliveryservice.domain.entity;

import com.loopang.common.domain.BaseUserEntity;
import com.loopang.deliveryservice.domain.event.payload.OrderAcceptedPayload;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
import com.loopang.deliveryservice.domain.vo.delivery.Destination;
import com.loopang.deliveryservice.domain.vo.delivery.Origin;
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

    @Version
    private Long version;

    @Column(name = "order_id", nullable = false, unique = true)
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

    // 배송 엔티티 생성 기본 팩토리
    public static Delivery from(OrderAcceptedPayload payload) {
        return Delivery.builder()
                .orderId(payload.orderId())
                .origin(payload.origin())
                .destination(payload.destination())
                .hubManagerId(payload.hubManagerId())
                .build();
    }

    // 배송담당자 지정 및 업데이트
    public void updateCurrentCourier(UUID courierId, CourierType type) {
        if (type == CourierType.HUB) {
            this.hubCourierId = courierId;
            this.companyCourierId = null;
        } else {
            this.companyCourierId = courierId;
            this.hubCourierId = null;
        }
    }

    // 배송상태 전이 관련 (도메인 규칙 검증)
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
        // 전체 취소 시 모든 하위 구간도 함께 취소 처리 (이미 완료된 것은 제외)
        if (this.deliveryRoutes != null) {
            this.deliveryRoutes.stream()
                .filter(route -> route.getStatus() != DeliveryRouteStatus.COMPLETED && route.getStatus() != DeliveryRouteStatus.CANCELLED)
                .forEach(DeliveryRoute::cancel);
        }
    }

    // 강제 취소 (상태 전이 검증 생략 및 모든 구간 강제 취소)
    public void forceCancel() {
        this.status = DeliveryStatus.CANCELLED;
        if (this.deliveryRoutes != null) {
            this.deliveryRoutes.forEach(DeliveryRoute::forceCancel);
        }
    }

    private void validateTransition(DeliveryStatus next) {
        if (this.isDeleted()) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_ALREADY_DELETED);
        }
        if (!this.status.checkTransition(next)) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_INVALID_STATUS_TRANSITION);
        }
    }

    // 배송경로 추가 및 순번 관리 (애그리거트 루트의 책임)
    public void addRouteSegment(UUID from, UUID to, double distance, int time, DeliveryRelation relation, CourierInfo courier) {
        // 1. 현재 경로 리스트 크기를 기반으로 다음 순번 결정
        int nextSequence = this.deliveryRoutes.size() + 1;

        // 2. RouteEdge VO 생성
        RouteEdge edge = RouteEdge.from(nextSequence, from, to, distance, time);

        // 3. DeliveryRoute 엔티티 생성 및 속성 설정
        DeliveryRoute route = DeliveryRoute.create(edge);
        route.updateRelation(relation);
        route.assignCourier(courier);

        // 4. 리스트 추가 및 연관관계 설정
        this.deliveryRoutes.add(route);
        route.updateDelivery(this);
    }

    // 전체 배송 상태 강제 변경 (관리자/시스템용)
    public void changeStatus(DeliveryStatus nextStatus) {
        switch (nextStatus) {
            case ON_DELIVERY -> this.onDelivery();
            case COMPLETED -> this.complete();
            case CANCELLED -> this.cancel();
            default -> throw new DeliveryException(DeliveryErrorCode.DELIVERY_INVALID_STATUS_TRANSITION);
        }
    }
}
