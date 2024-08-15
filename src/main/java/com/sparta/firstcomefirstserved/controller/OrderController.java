package com.sparta.firstcomefirstserved.controller;

import com.sparta.firstcomefirstserved.dto.OrderResponse;
import com.sparta.firstcomefirstserved.entity.Order;
import com.sparta.firstcomefirstserved.entity.OrderStatus;
import com.sparta.firstcomefirstserved.entity.User;
import com.sparta.firstcomefirstserved.security.UserDetailsImpl;
import com.sparta.firstcomefirstserved.service.AddressService;
import com.sparta.firstcomefirstserved.service.CartService;
import com.sparta.firstcomefirstserved.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

	private final CartService cartService;
	private final OrderService orderService;
	private final AddressService addressService;

	// 주문 진행
	@PostMapping("/items")
	public ResponseEntity<Void> makeOrder(@AuthenticationPrincipal UserDetailsImpl userDetails) {

		User user = userDetails.getUser();

		try {
			orderService.makeOrder(user);
			log.info("주문 완료");
			return ResponseEntity.ok().build(); // 200 OK 반환
		} catch (Exception e) {
			log.error("주문 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.build(); // 500 Internal Server Error 반환
		}
	}

	// 주문 조회
	@GetMapping
	public List<OrderResponse> getOrder(@AuthenticationPrincipal UserDetailsImpl userDetails) {

		User user = userDetails.getUser();

		return orderService.getOrders(user);
	}

	// 주문 취소
	@PostMapping("/{orderId}/cancel")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
		Order order = orderService.getOrder(orderId);

		if (!order.getStatus().equals(OrderStatus.ORDER)) {
			log.error("주문 중 상태에서만 취소가 가능합니다.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.build(); // 400 Bad Request 반환
		}

		try {
			log.info("주문 취소 완료");
			orderService.cancel(order);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("주문 취소가 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.build(); // 500 Internal Server Error 반환
		}

	}

	// 주문 반품
	@PostMapping("/{orderId}/return")
	public ResponseEntity<Void> returnOrder(@PathVariable Long orderId) {
		Order order = orderService.getOrder(orderId);

		log.info("반품 신청 시작");
		try {
			orderService.returnOrder(order);
			log.info("반품 신청 완료");
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			if (e.getMessage().equals("ILLEGAL STATUS")) {
				log.error("배송 완료인 상품만 반품 신청이 가능합니다.", e);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.build(); // 400 Bad Request 반환
			} else if (e.getMessage().equals("TIME OVER")) {
				log.error("반품 신청은 배송 완료 후 D+1까지만 가능합니다.", e);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.build(); // 400 Bad Request 반환
			} else {
				log.error("반품 신청 중 오류 발생", e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.build(); // 500 Internal Server Error 반환
			}
		}
	}
}
