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
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Embeddable
@Entity
@Getter
@Table(name = "p_delivery")
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

    // 출발지(공급업체) 정보
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

    public static Delivery from(OrderAcceptedPayload payload) {
        return Delivery.builder()
                .orderId(payload.orderId())
                .origin(payload.origin())
                .destination(payload.destination())
                .hubManagerId(payload.hubManagerId())
                .build();
    }

    // 배송담당자 지정

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
}
