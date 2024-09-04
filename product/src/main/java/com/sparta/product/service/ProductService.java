package com.sparta.product.service;

import com.sparta.product.dto.ProductResponse;
import com.sparta.product.dto.QuantityRequest;
import com.sparta.product.entity.Product;
import com.sparta.product.repository.ProductRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	// 전체 상품 조회
	@Transactional(readOnly = true)
	public Page<ProductResponse> getProducts(int page, int size, String sortBy, boolean isAsc) {
		Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(direction, sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Product> productList;

		productList = productRepository.findAllByIsDeletedFalse(pageable);

		return productList.map(ProductResponse::from);
	}

	// Redis 부팅 시 재고 입력을 위함
	public Map<String, Integer> getAllProductStocks() {
		Map<String, Integer> stockMap = new HashMap<>();
		List<Product> products = productRepository.findAll();
		for (Product product : products) {
			stockMap.put(product.getId().toString(), product.getQuantity());
		}
		return stockMap;
	}

	// 상품 상세 조회
	public ProductResponse getProduct(Long productId) {

		Product product = productRepository.findByIdAndIsDeletedFalse(productId).orElseThrow(() ->
			new NullPointerException("해당 상품은 존재하지 않습니다"));

		return ProductResponse.from(product);
	}

	// 상품 재고 마이너스
	@Transactional
	public void decreaseProduct(List<QuantityRequest> quantityRequests) {
		for (QuantityRequest quantityRequest : quantityRequests) {
			Product product = productRepository.findByIdAndIsDeletedFalse(
				quantityRequest.productId()).orElseThrow(() ->
				new NullPointerException("해당 상품이 없습니다"));
			int quantity = quantityRequest.quantity();

			product.minusQuantity(quantity);
		}
	}

	// 상품 재고 플러스
	@Transactional
	public void increaseProduct(List<QuantityRequest> quantityRequests) {
		for (QuantityRequest quantityRequest : quantityRequests) {
			Product product = productRepository.findByIdAndIsDeletedFalse(
				quantityRequest.productId()).orElseThrow(() ->
				new NullPointerException("해당 상품이 없습니다"));
			int quantity = quantityRequest.quantity();

			product.plusQuantity(quantity);
		}
	}

}
