package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.delivery.Delivery;
import com.loopang.deliveryservice.domain.delivery.DeliveryRepository;
import com.loopang.deliveryservice.domain.delivery.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    // 배송 생성
    public UUID createDelivery(UUID orderId) {
        Delivery delivery = new Delivery(orderId);
        deliveryRepository.save(delivery);
        return delivery.getDeliveryId();
    }

    // 단건 조회
    public Delivery getDelivery(Long id) {
        return deliveryRepository.findById(id);
    }

    // 목록 조회
    public List<Delivery> getDeliveries() {
        return deliveryRepository.findAll();
    }

    // 상태 변경
    public void changeStatus(Long id, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(id);
        delivery.changeStatus(status);
    }

    // 삭제
    public void delete(Long id) {
        Delivery delivery = deliveryRepository.findById(id);
        delivery.delete();
    }

    // 담당자 등록
    public void assignCourier(Long id, Long courierId) {
        Delivery delivery = deliveryRepository.findById(id);
        delivery.assignCourier(courierId);
    }

    // 순번 배정
    public void assignSequence(Long id, int sequence) {
        Delivery delivery = deliveryRepository.findById(id);
        delivery.assignSequence(sequence);
    }
}
