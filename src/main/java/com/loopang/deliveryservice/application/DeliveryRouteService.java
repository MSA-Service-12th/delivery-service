package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.route.DeliveryRoute;
import com.loopang.deliveryservice.domain.route.DeliveryRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryRouteService {

    private final DeliveryRouteRepository routeRepository;

    // 경로 생성
    public Long createRoute(Long deliveryId, String from, String to, int seq) {
        DeliveryRoute route = new DeliveryRoute(deliveryId, from, to, seq);
        routeRepository.save(route);
        return route.getId();
    }

    // 경로 계산 (간단 예시)
    public void calculateRoute(Long deliveryId) {
        // 외부 API or 알고리즘 호출 위치
    }

    // 경로 수정
    public void updateRoute(Long id, String from, String to) {
        DeliveryRoute route = routeRepository.findById(id);
        route.update(from, to);
    }

    // 단건 조회
    public DeliveryRoute getRoute(Long id) {
        return routeRepository.findById(id);
    }

    // 목록 조회
    public List<DeliveryRoute> getRoutes(Long deliveryId) {
        return routeRepository.findByDeliveryId(deliveryId);
    }

    // 삭제
    public void deleteRoute(Long id) {
        DeliveryRoute route = routeRepository.findById(id);
        route.delete();
    }
}
