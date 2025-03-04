package com.gbsfo.ecommerce.facade;

import com.gbsfo.ecommerce.controller.exception.ResourceNotFoundException;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.mapper.ItemMapper;
import com.gbsfo.ecommerce.service.impl.ItemService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ItemFacade {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemMapper itemMapper;

//    public IterableDataResponse<List<ItemDto>> find(ItemLookupPublicApiRequest itemLookupRequest) {
//        var page = itemService.findItems(itemLookupRequest);
//
//        var items = page.getContent().stream()
//            .map(itemMapper::toDto)
//            .collect(Collectors.toList());
//        return new IterableDataResponse<>(items, page.hasNext());
//    }

    public ItemDto getItemById(Long itemId) {
        var item = itemService.getItemById(itemId);
        return itemService.getItemById(itemId) != null ? itemMapper.toDto(item) : null;
    }

    public ItemDto findByName(String name) {
        var itemByName = itemService.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found by name", name));
        return itemMapper.toDto(itemByName);
    }

    public ItemDto createItem(ItemDto itemDto) {
        var item = itemService.createItem(itemDto);
        return itemMapper.toDto(item);
    }

    public ItemDto updateItem(Long itemId, ItemDto ItemDto) {
        var item = itemService.updateItem(itemId, ItemDto);
        return itemMapper.toDto(item);
    }

    public void deleteItem(Long itemId) {
        itemService.deleteItem(itemId);
    }
}