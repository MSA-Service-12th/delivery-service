package com.loopang.deliveryservice.infrastructure.persistence;

import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeliveryQueryCondition {
    private UUID orderId;
    private DeliveryStatus status;
    private UUID departureHubId;
    private UUID receiptHubId;
    private UUID hubCourierId;
    private UUID companyCourierId;

    // 배송원 본인 관련 배송 검색용 (hubCourierId OR companyCourierId 지원)
    private UUID searchCourierId; 
}
