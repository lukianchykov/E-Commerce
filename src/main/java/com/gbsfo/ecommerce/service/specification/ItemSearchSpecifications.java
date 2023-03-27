package com.gbsfo.ecommerce.service.specification;

import java.math.BigDecimal;
import java.util.Optional;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.domain.Item_;
import lombok.experimental.UtilityClass;

import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ItemSearchSpecifications {

    public static Specification<Item> idEquals(String id) {
        return (root, query, criteriaBuilder) ->
            Optional.ofNullable(id)
                .map(val -> criteriaBuilder.equal(root.get(Item_.ID), val))
                .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Item> nameEquals(String name) {
        return (root, query, criteriaBuilder) ->
            Optional.ofNullable(name)
                .map(val -> criteriaBuilder.equal(root.get(Item_.NAME), val))
                .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Item> priceEquals(BigDecimal price) {
        return (root, query, criteriaBuilder) ->
            Optional.ofNullable(price)
                .map(val -> criteriaBuilder.equal(root.get(Item_.PRICE), val))
                .orElse(criteriaBuilder.conjunction());
    }
}