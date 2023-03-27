package com.gbsfo.ecommerce.service.specification;

import java.math.BigDecimal;
import java.util.Optional;

import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.domain.Payment_;
import lombok.experimental.UtilityClass;

import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class PaymentSearchSpecifications {

    public static Specification<Payment> idEquals(String id) {
        return (root, query, criteriaBuilder) ->
            Optional.ofNullable(id)
                .map(val -> criteriaBuilder.equal(root.get(Payment_.ID), val))
                .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Payment> numberEquals(String number) {
        return (root, query, criteriaBuilder) ->
            Optional.ofNullable(number)
                .map(val -> criteriaBuilder.equal(root.get(Payment_.NUMBER), val))
                .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Payment> sumEquals(BigDecimal sum) {
        return (root, query, criteriaBuilder) ->
            Optional.ofNullable(sum)
                .map(val -> criteriaBuilder.equal(root.get(Payment_.SUM), val))
                .orElse(criteriaBuilder.conjunction());
    }
}
