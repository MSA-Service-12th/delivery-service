package com.loopang.deliveryservice.presentation;

import com.loopang.deliveryservice.application.DeliveryRouteService;
import com.loopang.deliveryservice.domain.route.DeliveryRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
public class DeliveryRouteController {

    private final DeliveryRouteService routeService;
    //배송 경로 생성
    @PostMapping
    public Long create(@RequestParam Long deliveryId,
                       @RequestParam String from,
                       @RequestParam String to,
                       @RequestParam int seq) {
        return routeService.createRoute(deliveryId, from, to, seq);
    }
    //배송 경로 계산
    @PostMapping("/calculate/{deliveryId}")
    public void calculate(@PathVariable Long deliveryId) {
        routeService.calculateRoute(deliveryId);
    }
    //배송 경로 수정
    @PatchMapping("/{id}")
    public void update(@PathVariable Long id,
                       @RequestParam String from,
                       @RequestParam String to) {
        routeService.updateRoute(id, from, to);
    }
    //배송 경로 단건 조회
    @GetMapping("/{id}")
    public DeliveryRoute get(@PathVariable Long id) {
        return routeService.getRoute(id);
    }
    //배송 경로 목록 조회
    @GetMapping
    public List<DeliveryRoute> list(@RequestParam Long deliveryId) {
        return routeService.getRoutes(deliveryId);
    }
    //배송 경로 삭제(soft delete)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        routeService.deleteRoute(id);
    }
}
