package com.gbsfo.ecommerce.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gbsfo.ecommerce.service.ItemEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ItemEventConsumer {

    @Autowired
    private ItemEventService itemEventService;

    @KafkaListener(topics = {"item-events"}, containerFactory = "kafkaListenerContainerFactory"
        , groupId = "item-events-listener-group")
    public void onMessage(ConsumerRecord<Integer, String> record) throws JsonProcessingException {
        log.info("Item event - {}", record);
        itemEventService.processItemEvent(record);
    }
}