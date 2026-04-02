package com.loopang.deliveryservice.domain.route;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    private UUID id;

    private UUID deliveryId;

    private String fromLocation;
    private String toLocation;

    private int sequence;



    public void update(String from, String to) {
        this.fromLocation = from;
        this.toLocation = to;
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
