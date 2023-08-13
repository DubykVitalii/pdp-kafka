package com.avenga.orderservice.repository;

import com.avenga.orderservice.model.enumeration.OrderStatus;
import com.avenga.orderservice.model.persistence.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where coalesce(:status, o.status) = o.status")
    List<Order> findAllOrdersByStatus(@Param("status") OrderStatus status);

    @Query("select o from Order o where o.customerId= :customerId")
    List<Order> findAllOrdersByCustomerId(@Param("customerId") Long customerId);
}
