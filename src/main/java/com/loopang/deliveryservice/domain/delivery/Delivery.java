package com.loopang.deliveryservice.domain.delivery;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue
    private UUID deliveryId;

    private UUID orderId;

    private UUID courierId;

//    private Integer sequence;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private boolean deleted = false;

    public Delivery(UUID orderId) {
        this.orderId = orderId;
        this.status = DeliveryStatus.WAITING_AT_HUB;
    }

    public void assignCourier(UUID courierId) {
        if (this.courierId != null) throw new IllegalStateException("이미 담당자 있음");
        this.courierId = courierId;
    }

//    public void assignSequence(int sequence) {
//        this.sequence = sequence;
//    }

    public void changeStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void delete() {
        this.deleted = true;
    }

//    //  삭제 (Soft Delete)
//    public void delete(String deletedBy) {
//        this.deletedAt = LocalDateTime.now();
//        this.deletedBy = deletedBy;
//    }
}
