package com.bookappstore.repository.cart;

import com.bookappstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph
    Optional<CartItem> findByIdAndShoppingCartId(Long shoppingCartId, Long cartItemId);
}
