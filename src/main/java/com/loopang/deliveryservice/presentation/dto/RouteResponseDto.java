package com.loopang.deliveryservice.presentation.dto;


import com.loopang.deliveryservice.domain.delivery.DeliveryStatus;
import com.loopang.deliveryservice.domain.route.DeliveryRoute;
import java.util.UUID;

public class RouteResponseDto {

    private UUID deliveryRouteId;

    private UUID deliveryId;

    private String fromLocationId;

    private String toLocationId;

    private String sequence;

    private DeliveryRoute.DeliveryRelation deliveryRelation;

    private DeliveryStatus status;

    private String expectedDistance;

    private String expectedTime;

    private String realDistance;

    private String realTime;

    private UUID courierId;

    private String courierName;

    private DeliveryRoute.CourierType chargeType;

    public RouteResponseDto(UUID deliveryRouteId,
                            UUID deliveryId,
                            UUID courierId,
                            DeliveryStatus status,
                            String fromLocationId,
                            String toLocationId,
                            String sequence,
                            DeliveryRoute.DeliveryRelation deliveryRelation,
                            DeliveryRoute.DeliveryRelation deliveryRelation1,
                            DeliveryStatus status1,
                            String expectedDistance,
                            String expectedTime,
                            String realDistance,
                            String realTime,
                            UUID courierId1,
                            String courierName,
                            DeliveryRoute.CourierType chargeType) {
    }

    public static RouteResponseDto from(DeliveryRoute deliveryRoute) {
        return new RouteResponseDto(
                deliveryRoute.getDeliveryRouteId(),
                deliveryRoute.getDeliveryId(),
                deliveryRoute.getCourierId(),
                deliveryRoute.getStatus(),
                deliveryRoute.getFromLocationId(),
                deliveryRoute.getToLocationId(),
                deliveryRoute.getSequence(),
                deliveryRoute.getDeliveryRelation(),
                deliveryRoute.getDeliveryRelation(),
                deliveryRoute.getStatus(),
                deliveryRoute.getExpectedDistance(),
                deliveryRoute.getExpectedTime(),
                deliveryRoute.getRealDistance(),
                deliveryRoute.getRealTime(),
                deliveryRoute.getCourierId(),
                deliveryRoute.getCourierName(),
                deliveryRoute.getChargeType()
        );
    }
}
