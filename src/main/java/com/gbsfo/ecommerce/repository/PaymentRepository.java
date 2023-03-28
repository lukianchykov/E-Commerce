package com.gbsfo.ecommerce.repository;

import java.util.Optional;

import com.gbsfo.ecommerce.domain.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, QueryByExampleExecutor<Payment>, JpaSpecificationExecutor<Payment> {

    Optional<Payment> findByNumber(String number);

}
