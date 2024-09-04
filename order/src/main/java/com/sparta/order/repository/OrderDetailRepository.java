package com.sparta.order.repository;

import com.sparta.order.entity.OrderDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

	List<OrderDetail> findAllByOrderId(Long orderId);

	void deleteAllByOrderId(Long orderId);

	List<OrderDetail> findByOrderId(Long orderId);
}
