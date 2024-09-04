package com.sparta.payment.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.payment.dto.PaymentRequest;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;

public class PaymentRequestDeserializer implements Deserializer<PaymentRequest> {

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {

	}

	@Override
	public PaymentRequest deserialize(String topic, byte[] data) {
		if (data == null) {
			return null;
		}
		try {
			return mapper.readValue(data, PaymentRequest.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to deserialize JSON to PaymentRequest", e);
		}
	}

	@Override
	public void close() {
		// Clean up resources if needed
	}

}
