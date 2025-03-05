package com.gbsfo.ecommerce.kafka.producer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.kafka.item.ItemCreated;
import com.gbsfo.kafka.item.ItemDeleted;
import com.gbsfo.kafka.item.ItemUpdated;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaItemEventProducer {

    @Autowired
    private final KafkaTemplate<String, ItemCreated> itemCreatedKafkaTemplate;

    @Autowired
    private final KafkaTemplate<String, ItemUpdated> itemUpdatedKafkaTemplate;

    @Autowired
    private final KafkaTemplate<String, ItemDeleted> itemDeletedKafkaTemplate;

    @Value("${spring.kafka.topic.item}")
    private String itemTopic;

    public KafkaItemEventProducer(KafkaTemplate<String, ItemCreated> itemCreatedKafkaTemplate,
                                  KafkaTemplate<String, ItemUpdated> itemUpdatedKafkaTemplate,
                                  KafkaTemplate<String, ItemDeleted> itemDeletedKafkaTemplate) {
        this.itemCreatedKafkaTemplate = itemCreatedKafkaTemplate;
        this.itemUpdatedKafkaTemplate = itemUpdatedKafkaTemplate;
        this.itemDeletedKafkaTemplate = itemDeletedKafkaTemplate;
    }

    public void sendItemEventCreated(Item item) {
        ItemCreated itemCreated = ItemCreated.newBuilder()
            .setName(item.getName())
            .setPrice(ByteBuffer.wrap(item.getPrice().toPlainString().getBytes(StandardCharsets.UTF_8)))
            .build();

        itemCreatedKafkaTemplate.send(itemTopic, itemCreated.getItemId().toString(), itemCreated);
        log.info("Item Created event {} sent to topic: {}", itemCreated, itemTopic);
    }

    public void sendItemEventUpdated(Item item) {
        ItemUpdated itemUpdated = ItemUpdated.newBuilder()
            .setName(item.getName())
            .setPrice(ByteBuffer.wrap(item.getPrice().toPlainString().getBytes(StandardCharsets.UTF_8)))
            .build();

        itemUpdatedKafkaTemplate.send(itemTopic, itemUpdated.getItemId().toString(), itemUpdated);
        log.info("Item Updated event {} sent to topic: {}", itemUpdated, itemTopic);
    }

    public void sendItemEventDeleted(Item item) {
        ItemDeleted itemDeleted = ItemDeleted.newBuilder()
            .setName(item.getName())
            .setPrice(ByteBuffer.wrap(item.getPrice().toPlainString().getBytes(StandardCharsets.UTF_8)))
            .build();

        itemDeletedKafkaTemplate.send(itemTopic, itemDeleted.getItemId().toString(), itemDeleted);
        log.info("Item Deleted event {} sent to topic: {}", itemDeleted, itemTopic);
    }
}
