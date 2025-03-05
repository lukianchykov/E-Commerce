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
    private final KafkaTemplate<String, OrderCreated> orderCreatedKafkaTemplate;

    @Autowired
    private final KafkaTemplate<String, OrderUpdated> orderUpdatedKafkaTemplate;

    @Autowired
    private final KafkaTemplate<String, OrderDeleted> orderDeletedKafkaTemplate;

    @Value("${spring.kafka.topic.order}")
    private String orderTopic;

    public KafkaOrderEventProducer(KafkaTemplate<String, OrderCreated> orderCreatedKafkaTemplate,
                                   KafkaTemplate<String, OrderUpdated> orderUpdatedKafkaTemplate,
                                   KafkaTemplate<String, OrderDeleted> orderDeletedKafkaTemplate) {
        this.orderCreatedKafkaTemplate = orderCreatedKafkaTemplate;
        this.orderUpdatedKafkaTemplate = orderUpdatedKafkaTemplate;
        this.orderDeletedKafkaTemplate = orderDeletedKafkaTemplate;
    }

    public void sendOrderEventCreated(Order order) {
        OrderCreated.newBuilder();
        OrderCreated orderCreated = OrderCreated.newBuilder()
            .setNumber(order.getNumber())
            .setOrderStatus(OrderStatus.valueOf(order.getOrderStatus().name()))
            .build();

        orderCreatedKafkaTemplate.send(orderTopic, orderCreated.getOrderId().toString(), orderCreated);
        log.info("Order Created event {} sent to topic: {}", orderCreated, orderTopic);
    }

    public void sendOrderEventUpdated(Order order) {
        OrderUpdated orderUpdated = OrderUpdated.newBuilder()
            .setNumber(order.getNumber())
            .setOrderStatus(OrderStatus.valueOf(order.getOrderStatus().name()))
            .build();

        orderUpdatedKafkaTemplate.send(orderTopic, orderUpdated.getOrderId().toString(), orderUpdated);
        log.info("Order Updated event {} sent to topic: {}", orderUpdated, orderTopic);
    }

    public void sendOrderEventDeleted(Order order) {
        OrderDeleted orderDeleted = OrderDeleted.newBuilder()
            .setNumber(order.getNumber())
            .setOrderStatus(OrderStatus.valueOf(order.getOrderStatus().name()))
            .build();

        orderDeletedKafkaTemplate.send(orderTopic, orderDeleted.getOrderId().toString(), orderDeleted);
        log.info("Order Deleted event {} sent to topic: {}", orderDeleted, orderTopic);
    }
}
