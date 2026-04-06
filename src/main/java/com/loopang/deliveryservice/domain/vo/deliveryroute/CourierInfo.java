package com.loopang.deliveryservice.domain.vo.deliveryroute;

import com.loopang.deliveryservice.domain.service.dto.CourierData;
import com.loopang.deliveryservice.domain.vo.CourierType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourierInfo {

	@Column(name = "courier_id", nullable = false)
	private UUID courierId;

	@Column(name = "courier_name", nullable = false)
	private String courierName;

	@Enumerated(EnumType.STRING)
	@Column(name = "courier_type", nullable = false, length = 30)
	private CourierType courierType;

	public static CourierInfo of(CourierData courierData) {
		CourierType courierType = CourierType.valueOf(courierData.deliveryChargeType());

		return new CourierInfo(courierData.courierId(), courierData.userName(), courierType);
	}
}
