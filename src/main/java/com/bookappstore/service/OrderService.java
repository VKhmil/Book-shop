package com.bookappstore.service;

import com.bookappstore.dto.order.OrderRequestDto;
import com.bookappstore.dto.order.OrderResponseDto;
import com.bookappstore.dto.order.OrderUpdateDto;
import com.bookappstore.dto.orderitem.OrderItemResponseDto;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    List<OrderResponseDto> findAllOrders(Long userId, Pageable pageable);

    OrderResponseDto create(Long userId, OrderRequestDto orderRequestDto);

    OrderResponseDto setUpdateStatus(Long userId, OrderUpdateDto orderUpdateDto);

    List<OrderItemResponseDto> findOrderItemById(Long orderId, Long id);

    Set<OrderItemResponseDto> findAllOrderItems(Long orderId, Long userId);
}
