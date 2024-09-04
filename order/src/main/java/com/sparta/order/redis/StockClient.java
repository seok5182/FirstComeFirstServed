package com.sparta.order.redis;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StockClient {

	private final StringRedisTemplate redisTemplate;
	private final RedissonClient redisson;

	private static final String STOCK_KEY_PREFIX = "stock:";
	private static final String LOCK_KEY_PREFIX = "lock:stock:";
	private static final String RESERVED_KEY_PREFIX = "reserved:stock:";

	public boolean verifyStock(Map<String, Integer> productQuantities) {
		boolean allAvailable = true;

		for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
			String productId = entry.getKey();
			Integer quantity = entry.getValue();
			String stockKey = STOCK_KEY_PREFIX + productId;
			String lockKey = LOCK_KEY_PREFIX + productId;
			String reservedKey = RESERVED_KEY_PREFIX + productId;

			// Redisson을 이용한 분산락 획득
			RLock lock = redisson.getLock(lockKey);

			try {
				// 락을 획득하려고 시도 (최대 10초 대기, 5초 동안 락 유지)
				if (lock.tryLock(10, 5, TimeUnit.SECONDS)) {
					try {
						// 현재 재고 가져오기
						String currentStockStr = redisTemplate.opsForValue().get(stockKey);
						// 예약 재고 가져오기
						String reservedStockStr = redisTemplate.opsForValue().get(reservedKey);
						if (currentStockStr == null) {
							allAvailable = false;
							break;
						}

						int currentStock = Integer.parseInt(currentStockStr);
						int reservedStock = Integer.parseInt(reservedStockStr);

						// 실제 사용 가능한 재고 = 현재 재고 - 예약된 재고
						int availableStock = currentStock - reservedStock;

						// 재고 부족 여부 확인
						if (availableStock < quantity) {
							allAvailable = false;
							break;
						}

						// 예약 재고 증가
						redisTemplate.opsForValue().increment(reservedKey, quantity);

					} finally {
						lock.unlock();
					}
				} else {
					allAvailable = false;
					break;
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				allAvailable = false;
				break;
			}
		}

		return allAvailable;
	}

	@Async
	public CompletableFuture<Void> decreaseStockAfterPayment(Long userId, Map<String, Integer> productQuantities) {
		for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
			String productId = entry.getKey();
			Integer quantity = entry.getValue();
			String stockKey = STOCK_KEY_PREFIX + productId;
			String lockKey = LOCK_KEY_PREFIX + productId;
			String reservedKey = RESERVED_KEY_PREFIX + productId;

			// Redisson을 이용한 분산락 획득
			RLock lock = redisson.getLock(lockKey);

			try {
				if (lock.tryLock(10, 5, TimeUnit.SECONDS)) {
					try {
						// 결제가 성공한 경우 재고, 예약 재고 감소
						redisTemplate.opsForValue().decrement(stockKey, quantity);
						redisTemplate.opsForValue().decrement(reservedKey, quantity);
					} finally {
						// 락 해제
						lock.unlock();
					}
				} else {
					// 락을 획득하지 못한 경우
					throw new IllegalStateException(
						"Unable to acquire lock for product: " + productId);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("Lock acquisition interrupted for product: " + productId,
					e);
			}
		}

		return CompletableFuture.completedFuture(null);
	}

	// 예약된 재고 해제 메서드
	public void releaseReservedStock(Map<String, Integer> productQuantities) {
		for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
			String productId = entry.getKey();
			Integer quantity = entry.getValue();
			String reservedKey = RESERVED_KEY_PREFIX + productId;

			redisTemplate.opsForValue().decrement(reservedKey, quantity);
		}
	}

	// 주문 취소, 반품으로 인한 재고 원복
	public void restoreStock(Map<String, Integer> productQuantities) {
		for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
			String productId = entry.getKey();
			String stockKey = STOCK_KEY_PREFIX + productId;
			Integer quantity = entry.getValue();

			redisTemplate.opsForValue().increment(stockKey, quantity);
		}
	}
}
