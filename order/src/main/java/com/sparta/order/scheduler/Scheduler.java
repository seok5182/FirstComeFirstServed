package com.sparta.order.scheduler;

import com.sparta.order.dto.QuantityRequest;
import com.sparta.order.entity.Order;
import com.sparta.order.entity.OrderDetail;
import com.sparta.order.entity.OrderStatus;
import com.sparta.order.feign.ProductClient;
import com.sparta.order.redis.StockClient;
import com.sparta.order.repository.OrderDetailRepository;
import com.sparta.order.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class Scheduler {

	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final ProductClient productClient;
	private final StockClient stockClient;

	@Scheduled(cron = "0 0 0 * * *") // 매일 00시
	@Transactional
	public void updateOrderStatus() {

		// 배송 중 -> 배송 완료로 변경
		List<Order> deliveryList = orderRepository.findByOrderStatus(OrderStatus.IN_DELIVERY);
		for (Order order : deliveryList) {
			order.updateOrderStatus(OrderStatus.DELIVERY_COMPLETED);
		}

		// 주문 중 -> 배송 중 변경
		List<Order> OrderList = orderRepository.findByOrderStatus(OrderStatus.ORDER);
		for (Order order : OrderList) {
			order.updateOrderStatus(OrderStatus.IN_DELIVERY);
		}

		// 반품 신청 -> 재고 반영 -> 반품 완료 변경
		List<Order> returnList = orderRepository.findByOrderStatus(OrderStatus.RETURN_REQUEST);
		for (Order order : returnList) {

			List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(order.getId());
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
			stockClient.restoreStock(productQuantities);
			// 상품 서버 재고 증가
			productClient.increaseQuantity(quantityRequests);

			order.updateOrderStatus(OrderStatus.RETURN_COMPLETED);
		}

	}
}
