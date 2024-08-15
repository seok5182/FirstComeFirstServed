package com.sparta.firstcomefirstserved.repository;

import com.sparta.firstcomefirstserved.entity.Order;
import com.sparta.firstcomefirstserved.entity.OrderDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

	List<OrderDetail> findByOrder(Order order);

}
