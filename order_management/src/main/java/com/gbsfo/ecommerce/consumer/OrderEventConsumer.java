package com.gbsfo.ecommerce.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gbsfo.ecommerce.service.OrderEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventConsumer {

    @Autowired
    private OrderEventService orderEventService;

    @KafkaListener(topics = {"order-events"}, containerFactory = "kafkaListenerContainerFactory"
        , groupId = "order-events-listener-group")
    public void onMessage(ConsumerRecord<Integer, String> record) throws JsonProcessingException {
        log.info("Order event - {}", record);
        orderEventService.processOrderEvent(record);
    }
}
