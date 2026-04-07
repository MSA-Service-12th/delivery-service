package com.loopang.deliveryservice.presentation;

import com.loopang.common.response.CommonResponse;
import com.loopang.deliveryservice.application.DeliveryCommandService;
import com.loopang.deliveryservice.application.DeliveryQueryService;
import com.loopang.deliveryservice.application.DeliveryRouteService;
import com.loopang.deliveryservice.infrastructure.persistence.DeliveryQueryCondition;
import com.loopang.deliveryservice.presentation.dto.DeliveryResponseDto;
import com.loopang.deliveryservice.presentation.dto.DeliveryStatusRequestDto;
import com.loopang.deliveryservice.presentation.dto.RouteStatusRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryController {

	private final DeliveryCommandService deliveryCommandService;
	private final DeliveryQueryService deliveryQueryService;
	private final DeliveryRouteService deliveryRouteService;

	// 1. 배송 상세 조회 (상세 경로 포함)
	@GetMapping("/{deliveryId}")
	public CommonResponse<DeliveryResponseDto> getDelivery(
			@PathVariable("deliveryId") UUID deliveryId,
			@RequestHeader(value = "X-User-Id") String userId,
			@RequestHeader(value = "X-User-Role") String userRole) {
		DeliveryResponseDto delivery = deliveryQueryService.getDelivery(deliveryId, userId, userRole);
		return CommonResponse.of(delivery);
	}

	// 2. 배송 목록 조회
	@GetMapping
	public CommonResponse<Page<DeliveryResponseDto>> getDeliveryList(
			@ModelAttribute DeliveryQueryCondition condition,
			Pageable pageable,
			@RequestHeader(value = "X-User-UUID") String userId,
			@RequestHeader(value = "X-User-Role") String userRole) {
		Page<DeliveryResponseDto> deliveries = deliveryQueryService.getDeliveries(condition, pageable, userId, userRole);
		return CommonResponse.of(deliveries);
	}

	// 3. 배송 구간 상태 변경 (배송원용)
	@PatchMapping("/routes/{routeId}/status")
	public CommonResponse<Void> updateRouteStatus(
			@PathVariable("routeId") UUID routeId,
			@Valid @RequestBody RouteStatusRequestDto request,
			@RequestHeader(value = "X-User-UUID") String userId,
			@RequestHeader(value = "X-User-Role") String userRole) {
		deliveryRouteService.updateRouteStatus(routeId, request.getStatus(), userId, userRole);
		return CommonResponse.of(null);
	}

	// 4. 배송 전체 상태 변경 (관리자/시스템용)
	@PatchMapping("/{deliveryId}/status")
	public CommonResponse<Void> updateDeliveryStatus(
			@PathVariable("deliveryId") UUID deliveryId,
			@Valid @RequestBody DeliveryStatusRequestDto request,
			@RequestHeader(value = "X-User-UUId") String userId,
			@RequestHeader(value = "X-User-Role") String userRole) {
		deliveryCommandService.updateDeliveryStatus(deliveryId, request.getStatus(), userId, userRole);
		return CommonResponse.of(null);
	}

	// 5. 배송 삭제 (Soft Delete)
	@DeleteMapping("/{deliveryId}")
	public CommonResponse<Void> deleteDelivery(
			@PathVariable("deliveryId") UUID deliveryId,
			@RequestHeader(value = "X-User-UUID") String userId,
			@RequestHeader(value = "X-User-Role") String userRole) {
		deliveryCommandService.deleteDelivery(deliveryId, userId, userRole);
		return CommonResponse.of(null);
	}
}
