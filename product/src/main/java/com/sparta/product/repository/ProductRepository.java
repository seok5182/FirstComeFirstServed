package com.sparta.product.repository;

import com.sparta.product.entity.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Page<Product> findAllByIsDeletedFalse(Pageable pageable);

	Optional<Product> findByIdAndIsDeletedFalse(Long productId);
}
