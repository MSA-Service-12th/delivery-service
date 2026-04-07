package com.loopang.deliveryservice.presentation.dto;

import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryStatusRequestDto {

    @NotNull(message = "배송 상태는 필수입니다.")
    private DeliveryStatus status;
}
