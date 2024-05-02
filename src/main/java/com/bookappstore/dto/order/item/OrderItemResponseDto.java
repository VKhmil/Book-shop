package com.bookappstore.dto.order.item;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        int quantity
) {
}
