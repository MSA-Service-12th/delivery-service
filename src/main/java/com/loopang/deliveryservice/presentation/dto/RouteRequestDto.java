package com.loopang.deliveryservice.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class RouteRequestDto {
    private UUID deliveryId;
}
