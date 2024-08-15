package com.sparta.firstcomefirstserved.service;

import com.sparta.firstcomefirstserved.dto.OrderResponse;
import com.sparta.firstcomefirstserved.entity.Address;
import com.sparta.firstcomefirstserved.entity.Cart;
import com.sparta.firstcomefirstserved.entity.Item;
import com.sparta.firstcomefirstserved.entity.Order;
import com.sparta.firstcomefirstserved.entity.OrderDetail;
import com.sparta.firstcomefirstserved.entity.OrderStatus;
import com.sparta.firstcomefirstserved.entity.User;
import com.sparta.firstcomefirstserved.repository.AddressRepository;
import com.sparta.firstcomefirstserved.repository.CartRepository;
import com.sparta.firstcomefirstserved.repository.OrderDetailRepository;
import com.sparta.firstcomefirstserved.repository.OrderRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final AddressRepository addressRepository;
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;

	// 주문, 주문상세에 save
	@Transactional
	public void makeOrder(User user) {
		List<Cart> cartList = cartRepository.findAllByUser(user).orElseThrow(() ->
			new NullPointerException("NULL")
		);
		Address address = addressRepository.findAddressByUserAndDefaultAddress(user, true);
		List<OrderDetail> orderDetailList = new ArrayList<>();
		int totalPrice = 0;
		int totalCount = 0;

		for (Cart cart : cartList) {
			if (cart.getUser().equals(user)) {
				throw new IllegalArgumentException("장바구니 유저가 일치하지 않습니다");
			}
			totalCount += cart.getQuantity();
			totalPrice += cart.getQuantity() * cart.getCartItem().getPrice();
		}

		Order order = new Order(user, totalCount, totalPrice, address);
		for (Cart cart : cartList) {
			orderDetailList.add(
				new OrderDetail(order, cart.getCartItem(), cart.getCartItem().getPrice(),
					cart.getQuantity()));
		}

		for (OrderDetail orderDetail : orderDetailList) {
			Item item = orderDetail.getOrderItem();
			// 주문하고자 한 상품의 재고가 미달인 경우 주문 불가
			if (item.getQuantity() - orderDetail.getQuantity() < 0) {
				throw new IllegalArgumentException("재고가 없습니다");
			}

			// 상품 재고 변경
			item.setQuantity(item.getQuantity() - orderDetail.getQuantity());
		}

		// 장바구니에 담긴 상품 제거
		cartRepository.deleteAllByUser(user);

		// 주문, 주문 상세 저장
		orderRepository.save(order);
		orderDetailRepository.saveAll(orderDetailList);

	}

	// 주문 조회
	public List<OrderResponse> getOrders(User user) {
		List<Order> orderList = orderRepository.findAllByUser(user);
		List<OrderResponse> orderResponseList = new ArrayList<>();
		for (Order order : orderList) {
			orderResponseList.add(new OrderResponse(order));
		}
		return orderResponseList;
	}

	public Order getOrder(Long orderId) {
		return orderRepository.findById(orderId).orElseThrow(() ->
			new NullPointerException("해당하는 주문이 없습니다")
		);
	}

	// 주문 취소
	// 주문 취소된 상품들의 재고 원복
	// 주문 상태를 [취소완료]로 변경
	@Transactional
	public void cancel(Order order) {
		List<OrderDetail> orderDetailList = order.getOrderDetailList();
		for (OrderDetail orderDetail : orderDetailList) {
			Item item = orderDetail.getOrderItem();
			item.setQuantity(item.getQuantity() + orderDetail.getQuantity());
		}
		order.setStatus(OrderStatus.CANCELLATION);
	}

	// 반품 신청
	@Transactional
	public void returnOrder(Order order) {

		LocalDate today = LocalDate.now();
		LocalDate lastUpdate = LocalDate.from(order.getUpdatedAt());

		if (order.getStatus().equals(OrderStatus.DELIVERY_COMPLETED)) {
			if (today.compareTo(lastUpdate) < 2) {
				order.setStatus(OrderStatus.RETURN_REQUEST);
			} else {
				throw new IllegalArgumentException("TIME OVER");
			}
		} else {
			throw new IllegalArgumentException("ILLEGAL STATUS");
		}
	}
}
