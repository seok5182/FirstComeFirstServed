package com.sparta.order.kafka;

import com.sparta.order.entity.Order;
import com.sparta.order.entity.OrderDetail;
import com.sparta.order.entity.OrderStatus;
import com.sparta.order.entity.PaymentStatus;
import com.sparta.order.redis.StockClient;
import com.sparta.order.repository.OrderDetailRepository;
import com.sparta.order.repository.OrderRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentConsumer {

	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final StockClient stockClient;

	@Transactional
	@KafkaListener(topics = "payment-fail", groupId = "order-group")
	public void listen(Long orderId) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new NullPointerException("해당 주문이 없습니다."));

		//log.info("order id:{} - 결제 실패로 인한 주문 취소 시작", order.getId());

		// 결제 상태를 '결제 실패'로 주문 상태를 '취소 완료'로 변경
		//log.info("결제 상태 변경");
		order.updatePaymentStatus(PaymentStatus.FAIL);
		//log.info("주문 상태 변경");
		order.updateOrderStatus(OrderStatus.CANCELLATION);

		List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
		Map<String, Integer> productQuantities = orderDetails.stream()
			.collect(Collectors.toMap(
				detail -> String.valueOf(detail.getProductId()),
				OrderDetail::getQuantity
			));

		// 예약된 재고 해제
		stockClient.releaseReservedStock(productQuantities);

		//log.info("결제 실패 처리 완료");
	}
}
