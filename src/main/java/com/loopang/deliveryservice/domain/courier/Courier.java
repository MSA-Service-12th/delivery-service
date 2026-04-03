package com.loopang.deliveryservice.domain.courier;

import com.loopang.deliveryservice.domain.common.BaseUserEntity;
import com.loopang.deliveryservice.domain.delivery.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Courier extends BaseUserEntity {

    @Id
    @GeneratedValue
    private UUID courierId;

    private UUID userId;

    private int deliveryTurn;

    @Enumerated(EnumType.STRING)
    private CourierType chargeType;

    public void assignCourier(UUID courierId) {
        if (this.courierId != null) throw new IllegalStateException("이미 담당자 있음");
        this.courierId = courierId;
    }

//    public void assignSequence(int sequence) {
//        this.sequence = sequence;
//    }

    public enum CourierType {
        COMPANY, HUB
    }


    //생성

    //수정

    //  삭제 (Soft Delete)
//    public void delete(String deletedBy) {
//        this.deletedAt = LocalDateTime.now();
//        this.deletedBy = deletedBy;
//    }
}
