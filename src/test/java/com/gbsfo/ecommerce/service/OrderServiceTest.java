package com.gbsfo.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Order.OrderStatus;
import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.OrderUpsertRequest;
import com.gbsfo.ecommerce.mapper.ItemMapper;
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

    @MockBean
    private ItemMapper itemMapper;

    private Order order;

    @Before
    public void setUp() {

        Item item1 = Item.builder().name("Item 1").price(new BigDecimal("10.00")).order(
            Order.builder().number("Number 1").orderStatus(OrderStatus.CREATED).build()).build();
        Item item2 = Item.builder().name("Item 2").price(new BigDecimal("20.00")).order(
            Order.builder().number("Number 1").orderStatus(OrderStatus.CREATED).build()).build();

        Payment payment1 = Payment.builder().number("number 1").sum(new BigDecimal("10.00")).paymentDateTime(timeUtils.getCurrentTime()).order(
            Order.builder().number("Number 1").orderStatus(OrderStatus.CREATED).build()).build();
        Payment payment2 = Payment.builder().number("number 2").sum(new BigDecimal("20.00")).paymentDateTime(timeUtils.getCurrentTime()).order(
            Order.builder().number("Number 1").orderStatus(OrderStatus.CREATED).build()).build();

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

//    @Test
//    public void findOrders_withFullSearch() {
//        OrderLookupPublicApiRequest orderLookupPublicApiRequest = OrderLookupPublicApiRequest.builder()
//            .number("ORD-001")
//            .orderStatus("CREATED")
//            .offset(0)
//            .limit(20)
//            .build();
//
//        Page<Order> actual = orderService.findOrders(orderLookupPublicApiRequest);
//
//        assertEquals(order, actual.getContent().get(0));
//    }
//
//    @Test
//    public void findOrders_withPartialSearch() {
//        OrderLookupPublicApiRequest orderLookupPublicApiRequest = OrderLookupPublicApiRequest.builder()
//            .number("ORD-001")
//            .offset(0)
//            .limit(20)
//            .build();
//
//        Page<Order> actual = orderService.findOrders(orderLookupPublicApiRequest);
//
//        assertEquals(order, actual.getContent().get(0));
//    }
//
//    @Test
//    public void findOrders_withPartialSearchWhenThereIsWrongField_shouldNotFindEntityAndTrowAnError() {
//        OrderLookupPublicApiRequest orderLookupPublicApiRequest = OrderLookupPublicApiRequest.builder()
//            .id(2L)
//            .number("INVALID")
//            .orderStatus("INVALID")
//            .offset(0)
//            .limit(20)
//            .build();
//
//        assertThrows(ValidationException.class, () -> orderService.findOrders(orderLookupPublicApiRequest));
//    }

    @Test
    public void verify_findOrderByNumber() {
        Optional<Order> actual = orderService.findByNumber(order.getNumber());
        assertEquals(order, actual.get());
        assertEquals(order.getNumber(), actual.get().getNumber());
    }

    @Test
    public void createOrder() {
        var order = Order.builder().number("ORD-002").orderStatus(OrderStatus.CREATED).build();
        when(orderMapper.toEntity(any(OrderUpsertRequest.class))).thenReturn(order);
        var orderUpsertRequest = OrderUpsertRequest.builder().number("number").build();

        Order createdOrder = orderService.createOrder(orderUpsertRequest);

        assertNotNull(orderRepository.findById(createdOrder.getId()));
    }

    @Test
    public void createOrder_whenNoItemsAndPayments_verifyItemsAndPaymentsSavedSuccessfully() {
        Order newOrder = Order.builder().number("ORD-003").orderStatus(OrderStatus.CREATED).build();
        Item newItem = Item.builder().name("It 1").price(new BigDecimal("10.00")).order(newOrder).build();
        newOrder.getTotal_items().add(newItem);
        Payment newPayment = Payment.builder().number("Num 1").sum(new BigDecimal("10.00")).paymentDateTime(timeUtils.getCurrentTime()).order(newOrder).build();
        newOrder.getTotal_payments().add(newPayment);

        when(orderMapper.toEntity(any(OrderUpsertRequest.class))).thenReturn(newOrder);
        var orderUpsertRequest = OrderUpsertRequest.builder().number("number").build();

        Order createdOrder = orderService.createOrder(orderUpsertRequest);

        assertNotNull(orderRepository.findById(createdOrder.getId()));
        assertFalse(createdOrder.getTotal_items().isEmpty());
        assertFalse(createdOrder.getTotal_payments().isEmpty());
    }

    @Test
    public void updateOrder_shouldUpdateOrderInDatabase() {
        var orderUpsertRequest = OrderUpsertRequest.builder().number("number").build();
        Order orderInDatabase = Order.builder().id(3L).number("Numb1").orderStatus(OrderStatus.CREATED).build();

        when(orderMapper.toEntity(any(OrderUpsertRequest.class))).thenReturn(Order.builder().number(orderUpsertRequest.getNumber()).build());

        Order savedOrder = orderRepository.save(orderInDatabase);
        assertNotNull(orderRepository.findById(savedOrder.getId()));

        assertNotEquals(orderUpsertRequest.getNumber(), savedOrder.getNumber());

        savedOrder.canUpdate(savedOrder);
        Order updatedOrder = orderService.updateOrder(savedOrder.getId(), orderUpsertRequest);
        assertEquals(orderUpsertRequest.getNumber(), updatedOrder.getNumber());
    }

    @Test
    public void updateOrder_whenRequestHaveNewItemsAndPayments_verifyItemsAndPaymentsUpdatedSuccessfully() {
        Order newOrder = Order.builder().id(3L).number("Numb1").orderStatus(OrderStatus.CREATED).build();
        Item newItem = Item.builder().name("Item 1").price(new BigDecimal("10.00")).build();
        newOrder.getTotal_items().add(newItem);
        Payment newPayment = Payment.builder().number("number 1").sum(new BigDecimal("10.00")).paymentDateTime(timeUtils.getCurrentTime()).build();
        newOrder.getTotal_payments().add(newPayment);

        when(orderMapper.toEntity(any(OrderUpsertRequest.class))).thenReturn(newOrder);
        var orderUpsertRequest = OrderUpsertRequest.builder().number("number").build();

        Order savedOrder = orderRepository.save(newOrder);
        assertNotNull(orderRepository.findById(savedOrder.getId()));

        savedOrder.canUpdate(savedOrder);
        Order updateOrder = orderService.updateOrder(savedOrder.getId(), orderUpsertRequest);

        assertNotNull(orderRepository.findById(updateOrder.getId()));
        // verify new items and payments added
        assertTrue(updateOrder.getTotal_items().stream()
            .anyMatch(item -> item.getName().equals(newItem.getName())));
        assertTrue(updateOrder.getTotal_payments().stream()
            .anyMatch(payment -> payment.getNumber().equals(newPayment.getNumber())));
    }

    @Test
    public void verify_CanAddItemsInOrderWithShippingStatus() {
        Order newOrder = Order.builder().id(3L).number("Numb1").orderStatus(OrderStatus.CREATED).build();
        Order savedOrder = orderRepository.save(newOrder);
        assertNotNull(orderRepository.findById(savedOrder.getId()));

        ItemDto item1 = ItemDto.builder().name("Item 1").price(new BigDecimal("10.00")).build();
        ItemDto item2 = ItemDto.builder().name("Item 2").price(new BigDecimal("20.00")).build();
        assertTrue(savedOrder.canAddItemsToOrder(savedOrder));
        assertNotNull(orderService.addItemsInOrder(savedOrder.getId(), List.of(item1, item2)));
    }

    @Test
    public void verify_CanNotAddItemsInOrderWithShippingStatus() {
        ItemDto item1 = ItemDto.builder().name("Item 1").price(new BigDecimal("10.00")).build();
        ItemDto item2 = ItemDto.builder().name("Item 2").price(new BigDecimal("20.00")).build();
        order.setOrderStatus(OrderStatus.SHIPPING);
        assertFalse(order.canAddItemsToOrder(order));
        assertThrows(IllegalStateException.class, () -> orderService.addItemsInOrder(order.getId(), List.of(item1, item2)));
    }

    @Test
    public void verify_CanNotAddItemsInOrderWithDeliveredStatus() {
        ItemDto item1 = ItemDto.builder().name("Item 1").price(new BigDecimal("10.00")).build();
        ItemDto item2 = ItemDto.builder().name("Item 2").price(new BigDecimal("20.00")).build();
        order.setOrderStatus(OrderStatus.DELIVERED);
        assertFalse(order.canAddItemsToOrder(order));
        assertThrows(IllegalStateException.class, () -> orderService.addItemsInOrder(order.getId(), List.of(item1, item2)));
    }

    @Test
    public void deleteOrder() {
        assertTrue(order.canDelete(order));
        orderService.deleteOrder(order.getId());
        assertEquals(Optional.empty(), orderRepository.findById(order.getId()));
    }

    @Test
    public void verify_CanNotDeleteOrderWithShippingStatus() {
        order.setOrderStatus(OrderStatus.SHIPPING);
        assertFalse(order.canDelete(order));
        assertThrows(IllegalStateException.class, () -> orderService.deleteOrder(order.getId()));
    }

    @Test
    public void verify_CanNotDeleteOrderWithDeliveredStatusUntilAllItemsArePaid() {
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
