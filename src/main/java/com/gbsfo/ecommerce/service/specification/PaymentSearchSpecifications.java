//package com.gbsfo.ecommerce.service.specification;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import javax.persistence.criteria.Join;
//import javax.persistence.criteria.JoinType;
//import javax.persistence.criteria.Predicate;
//
//import com.gbsfo.ecommerce.domain.Order;
//import com.gbsfo.ecommerce.domain.Order.OrderStatus;
//import com.gbsfo.ecommerce.domain.Order_;
//import com.gbsfo.ecommerce.domain.Payment;
//import com.gbsfo.ecommerce.domain.Payment_;
//import lombok.experimental.UtilityClass;
//
//import org.springframework.data.jpa.domain.Specification;
//
//@UtilityClass
//public class PaymentSearchSpecifications {
//
//    public static Specification<Payment> idEquals(Long id) {
//        return (root, query, criteriaBuilder) ->
//            Optional.ofNullable(id)
//                .map(val -> criteriaBuilder.equal(root.get(Payment_.ID), val))
//                .orElse(criteriaBuilder.conjunction());
//    }
//
//    public static Specification<Payment> numberEquals(String number) {
//        return (root, query, criteriaBuilder) ->
//            Optional.ofNullable(number)
//                .map(val -> criteriaBuilder.equal(root.get(Payment_.NUMBER), val))
//                .orElse(criteriaBuilder.conjunction());
//    }
//
//    public static Specification<Payment> sumEquals(BigDecimal sum) {
//        return (root, query, criteriaBuilder) -> {
//            Predicate pricePredicate = Optional.ofNullable(sum)
//                .map(val -> criteriaBuilder.equal(root.get(Payment_.SUM), val))
//                .orElse(criteriaBuilder.conjunction());
//
//            Join<Payment, Order> orderJoin = root.join(Payment_.order, JoinType.LEFT);
//            Predicate orderStatusPredicate = criteriaBuilder.or(
//                criteriaBuilder.isNull(orderJoin),
//                criteriaBuilder.notEqual(orderJoin.get(Order_.orderStatus), OrderStatus.SHIPPING),
//                criteriaBuilder.notEqual(orderJoin.get(Order_.orderStatus), OrderStatus.DELIVERED)
//            );
//
//            return criteriaBuilder.and(pricePredicate, orderStatusPredicate);
//        };
//    }
//}
