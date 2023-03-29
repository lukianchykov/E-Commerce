package com.gbsfo.ecommerce.facade;

import java.util.List;
import java.util.stream.Collectors;

import com.gbsfo.ecommerce.controller.exception.ResourceNotFoundException;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.IterableDataResponse;
import com.gbsfo.ecommerce.dto.OrderDto;
import com.gbsfo.ecommerce.dto.OrderLookupPublicApiRequest;
import com.gbsfo.ecommerce.dto.OrderUpsertRequest;
import com.gbsfo.ecommerce.mapper.OrderMapper;
import com.gbsfo.ecommerce.service.impl.OrderService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderFacade {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    public IterableDataResponse<List<OrderDto>> find(OrderLookupPublicApiRequest orderLookupRequest) {
        var page = orderService.findOrders(orderLookupRequest);

        var orders = page.getContent().stream()
            .map(orderMapper::toDto)
            .collect(Collectors.toList());
        return new IterableDataResponse<>(orders, page.hasNext());
    }

    public OrderDto getOrderById(Long orderId) {
        var order = orderService.getOrderById(orderId);
        return orderService.getOrderById(orderId) != null ? orderMapper.toDto(order) : null;
    }

    public OrderDto findByNumber(String number) {
        var orderByNumber = orderService.findByNumber(number)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found by number", number));
        return orderMapper.toDto(orderByNumber);
    }

    public OrderDto createOrder(OrderUpsertRequest orderUpsertRequest) {
        var order = orderService.createOrder(orderUpsertRequest);
        return orderMapper.toDto(order);
    }

    public OrderDto addItemsToOrder(Long orderId, List<ItemDto> items) {
        var order = orderService.addItemsInOrder(orderId, items);
        return orderMapper.toDto(order);
    }

    public OrderDto updateOrder(Long orderId, OrderUpsertRequest orderUpsertRequest) {
        var order = orderService.updateOrder(orderId, orderUpsertRequest);
        return orderMapper.toDto(order);
    }

    public void deleteOrder(Long orderId) {
        orderService.deleteOrder(orderId);
    }
}
