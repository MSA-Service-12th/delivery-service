package com.loopang.deliveryservice.infrastructure.event;

import com.loopang.common.event.Events;
import com.loopang.common.event.OutboxEvent;
import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.event.DeliveryEvents;
import com.loopang.deliveryservice.domain.event.payload.DeliveryUpdatePayload;
import com.loopang.deliveryservice.infrastructure.kafka.DeliveryTopicProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(DeliveryTopicProperties.class)
public class DeliveryEventsImpl implements DeliveryEvents {

	private final DeliveryTopicProperties properties;

	// 배송 엔티티 생성 직후 배송 -> 주문 방향 메시지 발송
	@Override
	public void created(Delivery delivery) {
		OutboxEvent outboxEvent = OutboxEvent.withCorrelation(
				getTraceId(),
				"DELIVERY",
				delivery.getDeliveryId(),
				properties.created(),
				DeliveryUpdatePayload.from(delivery)
		);
		Events.trigger(outboxEvent);
	}

	@Override
	public void statusUpdated(Delivery delivery) {
		OutboxEvent outboxEvent = OutboxEvent.withCorrelation(
				getTraceId(),
				"DELIVERY",
				delivery.getDeliveryId(),
				properties.statusUpdated(),
				DeliveryUpdatePayload.from(delivery)
		);
		Events.trigger(outboxEvent);
	}

	private String getTraceId() {
		String traceId = MDC.get("traceId");
		return StringUtils.hasText(traceId) ? traceId : UUID.randomUUID().toString();
	}
}
