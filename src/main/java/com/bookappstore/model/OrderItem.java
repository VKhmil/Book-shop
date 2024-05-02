package com.bookappstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private boolean isDeleted = false;

    public OrderItem() {
    }

    public OrderItem(CartItem cartItem) {
        this.book = cartItem.getBook();
        this.price = cartItem.getBook().getPrice();
        this.quantity = cartItem.getQuantity();
    }

    @Override // TODO
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id)
                && Objects.equals(order, orderItem.order)
                && Objects.equals(book, orderItem.book)
                && Objects.equals(price, orderItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, book, price);
    }
}
