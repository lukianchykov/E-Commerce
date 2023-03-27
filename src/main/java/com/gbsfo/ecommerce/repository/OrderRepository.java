package com.gbsfo.ecommerce.repository;

import java.util.Optional;

import com.gbsfo.ecommerce.domain.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, QueryByExampleExecutor<Order>, JpaSpecificationExecutor<Order> {

    Optional<Order> findFirstByNumberEquals(String number);

}