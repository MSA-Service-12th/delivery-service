package com.loopang.deliveryservice.presentation.dto;

import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRouteStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RouteStatusRequestDto {

    @NotNull(message = "배송 경로 상태는 필수입니다.")
    private DeliveryRouteStatus status;
}
