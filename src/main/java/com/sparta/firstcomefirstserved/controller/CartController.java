package com.sparta.firstcomefirstserved.controller;

import com.sparta.firstcomefirstserved.dto.CartResponseDto;
import com.sparta.firstcomefirstserved.entity.Cart;
import com.sparta.firstcomefirstserved.security.UserDetailsImpl;
import com.sparta.firstcomefirstserved.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	// 장바구니 담기
	// 이미 담겨 있는 상품이면 수량 변경
	// 담겨 있지 않은 상품이면 장바구니에 담기
	@PostMapping("/items/{itemId}")
	public ResponseEntity<Void> addItems(
		@PathVariable Long itemId,
		@RequestParam int quantity,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		log.info("장바구니 담기 시작 - itemId: {}, quantity: {}, userId: {}", itemId, quantity,
			userDetails.getUser().getId());

		if (quantity <= 0) {
			log.warn("유효하지 않은 수량: {}", quantity);
			return ResponseEntity.badRequest().build();
		}

		try {
			cartService.addCart(itemId, userDetails.getUser(), quantity);
			log.info("장바구니 담기 끝 - itemId: {}", itemId);
			return ResponseEntity.ok().build(); // 200 OK 반환
		} catch (Exception e) {
			log.error("장바구니에 아이템 추가 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.build(); // 500 Internal Server Error 반환
		}

	}

	// 장바구니에서 상품 삭제
	@DeleteMapping("/items/{cartId}")
	public ResponseEntity<Void> deleteItems(
		@PathVariable Long cartId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		log.info("장바구니 삭제 시작 - cartId: {}, userId: {}", cartId, userDetails.getUser().getId());

		try {
			cartService.deleteCart(cartId);
			log.info("장바구니 삭제 끝 - cartId: {}", cartId);
			return ResponseEntity.ok().build(); // 200 OK 반환
		} catch (Exception e) {
			log.error("장바구니에 삭제 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.build(); // 500 Internal Server Error 반환
		}

	}

	// 장바구니 전체 물품 검색
	@GetMapping("/items")
	public Page<CartResponseDto> getCarts(
		@RequestParam("page") int page,
		@RequestParam("size") int size,
		@RequestParam("sortBy") String sortBy,
		@RequestParam("isAsc") boolean isAsc,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		return cartService.getItemsInCart(
			page - 1,
			size,
			sortBy,
			isAsc,
			userDetails.getUser());
	}

	// 장바구니 물품 상세 페이지
	@GetMapping("/items/{cartId}")
	public String getItem(
		@PathVariable Long cartId
	) {
		log.info("장바구니 상품 상세 페이지 - cartId: {}", cartId);

		Cart cart;

		try {
			cart = cartService.getCart(cartId);
		} catch (Exception e) {
			if (e.getMessage().equals("NULL")) {
				log.error("장바구니를 확인해주세요");
			}
			return "redirect:/cart/items";
		}

		return "redirect:/items/" + cart.getCartItem().getId();
	}
}
