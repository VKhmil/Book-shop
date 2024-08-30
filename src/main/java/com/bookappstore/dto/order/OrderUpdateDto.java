package com.bookappstore.dto.order;

import com.bookappstore.model.Order;

public record OrderUpdateDto(
        Order.Status status
) {
}
