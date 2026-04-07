package com.loopang.deliveryservice.domain.vo.deliveryroute;

import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteEdge {

	@Column(name = "sequence", nullable = false)
	private Integer sequence;

	@Column(name = "from_location_id", nullable = false)
	private UUID fromLocationId;

	@Column(name = "to_location_id", nullable = false)
	private UUID toLocationId;

	@Column(name = "expected_distance", nullable = false)
	private double expectedDistance;    // 예상 거리(km 단위)

	@Column(name = "expected_time", nullable = false)
	private int expectedTime;

	public static RouteEdge from(int sequence, UUID from, UUID to, double distance, int duration) {
		validateRoute(sequence, from, to, distance, duration);
		return new RouteEdge(
				sequence,
				from,
				to,
				distance,
				duration
		);
	}

	private static void validateRoute(int sequence, UUID from, UUID to, double distance, int duration) {
		if (sequence < 0) {
			throw new DeliveryException(DeliveryErrorCode.DELIVERY_INVALID_ROUTE);
		}
		if (from == null || to == null) {
			throw new DeliveryException(DeliveryErrorCode.DELIVERY_INVALID_ROUTE);
		}
		if (distance < 0 || duration < 0) {
			throw new DeliveryException(DeliveryErrorCode.DELIVERY_INVALID_ROUTE);
		}
	}
}
