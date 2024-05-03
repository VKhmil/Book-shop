package com.bookappstore.service.impl;

import com.bookappstore.dto.order.OrderRequestDto;
import com.bookappstore.dto.order.OrderResponseDto;
import com.bookappstore.dto.order.OrderUpdateDto;
import com.bookappstore.dto.order.item.OrderItemResponseDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.OrderItemMapper;
import com.bookappstore.mapper.OrderMapper;
import com.bookappstore.model.CartItem;
import com.bookappstore.model.Order;
import com.bookappstore.model.OrderItem;
import com.bookappstore.model.ShoppingCart;
import com.bookappstore.repository.cart.ShoppingCartRepository;
import com.bookappstore.repository.order.OrderItemRepository;
import com.bookappstore.repository.order.OrderRepository;
import com.bookappstore.service.OrderService;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderResponseDto> findAllOrders(Long userId, Pageable pageable) {
        return orderMapper.toDtoList(orderRepository
                .findAllOrders(userId));
    }

    @Transactional
    @Override
    public OrderResponseDto create(Long userId, OrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = getShoppingCart(userId);
        Order userOrder = new Order(shoppingCart);
        userOrder.setShippingAddress(orderRequestDto.shippingAddress());
        userOrder.setTotal(shoppingCart.getCartItems().stream()
                .map(total -> total.getBook().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        Set<OrderItem> orderItems = new HashSet<>();

        for (CartItem item : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem(item);
            orderItems.add(orderItem);
        }
        userOrder.setOrderItems(orderItems);
        orderRepository.save(userOrder);
        return orderMapper.toDto(userOrder);
    }

    @Override
    @Transactional
    public void setUpdateStatus(Long userId, OrderUpdateDto orderUpdateDto) {
        orderRepository.updateOrderByStatus(userId, orderUpdateDto.status());
    }

    private ShoppingCart getShoppingCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by user id: " + userId)
        );
    }

    @Override
    public List<OrderItemResponseDto> getItems(Long orderId, Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Can not find order by id:" + id));

        return orderItemMapper.toDtos(order.getOrderItems());
    }

    @Override
    public Set<OrderItemResponseDto> findAllOrderItems(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order by id: " + orderId)
                );
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

}
