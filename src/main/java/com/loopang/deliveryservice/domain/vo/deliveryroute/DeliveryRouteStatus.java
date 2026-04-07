package com.loopang.deliveryservice.domain.vo.deliveryroute;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryRouteStatus {

    WAITING_AT_HUB("허브 대기 중"),
    IN_TRANSIT_TO_HUB("허브 이동 중"),
    ARRIVED_AT_DEST_HUB("목적지 허브 도착"),
    IN_TRANSIT_TO_COMPANY("업체 이동 중"),
    COMPLETED("배송 완료"),
    CANCELLED("배송 취소");

    private final String description;

    public boolean checkTransition(DeliveryRouteStatus target) {
        // 어느 상태에서든 취소(CANCELLED)는 가능해야 함 (단, 이미 완료되었거나 취소된 경우는 제외)
        if (target == CANCELLED) {
            return this != COMPLETED && this != CANCELLED;
        }

        return switch (this) {
            case WAITING_AT_HUB -> target == IN_TRANSIT_TO_HUB;
            case IN_TRANSIT_TO_HUB -> target == ARRIVED_AT_DEST_HUB;
            case ARRIVED_AT_DEST_HUB -> target == IN_TRANSIT_TO_COMPANY;
            case IN_TRANSIT_TO_COMPANY -> target == COMPLETED;
            default -> false;
        };
    }
}
