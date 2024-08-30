package com.bookappstore.repository.order;

import com.bookappstore.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "orderItems")
    List<Order> findOrdersByUserId(Long userId);

    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
