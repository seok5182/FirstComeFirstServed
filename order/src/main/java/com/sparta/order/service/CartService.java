package com.sparta.order.service;

import com.sparta.order.dto.CartItemRequest;
import com.sparta.order.dto.CartResponse;
import com.sparta.order.dto.ProductResponse;
import com.sparta.order.entity.Cart;
import com.sparta.order.feign.ProductClient;
import com.sparta.order.repository.CartRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final ProductClient productClient;

	public void addCartItem(Long userId, CartItemRequest cartItemRequest) {
		Long productId = cartItemRequest.productId();

		// 사용자와 제품 ID로 카트 항목을 한번에 조회
		Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(userId, productId);

		if (existingCart.isPresent()) {
			throw new IllegalArgumentException("EXIST");
		}

		Cart cart = Cart.of(userId, cartItemRequest);
		cartRepository.save(cart);
	}

	public void deleteCartItem(Long cartId) {
		cartRepository.deleteById(cartId);
	}

	@Transactional
	public void changeCartItem(Long cartId, int quantity) {
		Cart cart = cartRepository.findById(cartId)
			.orElseThrow(() -> new NullPointerException("장바구니에 해당 상품이 없습니다."));

		cart.update(quantity);
	}

	public List<CartResponse> getCartItems(Long userId) {

		List<Cart> cartList = cartRepository.findAllByUserId(userId);
		List<CartResponse> cartResponseList = new ArrayList<>();
		for (Cart cart : cartList) {
			ProductResponse productResponse = getProductInfo(cart.getProductId());
			cartResponseList.add(CartResponse.from(productResponse, cart));
		}

		return cartResponseList;
	}

	public Cart getCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId).orElseThrow(() ->
			new NullPointerException("NULL"));

		return cart;
	}

	public ProductResponse getProductInfo(Long productId) {
		return productClient.getProduct(productId);
	}

}
