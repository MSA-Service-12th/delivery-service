package com.loopang.deliveryservice.domain.courier;

import com.loopang.deliveryservice.domain.delivery.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Courier {

    @Id
    @GeneratedValue
    private UUID courierId;

    private UUID userId;

    private int deliveryTurn;

    @Enumerated(EnumType.STRING)
    private DeliveryChargeType chargeType;

    public void assignCourier(UUID courierId) {
        if (this.courierId != null) throw new IllegalStateException("이미 담당자 있음");
        this.courierId = courierId;
    }

//    public void assignSequence(int sequence) {
//        this.sequence = sequence;
//    }

    public enum DeliveryChargeType {
        COMPANY, HUB
    }

    //@Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //@Column(name = "created_by", length = 100, nullable = false)
    private String createdBy;

    //@Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //@Column(name = "updated_by", length = 100)
    private String updatedBy;

    //@Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    //@Column(name = "deleted_by", length = 100)
    private String deletedBy;


    //생성

    //수정

    //  삭제 (Soft Delete)
    public void delete(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
