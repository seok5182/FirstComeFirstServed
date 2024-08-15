package com.sparta.firstcomefirstserved.repository;

import com.sparta.firstcomefirstserved.entity.Cart;
import com.sparta.firstcomefirstserved.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<List<Cart>> findAllByUser(User user);

	Page<Cart> findAllByUser(User user, Pageable pageable);

	void deleteAllByUser(User user);

}
