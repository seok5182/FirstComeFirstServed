package com.sparta.firstcomefirstserved.service;

import com.sparta.firstcomefirstserved.dto.CartResponseDto;
import com.sparta.firstcomefirstserved.entity.Cart;
import com.sparta.firstcomefirstserved.entity.Item;
import com.sparta.firstcomefirstserved.entity.User;
import com.sparta.firstcomefirstserved.repository.CartRepository;
import com.sparta.firstcomefirstserved.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

	private final ItemRepository itemRepository;
	private final CartRepository cartRepository;

	// 장바구니에 담기
	@Transactional
	public void addCart(Long itemId, User user, int quantity) {
		Item item = itemRepository.findById(itemId).orElseThrow(() ->
			new NullPointerException("해당 상품은 존재하지 않습니다")
		);

		if (cartRepository.findAllByUser(user).isPresent()) {
			List<Cart> cartList = cartRepository.findAllByUser(user).get();

			for (Cart cart : cartList) {
				if (cart.getItem().getId().equals(itemId)) {
					cart.update(quantity);
					return;
				}
			}
		}

		cartRepository.save(new Cart(user, item, quantity));
	}

	// 장바구니에서 상품 삭제
	public void deleteCart(Long cartId) {
		cartRepository.deleteById(cartId);
	}

	// 유저의 장바구니 전체 물품 보여주기
	public Page<CartResponseDto> getItemsInCart(int page, int size, String sortBy,
		boolean isAsc, User user) {

		Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(direction, sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Cart> cartList = cartRepository.findAllByUser(user, pageable);
		Page<CartResponseDto> responseDtoList = cartList.map(CartResponseDto::new);

		return responseDtoList;
	}
}
