package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.event.payload.OrderAcceptedPayload;

import java.util.UUID;

public interface DeliveryCommandService {

	void createDelivery(OrderAcceptedPayload payload);

	void deleteDelivery(UUID deliveryId);

	void handleOrderRollback(OrderAcceptedPayload payload, boolean b);
}
