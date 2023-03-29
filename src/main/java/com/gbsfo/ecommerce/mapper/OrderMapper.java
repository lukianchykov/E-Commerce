package com.gbsfo.ecommerce.mapper;

import java.util.List;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.dto.OrderDto;
import com.gbsfo.ecommerce.dto.OrderUpsertRequest;
import com.gbsfo.ecommerce.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
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

    @Mapping(source = "orderUpsertRequest", target = "total_items", qualifiedByName = "mapOrderUpsertRequestItems")
    @Mapping(source = "orderUpsertRequest", target = "total_payments", qualifiedByName = "mapOrderUpsertRequestPayments")
    Order toEntity(OrderUpsertRequest orderUpsertRequest);

    @Named("mapOrderUpsertRequestItems")
    default List<Item> mapOrderUpsertRequestItems(OrderUpsertRequest orderUpsertRequest) {
        return MapperUtils.mapItems(orderUpsertRequest.getItems());
    }

    @Named("mapOrderUpsertRequestPayments")
    default List<Payment> mapOrderUpsertRequestPayments(OrderUpsertRequest orderUpsertRequest) {
        return MapperUtils.mapPayments(orderUpsertRequest.getPayments());
    }

}
