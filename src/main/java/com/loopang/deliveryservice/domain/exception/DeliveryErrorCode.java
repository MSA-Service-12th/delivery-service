package com.loopang.deliveryservice.domain.exception;

import com.loopang.common.exception.ErrorCodeSpec;
import org.springframework.http.HttpStatus;

public enum DeliveryErrorCode implements ErrorCodeSpec {
	;


	@Override
	public String getCode() {
		return "";
	}

	@Override
	public HttpStatus getStatus() {
		return null;
	}

	@Override
	public String getMessage() {
		return "";
	}

	@Override
	public String getField() {
		return "";
	}
}
