package com.gbsfo.ecommerce.mapper;

import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.WARN,
    uses = {PaymentMapper.class, ItemMapper.class}
)
public interface OrderMapper {

    @Mapping(target = "items", source = "total_items")
    @Mapping(target = "payments", source = "total_payments")
    @Mapping(target = "orderStatus", source = "orderStatus")
    OrderDto toDto(Order order);

    @Mapping(target = "total_items", source = "items")
    @Mapping(target = "total_payments", source = "payments")
    @Mapping(target = "orderStatus", source = "orderStatus")
    Order toEntity(OrderDto orderDto);
}
