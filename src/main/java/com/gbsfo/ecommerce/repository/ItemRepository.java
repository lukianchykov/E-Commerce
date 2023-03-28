package com.gbsfo.ecommerce.repository;

import java.util.Optional;

import com.gbsfo.ecommerce.domain.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, QueryByExampleExecutor<Item>, JpaSpecificationExecutor<Item> {

    Optional<Item> findByName(String name);

}