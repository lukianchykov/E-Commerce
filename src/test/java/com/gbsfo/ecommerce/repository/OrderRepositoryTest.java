package com.gbsfo.ecommerce.repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Order.OrderStatus;
import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.utils.time.TimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TimeUtils.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TimeUtils timeUtils;

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
    public void verify_FindAll() {
        var orders = orderRepository.findAll();
        assertFalse(orders.isEmpty());
    }

    @Test
    public void verify_OrderCreatedAndSaved() {
        assertNotNull(order.getId());
        assertEquals("ORD-001", order.getNumber());
        assertEquals(OrderStatus.CREATED.name(), order.getOrderStatus().name());
        assertEquals(2, order.getTotal_items().size());
        assertEquals(2, order.getTotal_payments().size());
    }

    @Test
    public void verify_OrderFoundByNumber() {
        order = orderRepository.findByNumber("ORD-001").orElse(null);
        assertNotNull(Objects.requireNonNull(order).getNumber());
    }

    @Test
    public void verify_OrderDeletedAndAllItemsArePaid() {
        assertTrue(order.canDelete(order));
        orderRepository.delete(order);
        assertFalse(orderRepository.findById(order.getId()).isPresent());
    }

    @Test
    public void verify_AllItemsInOrderArePaidAndOrderCanBeMovedToShippingStatus() {
        assertTrue(order.allItemsPaid());
        assertTrue(order.canUpdate(order));
        order.setOrderStatus(OrderStatus.SHIPPING);
    }

    @Test
    public void verify_AllItemsInOrderAreNotPaid() {
        Order order = Order.builder()
            .number("5678")
            .orderStatus(OrderStatus.CREATED)
            .total_items(List.of(
                Item.builder().name("Item 1").price(new BigDecimal("10.00")).build(),
                Item.builder().name("Item 2").price(new BigDecimal("20.00")).build()))
            .total_payments(Collections.singletonList(Payment.builder().number("number").sum(new BigDecimal("10.00")).build()))
            .build();

        orderRepository.save(order);
        assertFalse(order.allItemsPaid());
        order.setOrderStatus(OrderStatus.PROCESSING);
        assertFalse(order.canUpdate(order));
    }

    @Test
    public void verify_NewItemsCanNotBeAddedToOrderWithStatusShippingOrDelivered() {
        order.setOrderStatus(OrderStatus.SHIPPING);
        assertFalse(order.canAddItemsToOrder(order));
        order.setOrderStatus(OrderStatus.DELIVERED);
        assertFalse(order.canAddItemsToOrder(order));
    }
}
