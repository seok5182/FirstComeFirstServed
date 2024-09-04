package com.sparta.order.repository;

import com.sparta.order.entity.Order;
import com.sparta.order.entity.OrderStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findAllByUserIdOrderByIdAsc(Long userId);
	List<Order> findByOrderStatus(OrderStatus orderStatus);
}
