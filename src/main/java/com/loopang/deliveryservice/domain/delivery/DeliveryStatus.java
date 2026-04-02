package com.loopang.deliveryservice.domain.delivery;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {

    WAITING_AT_HUB("허브 대기 중"),
    IN_TRANSIT_TO_HUB("허브 이동 중"),
    ARRIVED_AT_DEST_HUB("목적지 허브 도착"),
    IN_TRANSIT_TO_COMPANY("업체 이동 중"),
    DELIVERED("배송 완료"),
    CANCELLED("배송 취소");

    private final String description;
}
