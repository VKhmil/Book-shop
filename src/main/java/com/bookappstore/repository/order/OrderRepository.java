package com.bookappstore.repository.order;

import com.bookappstore.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems"})
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findAllOrders(Long userId);

    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :orderId")
    void updateOrderByStatus(Long orderId, Order.Status status);

}
