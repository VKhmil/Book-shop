package com.bookappstore.controller;

import com.bookappstore.dto.order.OrderRequestDto;
import com.bookappstore.dto.order.OrderResponseDto;
import com.bookappstore.dto.order.OrderUpdateDto;
import com.bookappstore.dto.order.item.OrderItemResponseDto;
import com.bookappstore.model.User;
import com.bookappstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Place an order",
            description = "Allows authenticated users to place a new order")
    public OrderResponseDto placeOrder(
            Authentication authentication,
            @RequestBody @Valid OrderRequestDto orderRequestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.create(user.getId(), orderRequestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Retrieve user's order history",
            description = "Allows authenticated users to retrieve their order history")
    public List<OrderResponseDto> getOrders(
            Authentication authentication,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrders(user.getId(), pageable);
    }

    @Operation(summary = "Update order status",
            description = "Update order status")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateOrderStatus(@PathVariable Long id,
                                  @RequestBody OrderUpdateDto orderUpdateRequestDto) {
        orderService.setUpdateStatus(id, orderUpdateRequestDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Retrieve all OrderItems for a specific order",
            description = "Allows authenticated users to "
                    + "retrieve all OrderItems for a specific order")
    public Set<OrderItemResponseDto> getOrderItems(
            @PathVariable Long orderId) {
        return orderService.findAllOrderItems(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Retrieve a specific OrderItem within an order",
            description = "Allows authenticated users "
                    + "to retrieve a specific OrderItem within an order")
    public List<OrderItemResponseDto> getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getItems(orderId, itemId);

    }
}
