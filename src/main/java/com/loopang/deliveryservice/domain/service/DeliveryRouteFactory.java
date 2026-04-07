package com.loopang.deliveryservice.domain.service;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.event.payload.OrderAcceptedPayload;
import com.loopang.deliveryservice.domain.service.dto.RouteEdgeData;
import com.loopang.deliveryservice.domain.service.dto.RouteResultData;
import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.deliveryroute.CourierInfo;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRelation;
import org.springframework.stereotype.Service;

import java.util.List;

// 외부 데이터(RouteResultData)를 도메인 엔티티(Delivery, DeliveryRoute)로 변환하는 매핑 로직을 담당
@Service
public class DeliveryRouteFactory {

    public Delivery createWithRoutes(OrderAcceptedPayload payload, RouteResultData routeResult,
                                     CourierInfo firstCourier, List<CourierInfo> hubCouriers, CourierInfo lastCourier) {
        
        // 1. 배송 기본 정보 생성
        Delivery delivery = Delivery.from(payload);
        
        // 2. 현재 배송 담당자 설정 (최초 구간 담당자)
        delivery.updateCurrentCourier(firstCourier.getCourierId(), CourierType.COMPANY);

        // 3. 업체 -> 출발허브 구간 생성 (자동 순번 1)
        delivery.addRouteSegment(payload.supplierId(), payload.supplierHubId(), 0.0, 0, 
                                 DeliveryRelation.TO_HUB, firstCourier);

        // 4. 허브 -> 허브 구간 생성 (자동 순번 2 ~ N)
        List<RouteEdgeData> edges = routeResult.routeEdges();
        for (int i = 0; i < edges.size(); i++) {
            RouteEdgeData edgeData = edges.get(i);
            delivery.addRouteSegment(edgeData.fromHubId(), edgeData.toHubId(), 
                                     edgeData.distance(), edgeData.duration().intValue(), 
                                     DeliveryRelation.HUB_TO_HUB, hubCouriers.get(i));
        }

        // 5. 도착허브 -> 수령업체 구간 생성 (자동 순번 N+1)
        delivery.addRouteSegment(payload.receiverHubId(), payload.receiverId(), 0.0, 0, 
                                 DeliveryRelation.FROM_HUB, lastCourier);

        return delivery;
    }
}
