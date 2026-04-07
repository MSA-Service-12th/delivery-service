package com.loopang.deliveryservice.infrastructure.kafka;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "topics.delivery")
public record DeliveryTopicProperties(

		@NotNull
		String created,

		@NotNull
		String statusUpdated
) { }
