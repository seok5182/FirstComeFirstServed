package com.sparta.order.controller;

import com.sparta.order.dto.OrderResponse;
import com.sparta.order.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Void> makeOrder(@RequestHeader(value = "X-USER-ID") Long userId) {
		try {
			orderService.makeOrder(userId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
				.build();
		}
	}

	// 결제 성공 처리
	@PostMapping("/payment/success")
	public ResponseEntity<Void> handlePaymentSuccess(@RequestHeader(value = "X-USER-ID") Long userId, @RequestParam("orderId") Long orderId) {
		try {
			orderService.handlePaymentSuccess(userId, orderId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
				.build();
		}
	}

	@GetMapping
	public ResponseEntity<List<OrderResponse>> getOrders(
		@RequestHeader(value = "X-USER-ID") Long userId) {
		try {
			return ResponseEntity.ok(orderService.getOrders(userId));
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
				.build();
		}
	}

	// 주문 취소
	@PostMapping("/{orderId}/cancel")
	public ResponseEntity<Void> cancelOrder(@PathVariable("orderId") Long orderId) {
		try {
			orderService.cancel(orderId);
			return ResponseEntity.ok()
				.build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
				.build();
		}
	}

	// 주문 반품
	@PostMapping("/{orderId}/return")
	public ResponseEntity<Void> returnOrder(@PathVariable("orderId") Long orderId) {
		try {
			orderService.returnOrder(orderId);
			return ResponseEntity.ok()
				.build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
				.build();
		}
	}
}
