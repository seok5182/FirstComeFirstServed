package com.sparta.payment.kafka;

import com.sparta.payment.dto.PaymentRequest;
import com.sparta.payment.feign.OrderClient;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentListener {

	private final KafkaTemplate<String, Long> kafkaTemplate;
	private final OrderClient orderClient;

	@KafkaListener(topics = "payment-request", groupId = "payment-group")
	public void listen(PaymentRequest paymentRequest) {
		//log.info("Received payment request: " + paymentRequest);
		Long userId = paymentRequest.userId();
		Long orderId = paymentRequest.orderId();

		try {
			// 결제 처리 로직
			boolean paymentSuccess = processPayment(paymentRequest);

			if(!paymentSuccess) {
				//log.error("결제 실패");
				throw new Exception("결제 실패");
			}

			// 결제 성공 시 처리 로직
			//log.info("결제 성공");
			orderClient.handlePaymentSuccess(userId, orderId);

		} catch (Exception e) {
			// 결제 실패 시 실패 메시지 생성 및 전송
			//log.info("Payment failed for order:" + orderId);
			kafkaTemplate.send("payment-fail", paymentRequest.orderId());
		}
	}

	private boolean processPayment(PaymentRequest paymentRequest) {
		Random random = new Random();
		double failureProbability = 0.20; // 실패 확률 (20%)

		return random.nextDouble() > failureProbability;
	}
}
