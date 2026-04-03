package com.loopang.deliveryservice.presentation.dto;

import com.loopang.deliveryservice.domain.delivery.Delivery;
import com.loopang.deliveryservice.domain.delivery.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class DeliveryResponseDto {

    private UUID deliveryId;

    private UUID orderId;

    private UUID courierId;

    private DeliveryStatus status;

    private UUID departureHubId;

    private String departureHubAddress;

    private UUID receiptHubId;

    private String receiptHubAddress;

    private UUID hubManagerId;

    private UUID receiptCompanyId;

    private String receiptCompanyName;

    private UUID receiptCompanySlackId;

    private String receiptCompanyAddress;

    private UUID hubCourierId;

    private UUID companyCourierId;

    public DeliveryResponseDto(
            UUID deliveryId,
            UUID orderId,
            UUID courierId,
            DeliveryStatus status,
            UUID departureHubId,
            String departureHubAddress,
            UUID receiptHubId,
            String receiptHubAddress,
            UUID hubManagerId,
            String receiptCompanyName,
            UUID receiptCompanySlackId,
            String receiptCompanyAddress,
            UUID hubCourierId,
            UUID companyCourierId) {
    }


    public static DeliveryResponseDto from(Delivery delivery) {
        return new DeliveryResponseDto(
                delivery.getDeliveryId(),
                delivery.getOrderId(),
                delivery.getCourierId(),
                delivery.getStatus(),
                delivery.getDepartureHubId(),
                delivery.getDepartureHubAddress(),
                delivery.getReceiptHubId(),
                delivery.getReceiptHubAddress(),
                delivery.getHubManagerId(),
                delivery.getReceiptCompanyName(),
                delivery.getReceiptCompanySlackId(),
                delivery.getReceiptCompanyAddress(),
                delivery.getHubCourierId(),
                delivery.getCompanyCourierId()





        );
    }
}