package com.gbsfo.ecommerce.repository;

import com.gbsfo.ecommerce.domain.PaymentEvent;

import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEvent, Long> {
}
