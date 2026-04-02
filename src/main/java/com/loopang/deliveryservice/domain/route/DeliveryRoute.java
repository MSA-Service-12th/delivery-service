package com.loopang.deliveryservice.domain.route;

import com.loopang.deliveryservice.domain.courier.Courier;
import com.loopang.deliveryservice.domain.delivery.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class DeliveryRoute {

    @Id
    @GeneratedValue
    private UUID deliveryRouteId;

    private UUID deliveryId;

    private String fromLocationId;

    private String toLocationId;

    private String sequence;


    @Enumerated(EnumType.STRING)
    private DeliveryRelation deliveryRelation;


    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private String expectedDistance;

    private String expectedTime;

    private String realDistance;

    private String realTime;

    private UUID courierId;

    private String courierName;

    @Enumerated(EnumType.STRING)
    private CourierType chargeType;

    public enum CourierType {
        COMPANY, HUB
    }












    public enum DeliveryRelation {
        Inter_Hub_Transfer, Hub_To_Company_Transfer
    }

    public void update(String from, String to) {
        this.fromLocationId = from;
        this.toLocationId = to;
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
