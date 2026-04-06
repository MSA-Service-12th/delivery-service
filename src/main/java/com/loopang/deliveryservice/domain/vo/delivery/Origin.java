package com.loopang.deliveryservice.domain.vo.delivery;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Origin {

	@Column(name = "departure_hub_id", nullable = false)
	private UUID departureHubId;

	@Column(name = "departure_hub_name", nullable = false)
	private String departureHubAddress;

	public static Origin of(UUID departureHubId, String departureHubAddress) {
		return new Origin(departureHubId, departureHubAddress);
	}
}
