package com.gbsfo.ecommerce.mapper;

import java.math.BigDecimal;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.dto.ItemDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@Import(ItemMapperImpl.class)
public class ItemMapperTest {

    @Autowired
    private ItemMapper itemMapper;

    private Item item;

    private ItemDto itemDto;

    @Before
    public void setUp() {
        item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(100.0));
        item.setName("Item 1");
        item.setOrder(new Order());

        itemDto = ItemDto.builder()
            .id(1L)
            .name("Item 1")
            .price(BigDecimal.valueOf(100.0))
            .build();
    }

    @Test
    public void toEntity_VerifyAllRequiredDataSet() {
        Item item = itemMapper.toEntity(itemDto);

        assertNotNull(item);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getPrice(), itemDto.getPrice());
    }

    @Test
    public void toDto_VerifyAllRequiredDataSet() {
        ItemDto itemDto = itemMapper.toDto(item);

        assertNotNull(itemDto);
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getPrice(), item.getPrice());
    }
}
