package com.bookappstore.mapper;

import com.bookappstore.config.MapperConfig;
import com.bookappstore.dto.order.OrderResponseDto;
import com.bookappstore.model.Order;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDtoList(List<Order> order);

    Order toModel(OrderResponseDto responseDto);
}
