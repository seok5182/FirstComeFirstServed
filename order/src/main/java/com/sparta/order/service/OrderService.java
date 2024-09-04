package com.sparta.order.service;

import com.sparta.order.dto.AddressResponse;
import com.sparta.order.dto.OrderResponse;
import com.sparta.order.dto.PaymentRequest;
import com.sparta.order.dto.ProductResponse;
import com.sparta.order.dto.QuantityRequest;
import com.sparta.order.entity.Cart;
import com.sparta.order.entity.Order;
import com.sparta.order.entity.OrderDetail;
import com.sparta.order.entity.OrderStatus;
import com.sparta.order.entity.PaymentStatus;
import com.sparta.order.feign.AddressClient;
import com.sparta.order.feign.ProductClient;
import com.sparta.order.kafka.PaymentProducer;
import com.sparta.order.redis.StockClient;
import com.sparta.order.repository.CartRepository;
import com.sparta.order.repository.OrderDetailRepository;
import com.sparta.order.repository.OrderRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

	private final AddressClient addressClient;
	private final ProductClient productClient;
	private final StockClient stockClient;
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final PaymentProducer paymentProducer;

	// 주문, 주문 상세에 각각 저장
	@Transactional
	public ResponseEntity<Void> makeOrder(Long userId) {
		//log.info("주문 생성 시작");
		List<Cart> cartList = cartRepository.findAllByUserId(userId);
		AddressResponse address = addressClient.getAddress(userId);

		// Redis에 모든 상품 ID와 수량을 전달하여 재고를 확인
		Map<String, Integer> productQuantities = cartList.stream()
			.collect(Collectors.toMap(
				cart -> String.valueOf(cart.getProductId()),
				Cart::getQuantity
			));

		// Redis에서 재고 확인
		try {
			boolean orderAvailable = stockClient.verifyStock(productQuantities);
			//log.info("재고 확인 결과: {}", orderAvailable);

			if (!orderAvailable) {
				//log.warn("재고 부족으로 주문을 처리할 수 없습니다.");
				return ResponseEntity.badRequest().build();
			}
		} catch (Exception e) {
			//log.error("재고 확인 중 오류 발생: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		List<OrderDetail> orderDetailList = new ArrayList<>();
		int totalPrice = 0;
		int totalQuantity = 0;

		Order order = Order.of(userId, address);

		for (Cart cart : cartList) {
			//log.info("장바구니 물품 번호 : {}번 담는중", cart.getId());
			ProductResponse productResponse = productClient.getProduct(cart.getProductId());

			orderDetailList.add(
				OrderDetail.of(order.getId(), cart.getProductId(), productResponse.price(),
					cart.getQuantity())
			);

			totalQuantity += cart.getQuantity();
			totalPrice += cart.getQuantity() * productResponse.price();
		}

		order.updateOrder(totalQuantity, totalPrice);

		//log.info("장바구니 비우는 중");
		cartRepository.deleteAllByUserId(userId);

		Order savedOrder = orderRepository.save(order);
		orderDetailList.forEach(x -> x.updateOrderId(savedOrder.getId()));
		orderDetailRepository.saveAll(orderDetailList);

		//log.info("결제 서버로 주문 번호:{}번 메시지 보내기", savedOrder.getId());
		paymentProducer.sendPaymentRequest(PaymentRequest.of(savedOrder));

		return ResponseEntity.ok().build();
	}

	// 결제 완료 후 호출되는 메서드
	@Transactional
	public void handlePaymentSuccess(Long userId, Long orderId) {
		//log.info("결제 성공 처리 시작");

		// 주문 정보를 불러와 재고 감소 처리
		List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(orderId);
		List<QuantityRequest> quantityRequests = new ArrayList<>();
		for (OrderDetail orderDetail : orderDetails) {
			quantityRequests.add(QuantityRequest.from(orderDetail));
		}

		Map<String, Integer> productQuantities = orderDetails.stream()
			.collect(Collectors.toMap(
				detail -> String.valueOf(detail.getProductId()),
				OrderDetail::getQuantity
			));

		// Redis 재고 감소 처리(비동기 처리로 수정)
//		stockClient.decreaseStockAfterPayment(userId, productQuantities);
		CompletableFuture<Void> stockFuture = stockClient.decreaseStockAfterPayment(userId, productQuantities);
		// 상품 DB 재고 감소 처리
		productClient.decreaseQuantity(quantityRequests);

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new NullPointerException("주문.결제상태 변경 오류"));

		// 결제 상태 변경
		order.updatePaymentStatus(PaymentStatus.SUCCESS);
		// 주문 상태 변경
		order.updateOrderStatus(OrderStatus.ORDER);

		// 비동기 작업이 완료될 때까지 기다립니다
		CompletableFuture.allOf(stockFuture).join();
	}

	// 주문 조회
	public List<OrderResponse> getOrders(Long userId) {
		List<Order> orderList = orderRepository.findAllByUserIdOrderByIdAsc(userId);
		List<OrderResponse> orderResponseList = new ArrayList<>();
		for (Order order : orderList) {
			orderResponseList.add(OrderResponse.from(order));
		}
		return orderResponseList;
	}

	// 주문 취소
	@Transactional
	public void cancel(Long orderId) {
		//log.info("주문 번호:{}번 취소 진행 중", orderId);
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new NullPointerException("해당 주문이 없습니다."));
		List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderId(orderId);
		List<QuantityRequest> quantityRequests = new ArrayList<>();
		for (OrderDetail orderDetail : orderDetailList) {
			quantityRequests.add(QuantityRequest.from(orderDetail));
		}

		Map<String, Integer> productQuantities = orderDetailList.stream()
			.collect(Collectors.toMap(
				detail -> String.valueOf(detail.getProductId()),
				OrderDetail::getQuantity
			));
		// Redis 재고 증가
		//log.info("Redis 재고 증가");
		stockClient.restoreStock(productQuantities);
		// 상품 서버 재고 증가
		//log.info("상품 서버 재고 증가");
		productClient.increaseQuantity(quantityRequests);

		//log.info("주문 상태 CANCELLATION으로 업데이트");
		order.updateOrderStatus(OrderStatus.CANCELLATION);
	}

	// 반품 신청
	@Transactional
	public void returnOrder(Long orderId) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new NullPointerException("해당 주문이 없습니다."));

		LocalDate today = LocalDate.now();
		LocalDate lastUpdate = LocalDate.from(order.getUpdatedAt());

		if (order.getOrderStatus().equals(OrderStatus.DELIVERY_COMPLETED)) {
			if (today.compareTo(lastUpdate) < 2) {
				order.updateOrderStatus(OrderStatus.RETURN_REQUEST);
			} else {
				throw new IllegalArgumentException("TIME OVER");
			}
		} else {
			throw new IllegalArgumentException("ILLEGAL STATUS");
		}
	}

	@Transactional
	public void delete(Long orderId) {
		//log.info("{}번 주문 삭제", orderId);
		orderRepository.deleteById(orderId);
		//log.info("주문 {}번의 주문 상세 삭제", orderId);
		orderDetailRepository.deleteAllByOrderId(orderId);
	}

//	코드 개선 중 필요없는 코드
//	@Transactional
//	public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
//		//log.info("order id:{} - order status:{}", orderId, orderStatus);
//		Order order = orderRepository.findById(orderId)
//			.orElseThrow(() -> new NullPointerException("해당 주문이 없습니다."));
//
//		order.updateOrderStatus(orderStatus);
//	}
//
//	@Transactional
//	public void updatePaymentStatus(Long orderId, PaymentStatus paymentStatus) {
//		//log.info("order id:{} - payment status:{}", orderId, paymentStatus);
//		Order order = orderRepository.findById(orderId)
//			.orElseThrow(() -> new NullPointerException("해당 주문이 없습니다."));
//
//		order.updatePaymentStatus(paymentStatus);
//	}
}
