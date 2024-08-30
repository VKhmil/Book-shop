package com.bookappstore.repository.order;

import com.bookappstore.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi "
            + "WHERE oi.order.id = :orderId "
            + "AND oi.id = :orderItemId "
            + "AND oi.order.user.id = :userId")
    Optional<OrderItem> findOrderItemByOrderIdAndIdAndUserId(@Param("orderId") Long orderId,
                                                             @Param("orderItemId") Long orderItemId,
                                                             @Param("userId") Long userId);
}
