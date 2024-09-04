package com.sparta.product.util;//package com.sparta.product.util;
//
//import com.sparta.product.entity.Product;
//import com.sparta.product.repository.ProductRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TestDataRunner implements ApplicationRunner {
//
//	@Autowired
//	ProductRepository productRepository;
//
//	public void run(ApplicationArguments args) {
//
//		// 상품 생성
//		createTestData("신발");
//		createTestData("과자");
//		createTestData("키보드");
//		createTestData("휴지");
//		createTestData("휴대폰");
//		createTestData("앨범");
//		createTestData("헤드폰");
//		createTestData("이어폰");
//		createTestData("노트북");
//		createTestData("무선 이어폰");
//		createTestData("모니터");
//	}
//
//	private void createTestData(String name) {
//
//		Product product = Product.of(name, "테스트 " + name, getRandomPrice(), getRandomQuantity());
//
//		productRepository.save(product);
//	}
//
//	public int getRandomPrice() {
//		return (int) ((Math.random() * (100000)) + 5000);
//	}
//
//	public int getRandomQuantity() {
//		return (int) ((Math.random() * (100)) + 1);
//	}
//}
