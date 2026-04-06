package com.loopang.deliveryservice.domain.event;

import com.loopang.deliveryservice.domain.entity.Delivery;

public interface DeliveryEvents {

	void created(Delivery delivery);
	void statusUpdated(Delivery delivery);
}
