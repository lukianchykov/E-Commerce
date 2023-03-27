package com.gbsfo.ecommerce.service;

import java.util.Optional;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.ItemLookupPublicApiRequest;

import org.springframework.data.domain.Page;

public interface IItemService {

    Item getItemById(Long itemId);

    Page<Item> findItems(ItemLookupPublicApiRequest request);

    Optional<Item> findByName(String name);

    Item createItem(Item item);

    Item updateItem(Long itemId, ItemDto itemDto);

    void deleteItem(Long itemId);
}
