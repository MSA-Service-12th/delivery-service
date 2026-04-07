package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.event.payload.OrderAcceptedPayload;
import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;

import java.util.UUID;

public interface DeliveryCommandService {

	void createDelivery(OrderAcceptedPayload payload);

	void deleteDelivery(UUID deliveryId, String userId, String userRole);

	void updateDeliveryStatus(UUID deliveryId, DeliveryStatus status, String userId, String userRole);

	void handleOrderRollback(OrderAcceptedPayload payload, boolean force);
}
