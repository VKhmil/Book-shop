package com.bookappstore.repository.cart;

import com.bookappstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Modifying
    @Query("UPDATE CartItem item "
            + "SET item.quantity = :quantity "
            + "WHERE item.id = :id")
    void updateCartItemQuantityById(int quantity, Long id);

    @Query("select c from CartItem c where c.id = ?1 and c.shoppingCart.id = ?2")
    CartItem findByIdAndShoppingCartId(Long itemId, Long cartId);
}
