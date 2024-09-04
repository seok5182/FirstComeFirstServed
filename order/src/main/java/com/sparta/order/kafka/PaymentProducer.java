package com.sparta.order.kafka;

import com.sparta.order.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentProducer {

	private final KafkaTemplate<String, PaymentRequest> kafkaTemplate;

	public void sendPaymentRequest(PaymentRequest paymentRequest) {
		//log.info("Sending payment request: {}", paymentRequest);
		kafkaTemplate.send("payment-request", paymentRequest);
	}
}
