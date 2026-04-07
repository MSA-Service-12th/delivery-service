package com.loopang.deliveryservice.application;

import com.loopang.deliveryservice.domain.entity.Delivery;
import com.loopang.deliveryservice.domain.entity.DeliveryRoute;
import com.loopang.deliveryservice.domain.repository.DeliveryRepository;
import com.loopang.deliveryservice.domain.vo.delivery.DeliveryStatus;
import com.loopang.deliveryservice.domain.vo.deliveryroute.RouteEdge;
import com.loopang.deliveryservice.domain.event.payload.OrderAcceptedPayload;
import com.loopang.deliveryservice.domain.service.dto.RouteEdgeData;
import com.loopang.deliveryservice.domain.service.dto.RouteResultData;
import com.loopang.deliveryservice.domain.vo.CourierType;
import com.loopang.deliveryservice.domain.vo.deliveryroute.CourierInfo;
import com.loopang.deliveryservice.domain.vo.deliveryroute.DeliveryRelation;
import com.loopang.deliveryservice.domain.exception.DeliveryErrorCode;
import com.loopang.deliveryservice.domain.exception.DeliveryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryCommandCore {

    private final DeliveryRepository deliveryRepository;

    // 배송 및 동적 계산된 경로 통합 생성 (트랜잭션 보장)
    public Delivery createWithCalculatedRoutes(OrderAcceptedPayload payload, 
                                              RouteResultData routeResult,
                                              CourierInfo firstCourier, 
                                              List<CourierInfo> hubCouriers, 
                                              CourierInfo lastCourier) {
        // 1. Delivery 엔티티 구성 및 초기 담당자 설정
        Delivery delivery = Delivery.from(payload);
        delivery.updateCurrentCourier(firstCourier.getCourierId(), CourierType.COMPANY);

        int seq = 1;

        // 2. First Segment: 업체 -> 출발허브 (COMPANY)
        RouteEdge edge1 = RouteEdge.from(seq++, payload.supplierId(), payload.supplierHubId(), 0, 0);
        DeliveryRoute firstRoute = DeliveryRoute.create(edge1);
        firstRoute.updateRelation(DeliveryRelation.TO_HUB);
        firstRoute.assignCourier(firstCourier);
        delivery.addDeliveryRoute(firstRoute);

        // 3. Hub Transit Segments: 허브 -> 허브 (HUB, 가변 경로)
        List<RouteEdgeData> edges = routeResult.routeEdges();
        for (int i = 0; i < edges.size(); i++) {
            RouteEdgeData edgeData = edges.get(i);
            RouteEdge hubEdge = RouteEdge.from(seq++, edgeData.fromHubId(), edgeData.toHubId(), edgeData.distance(), edgeData.duration().intValue());
            DeliveryRoute hubRoute = DeliveryRoute.create(hubEdge);
            hubRoute.updateRelation(DeliveryRelation.HUB_TO_HUB);
            hubRoute.assignCourier(hubCouriers.get(i));
            delivery.addDeliveryRoute(hubRoute);
        }

        // 4. Last Segment: 도착허브 -> 수령업체 (COMPANY)
        RouteEdge edge3 = RouteEdge.from(seq++, payload.receiverHubId(), payload.receiverId(), 0, 0);
        DeliveryRoute lastRoute = DeliveryRoute.create(edge3);
        lastRoute.updateRelation(DeliveryRelation.FROM_HUB);
        lastRoute.assignCourier(lastCourier);
        delivery.addDeliveryRoute(lastRoute);

        // 5. DB 저장 (Cascade에 의해 Route도 함께 저장)
        return deliveryRepository.save(delivery);
    }

    // 배송 전체 상태 강제 변경 (트랜잭션)
    public void updateStatus(UUID deliveryId, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        delivery.changeStatus(status);
    }

    // 삭제
    public void deleteDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        delivery.delete(deliveryId);
    }
}
