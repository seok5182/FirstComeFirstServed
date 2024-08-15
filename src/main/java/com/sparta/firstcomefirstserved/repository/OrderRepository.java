package com.sparta.firstcomefirstserved.repository;

import com.sparta.firstcomefirstserved.entity.Order;
import com.sparta.firstcomefirstserved.entity.OrderStatus;
import com.sparta.firstcomefirstserved.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findAllByUser(User user);

	List<Order> findByStatus(OrderStatus orderStatus);
}
