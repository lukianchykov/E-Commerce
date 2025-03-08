package com.gbsfo.ecommerce.repository;

import com.gbsfo.ecommerce.domain.ItemEvent;

import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<ItemEvent, Long> {
}