package com.loopang.deliveryservice.domain.vo.delivery;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Getter
@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Destination {

	// 수령업체 허브 정보

	@Column(name = "receipt_hub_id", nullable = false)
	private UUID receiptHubId;

	@Column(name = "receipt_hub_address", nullable = false)
	private String receiptHubAddress;

	// 수령업체 정보

	@Column(name = "receipt_company_id", nullable = false)
	private UUID receiptCompanyId;

	@Column(name = "receipt_company_name", nullable = false, length = 100)
	private String receiptCompanyName;

	@Column(name = "receipt_company_address", nullable = false)
	private String receiptCompanyAddress;

	@Column(name = "receipt_company_slack_id", nullable = false, length = 50)
	private String receiptCompanySlackId;

	public static Destination of(
			UUID receiptHubId, String receiptHubAddress,
			UUID receiptCompanyId, String receiptCompanyName,
			String receiptCompanyAddress, String receiptCompanySlackId) {

		return Destination.builder()
				.receiptHubId(receiptHubId)
				.receiptHubAddress(receiptHubAddress)
				.receiptCompanyId(receiptCompanyId)
				.receiptCompanyName(receiptCompanyName)
				.receiptCompanyAddress(receiptCompanyAddress)
				.receiptCompanySlackId(receiptCompanySlackId)
				.build();
	}
}
