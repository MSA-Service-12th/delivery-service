package com.loopang.deliveryservice.infrastructure.kafka.listener;

import com.loopang.common.messaging.IdempotentConsumer;
import com.loopang.common.util.JsonUtil;
import com.loopang.deliveryservice.application.DeliveryCommandService;
import com.loopang.deliveryservice.domain.event.payload.OrderAcceptedPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAcceptedListener implements InboundEventListener {

	private final DeliveryCommandService deliveryCommandService;
	private final JsonUtil jsonUtil;

	@Override
	@IdempotentConsumer("order-accepted-group")
	@KafkaListener(id = "order-accepted-listener", topics = "${topics.order.accepted}", groupId = "delivery-group")
	public void onEvent(Message<String> message, Acknowledgment ack) {
		Object messageId = message.getHeaders().get(KafkaHeaders.RECEIVED_KEY);

		try {
			OrderAcceptedPayload payload = extractPayload(message.getPayload(), jsonUtil, OrderAcceptedPayload.class);

			deliveryCommandService.createDelivery(payload);

			ack.acknowledge();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@KafkaListener(id = "order-accepted-dlt-listener", topics = "${topics.order.accepted}.DLT", groupId = "delivery-group")
	public void handleDLT(Message<String> message, Acknowledgment ack) {
		Object messageId = message.getHeaders().get(KafkaHeaders.RECEIVED_KEY);
		log.error("[DLT 수신] 배송 생성 최종 실패 메시지 도착 - messageId: {}", messageId);

		try {
			OrderAcceptedPayload payload = extractPayload(message.getPayload(), jsonUtil, OrderAcceptedPayload.class);
			// 생성 DLT이므로 강제 취소 수행
			deliveryCommandService.handleOrderRollback(payload, true);
			log.warn("[DLT 처리 성공] 배송 생성 실패로 인한 주문 강제 롤백 및 보상 이벤트 발행 완료 - orderId: {}, messageId: {}", payload.orderId(), messageId);
		} catch (Exception e) {
			log.error("[DLT 복구 치명적 실패] 수동 확인 필요! messageId: {}, error: {}", messageId, e.getMessage(), e);
		} finally {
			ack.acknowledge();
		}
	}
}
