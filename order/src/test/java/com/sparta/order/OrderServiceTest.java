package com.sparta.order;

import com.sparta.order.dto.CartItemRequest;
import com.sparta.order.service.CartService;
import com.sparta.order.service.OrderService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;

	// 유저들의 장바구니에 상품 담아두기
	@Test
	void addCartItem() {
		// 500명의 유저가 상품(id:11)을 한개씩 주문하기 위한 장바구니 담기 작업
		for (int i = 1; i <= 500; i++) {
			CartItemRequest cartItemRequest = new CartItemRequest((long) 11, 1);
			cartService.addCartItem((long) i, cartItemRequest);
		}
	}

	// 500명이 한번에 주문하는 테스트
	@Test
	void test1() throws InterruptedException {
		// 스레드 풀 설정
		int threadCount = 500;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		// 주어진 수 만큼 이벤트를 기다림
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 1; i <= threadCount; i++) {
			Long userId = (long) (i);

			executorService.submit(() -> {
				try{
					System.out.println(userId + "번째 쓰레드 접근 시작");
					orderService.makeOrder(userId);
				} finally {
					latch.countDown();
					System.out.println(userId + "번째 쓰레드 접근 종료");
				}
			});
		}

		latch.await();
		executorService.shutdown();
	}
}
