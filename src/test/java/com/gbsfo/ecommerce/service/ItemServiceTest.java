package com.gbsfo.ecommerce.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Order.OrderStatus;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.ItemLookupPublicApiRequest;
import com.gbsfo.ecommerce.mapper.ItemMapper;
import com.gbsfo.ecommerce.repository.ItemRepository;
import com.gbsfo.ecommerce.service.impl.ItemService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import(ItemService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemServiceTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemMapper itemMapper;

    private Item item;

    @Before
    public void setUp() {
        item = Item.builder().name("Item 1").price(new BigDecimal("123.45"))
            .order(Order.builder().number("Number 1").orderStatus(OrderStatus.CREATED).build()).build();
        itemRepository.save(item);
    }

    @After
    public void cleanup() {
        itemRepository.delete(item);
    }

    @Test
    public void verify_findItemById() {
        Item actual = itemService.getItemById(item.getId());
        assertEquals(item, actual);
    }

    @Test
    public void findItems_withFullSearch() {
        ItemLookupPublicApiRequest itemLookupPublicApiRequest = ItemLookupPublicApiRequest.builder()
            .name("Item 1")
            .price(BigDecimal.valueOf(123.45))
            .offset(0)
            .limit(20)
            .build();

        Page<Item> actual = itemService.findItems(itemLookupPublicApiRequest);

        assertEquals(item, actual.getContent().get(0));
    }

    @Test
    public void findItems_withPartialSearch() {
        ItemLookupPublicApiRequest itemLookupPublicApiRequest = ItemLookupPublicApiRequest.builder()
            .name("Item 1")
            .offset(0)
            .limit(20)
            .build();

        Page<Item> actual = itemService.findItems(itemLookupPublicApiRequest);

        assertEquals(item, actual.getContent().get(0));
    }

    @Test
    public void findItems_withPartialSearchWhenThereIsWrongField_shouldNotFindEntityAndTrowAnError() {
        ItemLookupPublicApiRequest itemLookupPublicApiRequest = ItemLookupPublicApiRequest.builder()
            .name("INVALID")
            .price(BigDecimal.valueOf(0.00))
            .offset(0)
            .limit(20)
            .build();

        Page<Item> actual = itemService.findItems(itemLookupPublicApiRequest);

        assertTrue(actual.getContent().isEmpty());
    }

    @Test
    public void verify_findItemByName() {
        Optional<Item> actual = itemService.findByName(item.getName());
        assertEquals(item, actual.get());
        assertEquals(item.getName(), actual.get().getName());
    }

    @Test
    public void createItem() {
        Item newItem = Item.builder().name("nameTest").price(new BigDecimal("10.00")).build();
        when(itemMapper.toEntity(any(ItemDto.class))).thenReturn(newItem);
        ItemDto itemDto = ItemDto.builder().name("name").price(new BigDecimal("10.00")).build();

        Item createdItem = itemService.createItem(itemDto);

        assertNotNull(itemRepository.findById(createdItem.getId()));
        assertEquals(newItem, createdItem);
    }

    @Test
    public void updateItem_shouldUpdateItemInDatabase() {
        ItemDto itemDto = ItemDto.builder().id(4L).name("nameTest").price(new BigDecimal("10.00")).build();
        Item itemInDatabase = Item.builder().id(3L).name("name").price(new BigDecimal("10.00")).build();

        when(itemMapper.toEntity(itemDto)).thenReturn(Item.builder().name(itemDto.getName()).build());

        Item savedItem = itemRepository.save(itemInDatabase);
        assertNotNull(itemRepository.findById(savedItem.getId()));

        assertNotEquals(itemDto.getName(), savedItem.getName());

        Item updatedItem = itemService.updateItem(savedItem.getId(), itemDto);
        assertEquals(itemDto.getName(), updatedItem.getName());
    }

    @Test
    public void deleteItem() {
        itemService.deleteItem(item.getId());
        assertEquals(Optional.empty(), itemRepository.findById(item.getId()));
    }
}
