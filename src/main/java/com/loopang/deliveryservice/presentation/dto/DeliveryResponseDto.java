package com.loopang.deliveryservice.presentation.dto;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryResponseDto {

    private UUID deliveryId;
    private UUID orderId;
    private DeliveryStatus status;
    private UUID departureHubId;
    private String departureHubAddress;
    private UUID receiptHubId;
    private String receiptHubAddress;
    private UUID hubManagerId;
    private UUID receiptCompanyId;
    private String receiptCompanyName;
    private String receiptCompanySlackId;
    private String receiptCompanyAddress;
    private UUID hubCourierId;
    private UUID companyCourierId;
    private List<RouteResponseDto> routes;

    public static DeliveryResponseDto from(Delivery delivery) {
        return DeliveryResponseDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .orderId(delivery.getOrderId())
                .status(delivery.getStatus())
                .departureHubId(delivery.getOrigin().getDepartureHubId())
                .departureHubAddress(delivery.getOrigin().getDepartureHubAddress())
                .receiptHubId(delivery.getDestination().getReceiptHubId())
                .receiptHubAddress(delivery.getDestination().getReceiptHubAddress())
                .hubManagerId(delivery.getHubManagerId())
                .receiptCompanyId(delivery.getDestination().getReceiptCompanyId())
                .receiptCompanyName(delivery.getDestination().getReceiptCompanyName())
                .receiptCompanySlackId(delivery.getDestination().getReceiptCompanySlackId())
                .receiptCompanyAddress(delivery.getDestination().getReceiptCompanyAddress())
                .hubCourierId(delivery.getHubCourierId())
                .companyCourierId(delivery.getCompanyCourierId())
                .routes(delivery.getDeliveryRoutes().stream()
                        .map(RouteResponseDto::from)
                        .toList())
                .build();
    }
}
