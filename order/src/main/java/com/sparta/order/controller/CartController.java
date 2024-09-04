package com.sparta.order.controller;

import com.sparta.order.dto.CartItemRequest;
import com.sparta.order.dto.CartResponse;
import com.sparta.order.entity.Cart;
import com.sparta.order.service.CartService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

	private final CartService cartService;

	// 장바구니 담기
	@PostMapping
	public ResponseEntity<Void> addCartItem(
		@Valid @RequestBody CartItemRequest cartItem,
		@RequestHeader(value = "X-USER-ID") Long userId
	) {
		try {
			cartService.addCartItem(userId, cartItem);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			if (e.getMessage().equals("EXIST")) {
				//log.error("이미 장바구니에 담겨있습니다");
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.internalServerError().build();
	}

	// 장바구니 수정
	@PostMapping("/{cartId}")
	public ResponseEntity<Void> changeCartItem(
		@PathVariable Long cartId,
		@RequestParam int quantity
	) {
		cartService.changeCartItem(cartId, quantity);
		return ResponseEntity.ok().build();
	}

	// 장바구니 삭제
	@DeleteMapping("{cartId}")
	public ResponseEntity<Void> deleteCartItem(
		@PathVariable Long cartId
	) {
		cartService.deleteCartItem(cartId);
		return ResponseEntity.ok().build();
	}

	// 장바구니 전체 상품
	@GetMapping
	public List<CartResponse> getCartItems(
		@RequestHeader(value = "X-USER-ID") Long userId
	) {
		return cartService.getCartItems(userId);
	}

	// 장바구니 물품 상세 정보
	@GetMapping("/{cartId}")
	public String getItem(
		@PathVariable Long cartId
	) {
		try {
			Cart cart = cartService.getCart(cartId);
			return "redirect:/products/" + cart.getProductId();
		} catch (Exception e) {
			if (e.getMessage().equals("NULL")) {
				log.error("장바구니를 확인해주세요");
			}
			return "redirect:/carts";
		}
	}
}
