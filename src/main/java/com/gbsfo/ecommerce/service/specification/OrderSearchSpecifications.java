//package com.gbsfo.ecommerce.service.specification;
//
//import java.util.Optional;
//
//import javax.validation.ValidationException;
//
//import com.gbsfo.ecommerce.domain.Order;
//import com.gbsfo.ecommerce.domain.Order.OrderStatus;
//import com.gbsfo.ecommerce.domain.Order_;
//import lombok.experimental.UtilityClass;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.data.jpa.domain.Specification;
//
//@UtilityClass
//@Slf4j
//public class OrderSearchSpecifications {
//
//    public static Specification<Order> idEquals(Long id) {
//        return (root, query, criteriaBuilder) ->
//            Optional.ofNullable(id)
//                .map(val -> criteriaBuilder.equal(root.get(Order_.ID), val))
//                .orElse(criteriaBuilder.conjunction());
//    }
//
//    public static Specification<Order> numberEquals(String number) {
//        return (root, query, criteriaBuilder) ->
//            Optional.ofNullable(number)
//                .map(val -> criteriaBuilder.equal(root.get(Order_.NUMBER), val))
//                .orElse(criteriaBuilder.conjunction());
//    }
//
//    public static Specification<Order> orderStatusEquals(String orderStatus) {
//        return (root, query, criteriaBuilder) -> {
//            if (orderStatus == null) {
//                return criteriaBuilder.conjunction();
//            }
//            try {
//                return criteriaBuilder.equal(root.get(Order_.ORDER_STATUS), OrderStatus.valueOf(orderStatus));
//            } catch (IllegalArgumentException e) {
//                log.error("Invalid order status: " + orderStatus);
//                throw new ValidationException("Invalid order status: " + orderStatus);
//            }
//        };
//    }
//}