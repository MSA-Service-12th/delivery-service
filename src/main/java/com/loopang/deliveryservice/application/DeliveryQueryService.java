package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import com.loopang.deliveryservice.domain.repository.DeliveryQueryRepository;
import com.loopang.deliveryservice.domain.repository.DeliveryRepository;
import com.loopang.deliveryservice.presentation.dto.DeliveryRequestDto;
import com.loopang.deliveryservice.presentation.dto.DeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryQueryService {

    private final DeliveryQueryRepository deliveryRepository;

    // 단건 조회
    public DeliveryResponseDto getDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        return DeliveryResponseDto.from(delivery);
    }

    // 목록 조회
    public Page<DeliveryResponseDto> getDeliveries(Pageable pageable) {
        return deliveryRepository.findAll(pageable)
                .map(DeliveryResponseDto::from);
    }
}
