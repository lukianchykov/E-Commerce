package com.gbsfo.ecommerce.service.specification;

import java.util.Optional;

import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Order_;
import lombok.experimental.UtilityClass;

import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class OrderSearchSpecifications {

    public static Specification<Order> idEquals(String id) {
        return (root, query, criteriaBuilder) ->
            Optional.ofNullable(id)
                .map(val -> criteriaBuilder.equal(root.get(Order_.ID), val))
                .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Order> numberEquals(String number) {
        return (root, query, criteriaBuilder) ->
            Optional.ofNullable(number)
                .map(val -> criteriaBuilder.equal(root.get(Order_.NUMBER), val))
                .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Order> orderStatusEquals(String orderStatus) {
        return (root, query, criteriaBuilder) ->
            Optional.ofNullable(orderStatus)
                .map(val -> criteriaBuilder.equal(root.get(Order_.ORDER_STATUS), val))
                .orElse(criteriaBuilder.conjunction());
    }
}