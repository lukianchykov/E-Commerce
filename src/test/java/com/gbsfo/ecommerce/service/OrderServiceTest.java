package com.gbsfo.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Order.OrderStatus;
import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.dto.OrderDto;
import com.gbsfo.ecommerce.dto.OrderLookupPublicApiRequest;
import com.gbsfo.ecommerce.mapper.OrderMapper;
import com.gbsfo.ecommerce.repository.OrderRepository;
import com.gbsfo.ecommerce.service.impl.OrderService;
import com.gbsfo.ecommerce.utils.time.TimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import({OrderService.class, TimeUtils.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TimeUtils timeUtils;

    @MockBean
    private OrderMapper orderMapper;

    private Order order;

    @Before
    public void setUp() {

        Item item1 = Item.builder().name("Item 1").price(new BigDecimal("10.00")).order(order).build();
        Item item2 = Item.builder().name("Item 2").price(new BigDecimal("20.00")).order(order).build();

        Payment payment1 = Payment.builder().number("number 1").sum(new BigDecimal("10.00")).paymentDateTime(timeUtils.getCurrentTime()).build();
        Payment payment2 = Payment.builder().number("number 2").sum(new BigDecimal("20.00")).paymentDateTime(timeUtils.getCurrentTime()).build();

        order = Order.builder().number("ORD-001").orderStatus(OrderStatus.CREATED).total_items(List.of(item1, item2))
            .total_payments(List.of(payment1, payment2)).build();
        orderRepository.save(order);
    }

    @After
    public void cleanup() {
        orderRepository.delete(order);
    }

    @Test
    public void verify_findOrderById() {
        Order actual = orderService.getOrderById(order.getId());
        assertEquals(order, actual);
    }

    @Test
    public void findOrders_withFullSearch() {
        OrderLookupPublicApiRequest orderLookupPublicApiRequest = OrderLookupPublicApiRequest.builder()
            .number("ORD-001")
            .orderStatus("CREATED")
            .offset(0)
            .limit(20)
            .build();

        Page<Order> actual = orderService.findOrders(orderLookupPublicApiRequest);

        assertEquals(order, actual.getContent().get(0));
    }

    @Test
    public void findOrders_withPartialSearch() {
        OrderLookupPublicApiRequest orderLookupPublicApiRequest = OrderLookupPublicApiRequest.builder()
            .number("ORD-001")
            .offset(0)
            .limit(20)
            .build();

        Page<Order> actual = orderService.findOrders(orderLookupPublicApiRequest);

        assertEquals(order, actual.getContent().get(0));
    }

    @Test
    public void findOrders_withPartialSearchWhenThereIsWrongField_shouldNotFindEntityAndTrowAnError() {
        OrderLookupPublicApiRequest orderLookupPublicApiRequest = OrderLookupPublicApiRequest.builder()
            .number("INVALID")
            .orderStatus("INVALID")
            .offset(0)
            .limit(20)
            .build();

        assertThrows(ValidationException.class, () -> orderService.findOrders(orderLookupPublicApiRequest));
    }

    @Test
    public void createOrder() {
        Order mock = Order.builder().number("fake").build();
        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(mock);
        OrderDto orderDto = OrderDto.builder().number("number").orderStatus(OrderDto.OrderStatus.CREATED).build();

        Order createdOrder = orderService.createOrder(orderDto);

        assertNotNull(orderRepository.findById(createdOrder.getId()));
        assertEquals(mock, createdOrder);
    }

    @Test
    public void createOrder_whenNoItemsAndPayments_verifyItemsAndPaymentsSavedSuccessfully() {
        Order newOrder = Order.builder().number("number 1").build();
        Item newItem = Item.builder().name("Item 1").price(new BigDecimal("10.00")).build();
        newOrder.getTotal_items().add(newItem);
        Payment newPayment = Payment.builder().number("number 1").sum(new BigDecimal("10.00")).paymentDateTime(timeUtils.getCurrentTime()).build();
        newOrder.getTotal_payments().add(newPayment);

        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(newOrder);
        OrderDto orderDto = OrderDto.builder().number("number 1 Dto").build();

        Order createdOrder = orderService.createOrder(orderDto);

        assertNotNull(orderRepository.findById(createdOrder.getId()));
        assertFalse(createdOrder.getTotal_items().isEmpty());
        assertFalse(createdOrder.getTotal_payments().isEmpty());
    }

    @Test
    public void updateOrder_shouldUpdateOrderInDatabase() {
        OrderDto orderDto = OrderDto.builder().id(4L).number("number123").orderStatus(OrderDto.OrderStatus.CREATED).build();
        Order orderInDatabase = Order.builder().id(3L).number("Numb1").orderStatus(OrderStatus.CREATED).build();

        when(orderMapper.toEntity(orderDto)).thenReturn(Order.builder().number(orderDto.getNumber()).build());

        Order savedOrder = orderRepository.save(orderInDatabase);
        assertNotNull(orderRepository.findById(savedOrder.getId()));

        assertNotEquals(orderDto.getNumber(), savedOrder.getNumber());

        Order updatedOrder = orderService.updateOrder(savedOrder.getId(), orderDto);
        assertEquals(orderDto.getNumber(), updatedOrder.getNumber());
    }

    @Test
    public void updateOrder_whenRequestHaveNewItemsAndPayments_verifyItemsAndPaymentsUpdatedSuccessfully() {
        Order newOrder = Order.builder().id(3L).number("Numb1").orderStatus(OrderStatus.CREATED).build();
        Item newItem = Item.builder().name("Item 1").price(new BigDecimal("10.00")).build();
        newOrder.getTotal_items().add(newItem);
        Payment newPayment = Payment.builder().number("number 1").sum(new BigDecimal("10.00")).paymentDateTime(timeUtils.getCurrentTime()).build();
        newOrder.getTotal_payments().add(newPayment);

        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(newOrder);
        OrderDto orderDto = OrderDto.builder().number("number123").build();

        Order savedOrder = orderRepository.save(newOrder);
        assertNotNull(orderRepository.findById(savedOrder.getId()));

        Order updateOrder = orderService.updateOrder(savedOrder.getId(), orderDto);

        assertNotNull(orderRepository.findById(updateOrder.getId()));
        // verify new email added
        assertTrue(updateOrder.getTotal_items().stream()
            .anyMatch(item -> item.getName().equals(newItem.getName())));
        assertTrue(updateOrder.getTotal_payments().stream()
            .anyMatch(payment -> payment.getNumber().equals(newPayment.getNumber())));
    }

    @Test
    public void deleteOrder() {
        orderService.deleteOrder(order.getId());
        assertEquals(Optional.empty(), orderRepository.findById(order.getId()));
    }

    @Test
    public void verify_CanNotDeleteOrderInShippingStatus() {
        order.setOrderStatus(OrderStatus.SHIPPING);
        assertFalse(order.canDelete(order));
        assertThrows(IllegalStateException.class, () -> orderService.deleteOrder(order.getId()));
    }

    @Test
    public void verify_CanNotDeleteOrderInDeliveredStatusUntilAllItemsArePaid() {
        Order newOrder = Order.builder().id(3L).number("Numb1").orderStatus(OrderStatus.CREATED).build();
        Item newItem = Item.builder().name("Item 1").price(new BigDecimal("10.00")).build();
        Item newItem2 = Item.builder().name("Item 2").price(new BigDecimal("20.00")).build();
        newOrder.getTotal_items().add(newItem);
        newOrder.getTotal_items().add(newItem2);
        Payment newPayment = Payment.builder().number("number 1").sum(new BigDecimal("10.00")).paymentDateTime(timeUtils.getCurrentTime()).build();
        newOrder.getTotal_payments().add(newPayment);

        Order savedOrder = orderRepository.save(newOrder);
        assertNotNull(orderRepository.findById(savedOrder.getId()));

        savedOrder.setOrderStatus(OrderStatus.DELIVERED);
        assertFalse(savedOrder.canDelete(savedOrder));
        assertThrows(IllegalStateException.class, () -> orderService.deleteOrder(savedOrder.getId()));
    }
}