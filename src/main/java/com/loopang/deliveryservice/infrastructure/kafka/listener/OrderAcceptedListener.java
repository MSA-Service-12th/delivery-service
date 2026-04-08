package com.loopang.deliveryservice.infrastructure.kafka.listener;

import com.loopang.common.messaging.IdempotentConsumer;
import com.loopang.common.util.JsonUtil;
import com.loopang.deliveryservice.application.DeliveryCommandService;
import com.loopang.deliveryservice.domain.event.payload.OrderAcceptedPayload;
import com.loopang.deliveryservice.infrastructure.kafka.OrderTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(OrderTopicProperties.class)
public class OrderAcceptedListener implements InboundEventListener {

	private final DeliveryCommandService deliveryCommandService;
	private final JsonUtil jsonUtil;

	@Override
	@IdempotentConsumer("order-accepted-group")
	@KafkaListener(id = "order-accepted-listener", topics = "${topics.order.accepted}", groupId = "delivery-group")
	public void onEvent(Message<String> message, Acknowledgment ack) {
		String messageId = String.valueOf(message.getHeaders().get(KafkaHeaders.RECEIVED_KEY));

		try {
			OrderAcceptedPayload payload = extractPayload(message.getPayload(), jsonUtil, OrderAcceptedPayload.class);
			
			// 로깅 및 추적성을 위한 MDC 설정
			MDC.put("orderId", payload.orderId().toString());
			MDC.put("messageId", messageId);

			log.info("[주문 승인 이벤트 수신] 배송 생성 프로세스 시작 - orderId: {}, messageId: {}", payload.orderId(), messageId);

			// 배송 생성 핵심 비즈니스 로직 수행
			deliveryCommandService.createDelivery(payload);

			ack.acknowledge();
			log.info("[주문 승인 이벤트 처리 완료] 배송 생성 성공 및 오더 서비스 통지 완료 - orderId: {}, messageId: {}", payload.orderId(), messageId);
		} catch (Exception e) {
			log.error("[주문 승인 이벤트 처리 실패] 배송 생성 중 예외 발생 - messageId: {}, error: {}", messageId, e.getMessage(), e);
			throw e; // 예외를 던져 Kafka 재시도 및 DLT 이동 유도
		} finally {
			MDC.clear();
		}
	}

	@Override
	@KafkaListener(id = "order-accepted-dlt-listener", topics = "${topics.order.accepted}.DLT", groupId = "delivery-group")
	public void handleDLT(Message<String> message, Acknowledgment ack) {
		String messageId = String.valueOf(message.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
		log.error("[DLT 수신] 배송 생성 최종 실패 메시지 도착 - messageId: {}", messageId);

		try {
			OrderAcceptedPayload payload = extractPayload(message.getPayload(), jsonUtil, OrderAcceptedPayload.class);
			
			MDC.put("orderId", payload.orderId().toString());
			MDC.put("messageId", messageId);

			log.warn("[DLT 처리 시작] 배송 생성 최종 실패로 인한 주문 강제 롤백 수행 - orderId: {}, messageId: {}", payload.orderId(), messageId);

			// 생성 DLT이므로 강제 취소(롤백) 및 보상 트랜잭션 수행
			deliveryCommandService.handleOrderRollback(payload, true);
			
			log.warn("[DLT 처리 성공] 주문 강제 롤백 및 보상 이벤트 발행 완료 - orderId: {}, messageId: {}", payload.orderId(), messageId);
		} catch (Exception e) {
			log.error("[DLT 복구 치명적 실패] 수동 확인 필요! messageId: {}, error: {}", messageId, e.getMessage(), e);
		} finally {
			MDC.clear();
			ack.acknowledge(); // DLT 메시지는 처리 여부와 관계없이 오프셋 커밋 (무한 루프 방지)
		}
	}
}
