package com.loopang.deliveryservice.domain.entity;

import com.loopang.common.domain.BaseUserEntity;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
import com.loopang.deliveryservice.domain.vo.deliveryroute.CourierInfo;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRelation;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRouteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class DeliveryRoute extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryRouteId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery deliveryId;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "from_location_id", nullable = false)
    private UUID fromLocationId;

    @Column(name = "to_location_id", nullable = false)
    private UUID toLocationId;

    @Column(name = "expected_distance", nullable = false)
    private double expectedDistance;    // 예상 거리(km 단위)

    @Column(name = "expected_time", nullable = false)
    private int expectedTime;        // 예상 시간(분 단위)

    @Column(name = "real_distance", nullable = false)
    private double realDistance;

    @Column(name = "real_time", nullable = false)
    private int realTime;

    @Embedded
    private CourierInfo courierInfo;

    @Enumerated(EnumType.STRING)
    private DeliveryRelation deliveryRelation;  // 업체->허브 또는 허브->업체

    @Enumerated(EnumType.STRING)
    private DeliveryRouteStatus status;

    // 배송경로 예상 시간/예상 거리
    public void updateExpected(int expectedTime, double expectedDistance) {
        this.expectedTime = expectedTime;
        this.expectedDistance = expectedDistance;
    }

    // 배송경로 실제 시간/실제 거리
    public void updateReal(int realTime, double realDistance) {
        this.realTime = realTime;
        this.realDistance = realDistance;
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

    private void validateTransition(DeliveryRouteStatus next) {
        if (this.isDeleted()) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_ALREADY_DELETED);
        }
        if (!this.status.checkTransition(next)) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_INVALID_STATUS_TRANSITION);
        }
    }
}
