package com.sparta.product.redis;

import com.sparta.product.entity.Product;
import com.sparta.product.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockService {

	private final StringRedisTemplate redisTemplate;
	private final ProductRepository productRepository;

	private static final String STOCK_KEY_PREFIX = "stock:";
	private static final String RESERVED_KEY_PREFIX = "reserved:stock:";

	@PostConstruct
	public void initRedis() {
		List<Product> products = productRepository.findAll();

		for (Product product : products) {
			String stockKey = STOCK_KEY_PREFIX + product.getId();
			String reservedKey = RESERVED_KEY_PREFIX + product.getId();
			redisTemplate.opsForValue().set(stockKey, String.valueOf(product.getQuantity()));
			redisTemplate.opsForValue().set(reservedKey, String.valueOf(0));
		}

		log.info("모든 상품의 재고가 Redis에 초기화되었습니다.");
	}
}
