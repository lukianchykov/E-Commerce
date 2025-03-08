package com.gbsfo.ecommerce.repository;

import com.gbsfo.ecommerce.domain.OrderEvent;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEvent, Long> {
}
