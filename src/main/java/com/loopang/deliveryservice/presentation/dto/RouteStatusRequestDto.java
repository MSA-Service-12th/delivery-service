package com.loopang.deliveryservice.presentation.dto;

import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRouteStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RouteStatusRequestDto {
    private DeliveryRouteStatus status;
}
