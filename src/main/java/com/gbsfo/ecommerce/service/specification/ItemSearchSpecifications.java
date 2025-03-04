//package com.gbsfo.ecommerce.service.specification;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import javax.persistence.criteria.Join;
//import javax.persistence.criteria.JoinType;
//import javax.persistence.criteria.Predicate;
//
//import com.gbsfo.ecommerce.domain.Item;
//import com.gbsfo.ecommerce.domain.Item_;
//import com.gbsfo.ecommerce.domain.Order;
//import com.gbsfo.ecommerce.domain.Order.OrderStatus;
//import com.gbsfo.ecommerce.domain.Order_;
//import lombok.experimental.UtilityClass;
//
//import org.springframework.data.jpa.domain.Specification;
//
//@UtilityClass
//public class ItemSearchSpecifications {
//
//    public static Specification<Item> idEquals(Long id) {
//        return (root, query, criteriaBuilder) ->
//            Optional.ofNullable(id)
//                .map(val -> criteriaBuilder.equal(root.get(Item_.ID), val))
//                .orElse(criteriaBuilder.conjunction());
//    }
//
//    public static Specification<Item> nameEquals(String name) {
//        return (root, query, criteriaBuilder) ->
//            Optional.ofNullable(name)
//                .map(val -> criteriaBuilder.equal(root.get(Item_.NAME), val))
//                .orElse(criteriaBuilder.conjunction());
//    }
//
//    public static Specification<Item> priceEquals(BigDecimal price) {
//        return (root, query, criteriaBuilder) -> {
//            Predicate pricePredicate = Optional.ofNullable(price)
//                .map(val -> criteriaBuilder.equal(root.get(Item_.PRICE), val))
//                .orElse(criteriaBuilder.conjunction());
//
//            Join<Item, Order> orderJoin = root.join(Item_.order, JoinType.LEFT);
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