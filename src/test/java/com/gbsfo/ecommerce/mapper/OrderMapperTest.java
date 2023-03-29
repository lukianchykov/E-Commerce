package com.gbsfo.ecommerce.mapper;

import java.math.BigDecimal;
import java.util.Collections;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Order.OrderStatus;
import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.OrderDto;
import com.gbsfo.ecommerce.dto.PaymentDto;
import com.gbsfo.ecommerce.utils.time.TimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@Import({TimeUtils.class, OrderMapperImpl.class, ItemMapperImpl.class, PaymentMapperImpl.class})
public class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private TimeUtils timeUtils;

    private Order order;

    private OrderDto orderDto;

    @Before
    public void setUp() {
        Item item = Item.builder().id(1L).name("Item 1").price(BigDecimal.valueOf(100.0)).order(new Order()).build();
        Payment payment = Payment.builder().id(1L).number("PAYMENT-12345").sum(BigDecimal.valueOf(100.0)).paymentDateTime(timeUtils.getCurrentTime())
            .order(new Order()).build();
        order = Order.builder().id(1L).number("12345").orderStatus(OrderStatus.CREATED).total_items(Collections.singletonList(item))
            .total_payments(Collections.singletonList(payment)).build();

        ItemDto itemDto = ItemDto.builder().id(1L).name("Item 1").price(BigDecimal.valueOf(100.0)).build();
        PaymentDto paymentDto = PaymentDto.builder().id(1L).number("PAYMENT-12345").sum(BigDecimal.valueOf(100.0))
            .paymentDateTime(timeUtils.getCurrentTime()).build();
        orderDto = OrderDto.builder().id(1L).number("12345").orderStatus(com.gbsfo.ecommerce.dto.OrderDto.OrderStatus.CREATED)
            .items(Collections.singletonList(itemDto)).payments(Collections.singletonList(paymentDto)).build();
    }

    @Test
    public void toEntity_VerifyAllRequiredDataSet() {
        Order order = orderMapper.toEntity(orderDto);

        assertNotNull(order);
        assertEquals(order.getId(), orderDto.getId());
        assertEquals(order.getNumber(), orderDto.getNumber());
        assertEquals(order.getOrderStatus().name(), orderDto.getOrderStatus().name());

        Item item = order.getTotal_items().get(0);
        assertNotNull(item);
        assertEquals(item.getId(), order.getTotal_items().get(0).getId());
        assertEquals(item.getName(), order.getTotal_items().get(0).getName());
        assertEquals(item.getPrice(), order.getTotal_items().get(0).getPrice());

        Payment payment = order.getTotal_payments().get(0);
        assertNotNull(item);
        assertEquals(payment.getId(), order.getTotal_payments().get(0).getId());
        assertEquals(payment.getNumber(), order.getTotal_payments().get(0).getNumber());
        assertEquals(payment.getSum(), order.getTotal_payments().get(0).getSum());
        assertEquals(payment.getPaymentDateTime(), order.getTotal_payments().get(0).getPaymentDateTime());
    }

    @Test
    public void toDto_VerifyAllRequiredDataSet() {
        OrderDto orderDto = orderMapper.toDto(order);

        assertNotNull(orderDto);
        assertEquals(orderDto.getId(), order.getId());
        assertEquals(orderDto.getNumber(), order.getNumber());
        assertEquals(orderDto.getOrderStatus().name(), order.getOrderStatus().name());

        ItemDto itemDto = orderDto.getItems().get(0);
        assertNotNull(itemDto);
        assertEquals(itemDto.getId(), orderDto.getItems().get(0).getId());
        assertEquals(itemDto.getName(), orderDto.getItems().get(0).getName());
        assertEquals(itemDto.getPrice(), orderDto.getItems().get(0).getPrice());

        PaymentDto paymentDto = orderDto.getPayments().get(0);
        assertNotNull(paymentDto);
        assertEquals(paymentDto.getId(), orderDto.getPayments().get(0).getId());
        assertEquals(paymentDto.getNumber(), orderDto.getPayments().get(0).getNumber());
        assertEquals(paymentDto.getSum(), orderDto.getPayments().get(0).getSum());
    }
}

