package com.loopang.deliveryservice.presentation;

import com.loopang.deliveryservice.application.DeliveryService;
import com.loopang.deliveryservice.domain.delivery.Delivery;
import com.loopang.deliveryservice.domain.delivery.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    //배송생성
    @PostMapping
    public UUID create(@RequestParam UUID orderId) {
        return deliveryService.createDelivery(orderId);
    }
    //배송 단건 조회
    @GetMapping("/{id}")
    public Delivery get(@PathVariable Long id) {
        return deliveryService.getDelivery(id);
    }
    //배송 목록 조회
    @GetMapping
    public List<Delivery> list() {
        return deliveryService.getDeliveries();
    }
    //배송 상태 변경
    @PatchMapping("/{id}/status")
    public void changeStatus(@PathVariable Long id,
                             @RequestParam DeliveryStatus status) {
        deliveryService.changeStatus(id, status);
    }

    //배송 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deliveryService.delete(id);
    }

    //배송 담당자 등록
    @PostMapping("/{id}/courier")
    public void assignCourier(@PathVariable Long id,
                            @RequestParam Long agentId) {
        deliveryService.assignCourier(id, agentId);
    }

    //배송 순번 배정
    @PostMapping("/{id}/sequence")
    public void assignSequence(@PathVariable Long id,
                               @RequestParam int seq) {
        deliveryService.assignSequence(id, seq);
    }
}
