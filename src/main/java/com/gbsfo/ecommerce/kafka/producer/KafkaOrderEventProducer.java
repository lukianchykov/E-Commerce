package com.gbsfo.ecommerce.kafka.producer;

import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.kafka.order.OrderCreated;
import com.gbsfo.kafka.order.OrderDeleted;
import com.gbsfo.kafka.order.OrderStatus;
import com.gbsfo.kafka.order.OrderUpdated;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaOrderEventProducer {

    @Autowired
    private final KafkaTemplate<Integer, String> orderCreatedKafkaTemplate;

    @Autowired
    private final KafkaTemplate<Integer, String> orderUpdatedKafkaTemplate;

    @Autowired
    private final KafkaTemplate<Integer, String> orderDeletedKafkaTemplate;

    @Value("${spring.kafka.topic.order}")
    private String orderTopic;

    public KafkaOrderEventProducer(KafkaTemplate<Integer, String> orderCreatedKafkaTemplate, KafkaTemplate<Integer, String> orderUpdatedKafkaTemplate,
                                   KafkaTemplate<Integer, String> orderDeletedKafkaTemplate) {
        this.orderCreatedKafkaTemplate = orderCreatedKafkaTemplate;
        this.orderUpdatedKafkaTemplate = orderUpdatedKafkaTemplate;
        this.orderDeletedKafkaTemplate = orderDeletedKafkaTemplate;
    }

    public void sendOrderEventCreated(Order order) {
        OrderCreated orderCreated = OrderCreated.newBuilder()
            .setOrderId(order.getId())
            .setNumber(order.getNumber())
            .setOrderStatus(OrderStatus.valueOf(order.getOrderStatus().name()))
            .build();

        orderCreatedKafkaTemplate.send(orderTopic, orderCreated.getOrderId().intValue(), orderCreated.toString());
        log.info("Order Created event {} sent to topic: {}", orderCreated, orderTopic);
    }

    public void sendOrderEventUpdated(Order order) {
        OrderUpdated orderUpdated = OrderUpdated.newBuilder()
            .setOrderId(order.getId())
            .setNumber(order.getNumber())
            .setOrderStatus(OrderStatus.valueOf(order.getOrderStatus().name()))
            .build();

        orderUpdatedKafkaTemplate.send(orderTopic, orderUpdated.getOrderId().intValue(), orderUpdated.toString());
        log.info("Order Updated event {} sent to topic: {}", orderUpdated, orderTopic);
    }

    public void sendOrderEventDeleted(Order order) {
        OrderDeleted orderDeleted = OrderDeleted.newBuilder()
            .setOrderId(order.getId())
            .setNumber(order.getNumber())
            .setOrderStatus(OrderStatus.valueOf(order.getOrderStatus().name()))
            .build();

        orderDeletedKafkaTemplate.send(orderTopic, orderDeleted.getOrderId().intValue(), orderDeleted.toString());
        log.info("Order Deleted event {} sent to topic: {}", orderDeleted, orderTopic);
    }
}
