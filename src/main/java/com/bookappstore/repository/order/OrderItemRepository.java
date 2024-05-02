package com.bookappstore.repository.order;

import com.bookappstore.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.id = :orderId AND oi.id = :itemId")
    Optional<OrderItem> getById(@Param("orderId") Long orderId, @Param("itemId") Long itemId);
}
