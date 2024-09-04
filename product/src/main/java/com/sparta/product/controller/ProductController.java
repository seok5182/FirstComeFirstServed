package com.sparta.product.controller;

import com.sparta.product.dto.ProductResponse;
import com.sparta.product.dto.QuantityRequest;
import com.sparta.product.service.ProductService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	// 상품 전체 조회
	@GetMapping
	public ResponseEntity<Page<ProductResponse>> getProducts(
		@RequestParam("page") int page,
		@RequestParam("size") int size,
		@RequestParam("sortBy") String sortBy,
		@RequestParam("isAsc") boolean isAsc) {

		return ResponseEntity.ok(productService.getProducts(page - 1, size, sortBy, isAsc));
	}

	// Redis 서버에 부팅 시 재고 미리 넣어두기
	@GetMapping("/stock")
	public Map<String, Integer> getAllProductStocks() {
		return productService.getAllProductStocks();
	}

	// 상품 상세 조회
	@GetMapping("/{productId}")
	public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") Long itemId) {
		return ResponseEntity.ok(productService.getProduct(itemId));
	}

	// 상품 재고 감소(결제 성공)
	@PostMapping("/decrease")
	public ResponseEntity<Void> decreaseQuantity(@RequestBody List<QuantityRequest> quantityRequests) {
		try {
			productService.decreaseProduct(quantityRequests);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	// 상품 재고 증가(주문 취소, 주문 반품)
	@PostMapping("/increase")
	public ResponseEntity<Void> increaseQuantity(@RequestBody List<QuantityRequest> quantityRequests) {
		try {
			productService.increaseProduct(quantityRequests);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}
