package com.gbsfo.ecommerce.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import com.gbsfo.ecommerce.controller.exception.ResourceAlreadyExistException;
import com.gbsfo.ecommerce.controller.exception.ResourceNotFoundException;
import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.kafka.producer.KafkaItemEventProducer;
import com.gbsfo.ecommerce.mapper.ItemMapper;
import com.gbsfo.ecommerce.repository.ItemRepository;
import com.gbsfo.ecommerce.service.IItemService;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.util.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.gbsfo.ecommerce.utils.LogStructuredArguments.itemId;

@Service
@Transactional
@Slf4j
public class ItemService implements IItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private KafkaItemEventProducer kafkaItemEventProducer;

//    /**
//     * Public API find items request
//     * Example:
//     * total: 100
//     * offset: 50, limit: 10
//     * pages to skip = 5
//     * 50 / 10 = offset / limit = 5
//     */
//    @Override
//    public Page<Item> findItems(ItemLookupPublicApiRequest request) {
//        Specification<Item> searchSpecification = Specification
//            .where(idEquals(request.getId()))
//            .and(nameEquals(request.getName()))
//            .and(priceEquals(request.getPrice()));
//
//        return itemRepository.findAll(searchSpecification, PageRequest.of(request.getOffset() / request.getLimit(), request.getLimit()));
//    }

    @Override
    public Item getItemById(Long itemId) {
        log.info("Get Item attempt for {}", itemId(itemId));
        return itemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: ", itemId));
    }

    @Override
    public Optional<Item> findByName(String name) {
        if (StringUtils.isBlank(name)) {
            log.error("Name is blank: {}", name);
            throw new IllegalStateException("Name is blank: " + name);
        }
        return itemRepository.findByName(name);
    }

    @Override
    public Item createItem(ItemDto itemDto) {
        var item = itemMapper.toEntity(itemDto);
        if (findByName(item.getName()).isPresent()) {
            log.error("Item already exists. Can’t create new {} with same name: {}", itemId(item.getId()), item.getName());
            throw new ResourceAlreadyExistException("Item already exists. Can’t create new Item with same name: " + item.getName());
        }

        log.info("Saving item in database {}", item);
        kafkaItemEventProducer.sendItemEventCreated(item);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Long itemId, ItemDto itemRequest) {
        var itemInDatabase = itemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: ", itemId));
        var itemFromRequest = itemMapper.toEntity(itemRequest);

        log.info("Updating item in database, source = {}, target = {}", itemFromRequest, itemInDatabase);
        BeanUtils.copyProperties(itemFromRequest, itemInDatabase, "id");

        kafkaItemEventProducer.sendItemEventUpdated(itemInDatabase);
        return itemRepository.save(itemInDatabase);
    }

    @Override
    public void deleteItem(Long itemId) {
        log.info("Item deleted attempt for {}", itemId(itemId));
        var item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: ", itemId));
        log.info("Item deleted from  database {}", item);
        kafkaItemEventProducer.sendItemEventDeleted(item);
        itemRepository.deleteById(item.getId());
    }
}
