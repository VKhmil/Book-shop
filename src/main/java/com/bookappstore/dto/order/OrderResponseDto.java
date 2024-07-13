package com.bookappstore.dto.order;

import com.bookappstore.dto.orderitem.OrderItemResponseDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private BigDecimal total;
    private LocalDateTime orderTime;
    private String shippingAddress;
    private Set<OrderItemResponseDto> orderItemResponseDtos;
    private String status;
}
