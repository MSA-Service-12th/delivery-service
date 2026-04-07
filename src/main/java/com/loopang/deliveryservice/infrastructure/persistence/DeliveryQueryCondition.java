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
}
