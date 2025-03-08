package com.gbsfo.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbsfo.ecommerce.domain.OrderEvent;
import com.gbsfo.ecommerce.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class OrderEventService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    public void processOrderEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        OrderEvent orderEvent = objectMapper.readValue(consumerRecord.value(), OrderEvent.class);
        log.info("Order Event : {} ", orderEvent);


        if (orderEvent.getId() != null && (orderEvent.getId() == 999)) {
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }

        orderRepository.save(orderEvent);
        log.info("Successfully Persisted the order Event {} ", orderEvent);
    }
}
