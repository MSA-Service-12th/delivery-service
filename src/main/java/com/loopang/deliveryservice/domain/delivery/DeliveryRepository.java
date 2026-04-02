package com.loopang.deliveryservice.domain.delivery;

import java.util.List;

public interface DeliveryRepository {
    Delivery findById(Long id);
    List<Delivery> findAll();
    void save(Delivery delivery);
}
