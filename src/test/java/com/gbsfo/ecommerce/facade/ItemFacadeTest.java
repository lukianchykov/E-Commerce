package com.gbsfo.ecommerce.facade;

import java.util.List;
import java.util.Optional;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.ItemLookupPublicApiRequest;
import com.gbsfo.ecommerce.dto.IterableDataResponse;
import com.gbsfo.ecommerce.mapper.ItemMapper;
import com.gbsfo.ecommerce.service.impl.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ItemFacade.class)
@RunWith(SpringRunner.class)
public class ItemFacadeTest {

    private static final Long ITEM_ID = 1L;

    private static final String NAME = "name";

    @Autowired
    private ItemFacade itemFacade;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemMapper itemMapper;

    @MockBean
    private ItemDto mockItemDto;

    @MockBean
    private Item mockItem;

//    @Test
//    public void testFind() {
//        var request = new ItemLookupPublicApiRequest();
//        var Items = List.of(new Item());
//        var page = new PageImpl<>(Items);
//
//        when(itemService.findItems(request)).thenReturn(page);
//        when(itemMapper.toDto(mockItem)).thenReturn(mockItemDto);
//
//        IterableDataResponse<List<ItemDto>> response = itemFacade.find(request);
//
//        assertNotNull(response);
//        assertEquals(1, response.getData().size());
//        assertFalse(response.isHasNext());
//    }

    @Test
    public void testGetItemById() {
        when(itemService.getItemById(anyLong())).thenReturn(mockItem);
        when(itemMapper.toDto(mockItem)).thenReturn(mockItemDto);

        ItemDto result = itemFacade.getItemById(ITEM_ID);

        assertNotNull(result);
        verify(itemMapper).toDto(mockItem);
    }

    @Test
    public void testFindByName() {
        when(itemService.findByName(NAME)).thenReturn(Optional.of(mockItem));
        when(itemMapper.toDto(mockItem)).thenReturn(mockItemDto);

        ItemDto result = itemFacade.findByName(NAME);

        assertNotNull(result);
        verify(itemMapper).toDto(mockItem);
    }

    @Test
    public void testCreateItem() {
        when(itemService.createItem(any(ItemDto.class))).thenReturn(mockItem);
        when(itemMapper.toDto(mockItem)).thenReturn(mockItemDto);
        ItemDto result = itemFacade.createItem(new ItemDto());

        assertNotNull(result);
        verify(itemMapper).toDto(mockItem);
    }

    @Test
    public void testUpdateItem() {
        when(itemService.updateItem(anyLong(), any(ItemDto.class))).thenReturn(mockItem);
        when(itemMapper.toDto(mockItem)).thenReturn(mockItemDto);
        ItemDto result = itemFacade.updateItem(ITEM_ID, new ItemDto());

        assertNotNull(result);
        verify(itemMapper).toDto(mockItem);
    }

    @Test
    public void testDeleteItem() {
        itemFacade.deleteItem(ITEM_ID);

        verify(itemService, times(1)).deleteItem(ITEM_ID);
    }

}
