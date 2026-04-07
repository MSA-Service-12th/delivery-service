package com.loopang.deliveryservice.presentation.dto;

import com.loopang.deliveryservice.domain.entity.DeliveryRoute;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRouteStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteResponseDto {
    private UUID routeId;
    private int sequence;
    private UUID fromId;
    private UUID toId;
    private DeliveryRouteStatus status;
    private UUID courierId;
    private String courierName;

    public static RouteResponseDto from(DeliveryRoute route) {
        return RouteResponseDto.builder()
                .routeId(route.getDeliveryRouteId())
                .sequence(route.getRouteEdge().getSequence())
                .fromId(route.getRouteEdge().getFromLocationId())
                .toId(route.getRouteEdge().getToLocationId())
                .status(route.getStatus())
                .courierId(route.getCourierInfo() != null ? route.getCourierInfo().getCourierId() : null)
                .courierName(route.getCourierInfo() != null ? route.getCourierInfo().getCourierName() : null)
                .build();
    }
}
