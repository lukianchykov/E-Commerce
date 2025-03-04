package com.gbsfo.ecommerce.facade;

import java.util.List;
import java.util.Optional;

import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.IterableDataResponse;
import com.gbsfo.ecommerce.dto.OrderDto;
import com.gbsfo.ecommerce.dto.OrderLookupPublicApiRequest;
import com.gbsfo.ecommerce.dto.OrderUpsertRequest;
import com.gbsfo.ecommerce.mapper.OrderMapper;
import com.gbsfo.ecommerce.service.impl.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OrderFacade.class)
@RunWith(SpringRunner.class)
public class OrderFacadeTest {

    private static final Long ORDER_ID = 1L;

    private static final String NUMBER = "number";

    @Autowired
    private OrderFacade orderFacade;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    @MockBean
    private ItemDto mockItemDto;

    @MockBean
    private OrderDto mockOrderDto;

    @MockBean
    private Order mockOrder;

//    @Test
//    public void testFind() {
//        var request = new OrderLookupPublicApiRequest();
//        var orders = List.of(new Order());
//        var page = new PageImpl<>(orders);
//
//        when(orderService.findOrders(request)).thenReturn(page);
//        when(orderMapper.toDto(mockOrder)).thenReturn(mockOrderDto);
//
//        IterableDataResponse<List<OrderDto>> response = orderFacade.find(request);
//
//        assertNotNull(response);
//        assertEquals(1, response.getData().size());
//        assertFalse(response.isHasNext());
//    }

    @Test
    public void testGetOrderById() {
        when(orderService.getOrderById(anyLong())).thenReturn(mockOrder);
        when(orderMapper.toDto(mockOrder)).thenReturn(mockOrderDto);

        OrderDto result = orderFacade.getOrderById(ORDER_ID);

        assertNotNull(result);
        verify(orderMapper).toDto(mockOrder);
    }

    @Test
    public void testFindByNumber() {
        when(orderService.findByNumber(NUMBER)).thenReturn(Optional.of(mockOrder));
        when(orderMapper.toDto(mockOrder)).thenReturn(mockOrderDto);

        OrderDto result = orderFacade.findByNumber(NUMBER);

        assertNotNull(result);
        verify(orderMapper).toDto(mockOrder);
    }

    @Test
    public void testCreateOrder() {
        when(orderService.createOrder(any(OrderUpsertRequest.class))).thenReturn(mockOrder);
        when(orderMapper.toDto(mockOrder)).thenReturn(mockOrderDto);
        OrderDto result = orderFacade.createOrder(new OrderUpsertRequest());

        assertNotNull(result);
        verify(orderMapper).toDto(mockOrder);
    }

    @Test
    public void testAddItemsToOrder() {
        var items = List.of(mockItemDto);
        when(orderService.addItemsInOrder(ORDER_ID, items)).thenReturn(mockOrder);
        when(orderMapper.toDto(mockOrder)).thenReturn(mockOrderDto);
        OrderDto result = orderFacade.addItemsToOrder(ORDER_ID, items);

        assertNotNull(result);
        verify(orderMapper).toDto(mockOrder);
    }

    @Test
    public void testUpdateOrder() {
        when(orderService.updateOrder(anyLong(), any(OrderUpsertRequest.class))).thenReturn(mockOrder);
        when(orderMapper.toDto(mockOrder)).thenReturn(mockOrderDto);
        OrderDto result = orderFacade.updateOrder(ORDER_ID, new OrderUpsertRequest());

        assertNotNull(result);
        verify(orderMapper).toDto(mockOrder);
    }

    @Test
    public void testDeleteOrder() {
        orderFacade.deleteOrder(ORDER_ID);

        verify(orderService, times(1)).deleteOrder(ORDER_ID);
    }

}
