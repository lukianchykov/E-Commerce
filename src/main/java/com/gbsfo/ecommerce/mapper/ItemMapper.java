package com.gbsfo.ecommerce.mapper;

import java.util.List;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.dto.ItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ItemMapper {

    ItemDto toDto(Item item);

    Item toEntity(ItemDto itemDto);

    List<ItemDto> toDto(List<Item> items);

    List<Item> toEntity(List<ItemDto> itemDtos);
}

