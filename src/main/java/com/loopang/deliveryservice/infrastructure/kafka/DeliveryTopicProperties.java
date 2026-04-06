package com.loopang.deliveryservice.infrastructure.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "topics.delivery")
public record DeliveryTopicProperties(
		String created,
		String statusUpdated
) { }
