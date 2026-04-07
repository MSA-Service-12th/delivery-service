package com.loopang.deliveryservice.domain.exception;

import com.loopang.common.exception.ErrorCodeSpec;
import org.springframework.http.HttpStatus;

public enum DeliveryErrorCode implements ErrorCodeSpec {

	DELIVERY_ACCESS_DENIED("DELIVERY_001", HttpStatus.FORBIDDEN, "해당 배송에 관한 권한이 없습니다."),
	DELIVERY_FORBIDDEN("DELIVERY_002", HttpStatus.FORBIDDEN, "해당 작업을 수행할 권한이 없습니다."),
	DELIVERY_UNAUTHORIZED("DELIVERY_003", HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
	DELIVERY_NOT_FOUND("DELIVERY_004", HttpStatus.NOT_FOUND, "배송 정보를 찾을 수 없습니다."),
	DELIVERY_ROUTE_NOT_FOUND("DELIVERY_005", HttpStatus.NOT_FOUND, "배송 경로 정보를 찾을 수 없습니다."),
	DELIVERY_INVALID_STATUS_TRANSITION("DELIVERY_006", HttpStatus.BAD_REQUEST, "배송 상태를 변경할 수 없습니다."),
	DELIVERY_ALREADY_DELETED("DELIVERY_007", HttpStatus.BAD_REQUEST, "이미 삭제된 배송정보입니다."),
	DELIVERY_INVALID_COURIER_TYPE("DELIVERY_008", HttpStatus.BAD_REQUEST, "유효하지 않은 배송관리자 타입입니다."),
	DELIVERY_PREDECESSOR_NOT_COMPLETED("DELIVERY_009", HttpStatus.BAD_REQUEST, "이전 배송 구간이 완료되지 않았습니다.");

	private final String code;
	private final HttpStatus status;
	private final String message;

	DeliveryErrorCode(String code, HttpStatus status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getField() {
		return "delivery";
	}
}
