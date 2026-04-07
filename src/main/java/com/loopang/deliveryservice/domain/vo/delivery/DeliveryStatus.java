package com.loopang.deliveryservice.domain.vo.delivery;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {

    START_DELIVERY("배송 시작"),
    ON_DELIVERY("배송 중"),
    COMPLETED("배송 완료"),
    CANCELLED("배송 취소"),

    ;

    private final String description;

    public boolean checkTransition(DeliveryStatus target) {
        return switch (this) {
            case START_DELIVERY -> target == ON_DELIVERY || target == CANCELLED;
            case ON_DELIVERY -> target == COMPLETED;
            default -> false;
        };
    }
}
