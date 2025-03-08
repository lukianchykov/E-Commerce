package com.gbsfo.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbsfo.ecommerce.domain.PaymentEvent;
import com.gbsfo.ecommerce.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class PaymentEventService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private PaymentRepository paymentRepository;

    public void processPaymentEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        PaymentEvent paymentEvent = objectMapper.readValue(consumerRecord.value(), PaymentEvent.class);
        log.info("Payment Event : {} ", paymentEvent);

        if (paymentEvent.getId() != null && (paymentEvent.getId() == 999)) {
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }

        paymentRepository.save(paymentEvent);
        log.info("Successfully Persisted the payment Event {} ", paymentEvent);
    }
}