package com.loopang.deliveryservice.domain.exception;

import com.loopang.common.exception.CustomException;
import com.loopang.common.exception.ErrorCodeSpec;
import org.springframework.http.HttpStatus;

public class DeliveryException extends CustomException {

	public DeliveryException(ErrorCodeSpec errorCode) {
		super(errorCode);
	}

	public DeliveryException(HttpStatus status, String message, String field) {
		super(status, message, field);
	}
}
