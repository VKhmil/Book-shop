package com.bookappstore.dto.orderitem;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        int quantity
) {
}
