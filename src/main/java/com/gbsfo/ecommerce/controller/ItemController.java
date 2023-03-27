package com.gbsfo.ecommerce.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.ItemLookupPublicApiRequest;
import com.gbsfo.ecommerce.dto.IterableDataResponse;
import com.gbsfo.ecommerce.facade.ItemFacade;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.gbsfo.ecommerce.utils.Constants.API_LIMIT_REQUEST_PARAMETER;
import static com.gbsfo.ecommerce.utils.Constants.API_OFFSET_REQUEST_PARAMETER;
import static com.gbsfo.ecommerce.utils.Constants.API_VERSION_PREFIX_V1;

@Slf4j
@RestController
@RequestMapping(API_VERSION_PREFIX_V1 + "/items")
public class ItemController {

    @Autowired
    private ItemFacade itemFacade;

    @GetMapping
    public IterableDataResponse<List<ItemDto>> searchItems(
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "price", required = false) BigDecimal price,
        @RequestParam(value = API_OFFSET_REQUEST_PARAMETER, required = false, defaultValue = "0") int offset,
        @RequestParam(value = API_LIMIT_REQUEST_PARAMETER, required = false, defaultValue = "20") int limit
    ) {
        var itemLookupRequest = new ItemLookupPublicApiRequest(name, price, offset, limit);
        return itemFacade.find(itemLookupRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable(value = "id") Long itemId) {
        return ResponseEntity.ok(itemFacade.getItemById(itemId));
    }

    @GetMapping("/by-number/{name}")
    public ResponseEntity<ItemDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(itemFacade.findByName(name));
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemFacade.createItem(itemDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable(value = "id") Long itemId, @Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok().body(itemFacade.updateItem(itemId, itemDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable(value = "id") Long itemId) {
        itemFacade.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }
}
