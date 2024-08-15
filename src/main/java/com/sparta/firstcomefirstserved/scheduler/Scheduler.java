package com.sparta.firstcomefirstserved.scheduler;

import com.sparta.firstcomefirstserved.entity.Item;
import com.sparta.firstcomefirstserved.entity.Order;
import com.sparta.firstcomefirstserved.entity.OrderDetail;
import com.sparta.firstcomefirstserved.entity.OrderStatus;
import com.sparta.firstcomefirstserved.repository.OrderDetailRepository;
import com.sparta.firstcomefirstserved.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class Scheduler {

	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;

	@Scheduled(cron = "0 0 0 * * *") // 매일 00시
	@Transactional
	public void updateOrderStatus() {

		// 배송 중 -> 배송 완료로 변경
		List<Order> deliveryList = orderRepository.findByStatus(OrderStatus.IN_DELIVERY);
		for (Order order : deliveryList) {
			order.setStatus(OrderStatus.DELIVERY_COMPLETED);
		}

		// 주문 중 -> 배송 중 변경
		List<Order> OrderList = orderRepository.findByStatus(OrderStatus.ORDER);
		for (Order order : OrderList) {
			order.setStatus(OrderStatus.IN_DELIVERY);
		}

		// 반품 신청 -> 재고 반영 -> 반품 완료 변경
		List<Order> returnList = orderRepository.findByStatus(OrderStatus.RETURN_REQUEST);
		for (Order order : returnList) {

			List<OrderDetail> orderDetailList = orderDetailRepository.findByOrder(order);
			for (OrderDetail orderDetail : orderDetailList) {
				Item item = orderDetail.getOrderItem();
				item.setQuantity(item.getQuantity() + orderDetail.getQuantity());
			}

			order.setStatus(OrderStatus.RETURN_COMPLETED);
		}

	}
}
