package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.event.payload.OrderAcceptedPayload;
import com.loopang.deliveryservice.domain.service.dto.RouteEdgeData;
import com.loopang.deliveryservice.domain.service.dto.RouteResultData;
import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.deliveryroute.CourierInfo;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRelation;
import org.springframework.stereotype.Component;

import java.util.List;

// 여러 도메인 객체와 외부 데이터를 조합하여 복잡한 Delivery 애그리거트를 생성
// 응용 계층에 위치하여 향후 외부 API 호출 로직 등을 유연하게 수용 가능
@Component
public class DeliveryRouteFactory {

    public Delivery createWithRoutes(OrderAcceptedPayload payload, RouteResultData routeResult,
                                     CourierInfo firstCourier, List<CourierInfo> hubCouriers, CourierInfo lastCourier) {
        
        // 1. 배송 기본 정보 생성 및 초기 담당자 설정
        Delivery delivery = Delivery.from(payload);
        delivery.updateCurrentCourier(firstCourier.getCourierId(), CourierType.COMPANY);

        // 2. 구간별 경로 생성 위임
        addFirstSegment(delivery, payload, firstCourier);
        addTransitSegments(delivery, routeResult.routeEdges(), hubCouriers);
        addLastSegment(delivery, payload, lastCourier);

        return delivery;
    }

    // [First Segment] 업체 -> 출발허브 (TO_HUB)
    private void addFirstSegment(Delivery delivery, OrderAcceptedPayload payload, CourierInfo courier) {
        delivery.addRouteSegment(
                payload.supplierId(), 
                payload.supplierHubId(), 
                0.0, 0, 
                DeliveryRelation.TO_HUB, 
                courier
        );
    }

    // [Transit Segments] 허브 -> 허브 (HUB_TO_HUB)
    private void addTransitSegments(Delivery delivery, List<RouteEdgeData> edges, List<CourierInfo> couriers) {
        for (int i = 0; i < edges.size(); i++) {
            RouteEdgeData edge = edges.get(i);
            delivery.addRouteSegment(
                    edge.fromHubId(), 
                    edge.toHubId(), 
                    edge.distance(), 
                    edge.duration().intValue(), 
                    DeliveryRelation.HUB_TO_HUB, 
                    couriers.get(i)
            );
        }
    }

    // [Last Segment] 도착허브 -> 목적지 (FROM_HUB)
    private void addLastSegment(Delivery delivery, OrderAcceptedPayload payload, CourierInfo courier) {
        delivery.addRouteSegment(
                payload.receiverHubId(), 
                payload.receiverId(), 
                0.0, 0, 
                DeliveryRelation.FROM_HUB, 
                courier
        );
    }
}
