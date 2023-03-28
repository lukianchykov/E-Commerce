package com.gbsfo.ecommerce.repository;

import java.math.BigDecimal;
import java.util.Objects;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Order.OrderStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    private Item item;

    @Before
    public void setUp() {
        item = Item.builder().name("Item 1").price(new BigDecimal("10.00"))
            .order(Order.builder().number("Number 1").orderStatus(OrderStatus.CREATED).build()).build();
        itemRepository.save(item);
    }

    @After
    public void cleanup() {
        itemRepository.delete(item);
    }

    @Test
    public void verify_FindAll() {
        var items = itemRepository.findAll();
        assertFalse(items.isEmpty());
    }

    @Test
    public void verify_itemCreatedAndSaved() {
        assertNotNull(item.getId());
        assertNotNull(item.getOrder());
        assertEquals("Item 1", item.getName());
        assertEquals(new BigDecimal("10.00"), item.getPrice());
    }

    @Test
    public void verify_itemFoundByName() {
        item = itemRepository.findByName("Item 1").orElse(null);
        assertNotNull(Objects.requireNonNull(item).getName());
    }

    @Test
    public void verify_itemDeletedAndAllItemsArePaid() {
        itemRepository.delete(item);
        assertFalse(itemRepository.findById(item.getId()).isPresent());
    }
}
