package com.bookappstore.repository.order;

import com.bookappstore.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> getByOrderIdAndItemId(@Param("orderId") Long orderId,
                                              @Param("itemId") Long itemId);
}
