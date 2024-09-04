package com.sparta.order.repository;

import com.sparta.order.entity.Cart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

	List<Cart> findAllByUserId(Long userId);

	void deleteById(Long cartId);

	void deleteAllByUserId(Long userId);

	Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);
}
