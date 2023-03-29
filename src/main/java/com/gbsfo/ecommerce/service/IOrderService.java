package com.gbsfo.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.OrderLookupPublicApiRequest;
import com.gbsfo.ecommerce.dto.OrderUpsertRequest;

import org.springframework.data.domain.Page;

public interface IOrderService {

    Order getOrderById(Long orderId);

    Page<Order> findOrders(OrderLookupPublicApiRequest request);

    Optional<Order> findByNumber(String number);

    Order addItemsInOrder(Long orderId, List<ItemDto> items);

    Order createOrder(OrderUpsertRequest orderUpsertRequest);

    Order updateOrder(Long orderId, OrderUpsertRequest orderUpsertRequest);

    void deleteOrder(Long orderId);
}
