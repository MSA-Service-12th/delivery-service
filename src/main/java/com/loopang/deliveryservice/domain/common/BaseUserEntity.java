package com.loopang.deliveryservice.domain.common;

import java.time.LocalDateTime;

public class BaseUserEntity {

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
}
