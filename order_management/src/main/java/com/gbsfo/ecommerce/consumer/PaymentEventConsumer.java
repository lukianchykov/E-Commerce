package com.gbsfo.ecommerce.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gbsfo.ecommerce.service.PaymentEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentEventConsumer {

    @Autowired
    private PaymentEventService paymentEventService;

    @KafkaListener(topics = {"payment-events"}, containerFactory = "kafkaListenerContainerFactory"
        , groupId = "payment-events-listener-group")
    public void onMessage(ConsumerRecord<Integer, String> record) throws JsonProcessingException {
        log.info("Payment event - {}", record);
        paymentEventService.processPaymentEvent(record);
    }
}
