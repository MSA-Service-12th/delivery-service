package com.loopang.deliveryservice.domain.delivery;

import com.loopang.deliveryservice.presentation.dto.DeliveryRequestDto;
import com.loopang.deliveryservice.presentation.dto.DeliveryResponseDto;

import java.util.List;
import java.util.UUID;

public interface DeliveryRepository {
    Delivery findById(UUID deliveryId);
    List<DeliveryRequestDto> findAll();
    void save(Delivery delivery);
}
