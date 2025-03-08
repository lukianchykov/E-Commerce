package com.gbsfo.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbsfo.ecommerce.domain.ItemEvent;
import com.gbsfo.ecommerce.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class ItemEventService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ItemRepository itemRepository;

    public void processItemEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        ItemEvent itemEvent = objectMapper.readValue(consumerRecord.value(), ItemEvent.class);
        log.info("Item Event : {} ", itemEvent);

        if (itemEvent.getId() != null && (itemEvent.getId() == 999)) {
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }

        itemRepository.save(itemEvent);
        log.info("Successfully Persisted the item Event {} ", itemEvent);
    }
}
