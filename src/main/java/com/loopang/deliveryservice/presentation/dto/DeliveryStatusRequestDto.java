package com.loopang.deliveryservice.presentation.dto;

import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryStatusRequestDto {
    private DeliveryStatus status;
}
