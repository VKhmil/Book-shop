package com.bookappstore.service.impl;

import com.bookappstore.dto.order.OrderRequestDto;
import com.bookappstore.dto.order.OrderResponseDto;
import com.bookappstore.dto.order.OrderUpdateDto;
import com.bookappstore.dto.orderitem.OrderItemResponseDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.OrderItemMapper;
import com.bookappstore.mapper.OrderMapper;
import com.bookappstore.mapper.ShoppingCartMapper;
import com.bookappstore.model.Order;
import com.bookappstore.model.OrderItem;
import com.bookappstore.model.ShoppingCart;
import com.bookappstore.repository.cart.ShoppingCartRepository;
import com.bookappstore.repository.order.OrderItemRepository;
import com.bookappstore.repository.order.OrderRepository;
import com.bookappstore.service.OrderService;
import com.bookappstore.service.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final ShoppingCartMapper shoppingCartMapper;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderResponseDto> findAllOrders(Long userId, Pageable pageable) {
        return orderMapper.toDtoList(orderRepository
                .findOrdersByUserId(userId));
    }

    @Transactional
    @Override
    public OrderResponseDto create(Long userId, OrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = getShoppingCart(userId);
        Order userOrder = orderRepository.save(
                initializeNewOrder(shoppingCart, orderRequestDto));
        orderItemRepository.saveAll(userOrder.getOrderItems()
                .stream()
                .peek(orderItem -> orderItem.setOrder(userOrder))
                .collect(Collectors.toSet()));
        return orderMapper.toDto(userOrder);
    }

    @Override
    @Transactional
    public OrderResponseDto setUpdateStatus(Long orderId, OrderUpdateDto orderUpdateDto) {
        Order order = getOrderById(orderId);
        Order.Status status = Order.Status.valueOf(orderUpdateDto
                .status()
                .toString()
                .toUpperCase());
        order.setStatus(status);
        orderRepository.save(order);
        return orderMapper.toDto(order);
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

    private Order initializeNewOrder(ShoppingCart shoppingCart, OrderRequestDto requestDto) {
        Order newOrder = shoppingCartMapper.toOrder(shoppingCart);
        newOrder.setShippingAddress(requestDto.shippingAddress());
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setStatus(Order.Status.NEW);
        newOrder.setTotal(countTotal(newOrder));
        return newOrder;
    }

    private BigDecimal countTotal(Order newOrder) {
        return newOrder.getOrderItems()
                .stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ShoppingCart getShoppingCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by user id: " + userId)
        );
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find order by id: " + orderId));
    }

}
