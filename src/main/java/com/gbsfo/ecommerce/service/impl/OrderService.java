package com.gbsfo.ecommerce.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.gbsfo.ecommerce.controller.exception.ResourceAlreadyExistException;
import com.gbsfo.ecommerce.controller.exception.ResourceNotFoundException;
import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.OrderLookupPublicApiRequest;
import com.gbsfo.ecommerce.dto.OrderUpsertRequest;
import com.gbsfo.ecommerce.mapper.ItemMapper;
import com.gbsfo.ecommerce.mapper.OrderMapper;
import com.gbsfo.ecommerce.repository.OrderRepository;
import com.gbsfo.ecommerce.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.util.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.gbsfo.ecommerce.service.specification.OrderSearchSpecifications.idEquals;
import static com.gbsfo.ecommerce.service.specification.OrderSearchSpecifications.numberEquals;
import static com.gbsfo.ecommerce.service.specification.OrderSearchSpecifications.orderStatusEquals;
import static com.gbsfo.ecommerce.utils.LogStructuredArguments.orderId;

@Service
@Transactional
@Slf4j
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ItemMapper itemMapper;

    /**
     * Public API find orders request
     * Example:
     * total: 100
     * offset: 50, limit: 10
     * pages to skip = 5
     * 50 / 10 = offset / limit = 5
     */
    @Override
    public Page<Order> findOrders(OrderLookupPublicApiRequest request) {
        Specification<Order> searchSpecification = Specification
            .where(idEquals(request.getId()))
            .and(numberEquals(request.getNumber()))
            .and(orderStatusEquals(request.getOrderStatus()));

        return orderRepository.findAll(searchSpecification, PageRequest.of(request.getOffset() / request.getLimit(), request.getLimit()));
    }

    @Override
    public Order getOrderById(Long orderId) {
        log.info("Get Order attempt for {}", orderId(orderId));
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: ", orderId));
    }

    @Override
    public Optional<Order> findByNumber(String number) {
        if (StringUtils.isBlank(number)) {
            log.error("Number is blank: {}", number);
            throw new IllegalStateException("Number is blank: " + number);
        }
        return orderRepository.findByNumber(number);
    }

    @Override
    public Order createOrder(OrderUpsertRequest orderUpsertRequest) {
        var order = orderMapper.toEntity(orderUpsertRequest);
        if (findByNumber(order.getNumber()).isPresent()) {
            log.error("Order already exists. Can’t create new {} with same number: {}", orderId(order.getId()), order.getNumber());
            throw new ResourceAlreadyExistException("Order already exists. Can’t create new Order with same number: " + order.getNumber());
        }
        log.info("Saving order in database {}", order);

        order.getTotal_items().forEach(item -> item.setOrder(order));
        order.getTotal_payments().forEach(payment -> payment.setOrder(order));

        return orderRepository.save(order);
    }

    @Override
    public Order addItemsInOrder(Long orderId, List<ItemDto> itemsFromRequest) {
        var order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: ", orderId));
        var items = itemMapper.toEntity(itemsFromRequest);

        if (!order.canAddItemsToOrder(order)) {
            log.error("Items can`t be added to {} ", orderId(orderId));
            throw new IllegalStateException("Items can`t be added to: " + orderId(orderId));
        }

        log.info("Saving order in database {}", order);

        order.getTotal_items().addAll(items);

        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long orderId, OrderUpsertRequest orderUpsertRequest) {
        var orderInDatabase = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: ", orderId));
        var orderFromRequest = orderMapper.toEntity(orderUpsertRequest);

        if (!orderFromRequest.canUpdate(orderInDatabase)) {
            log.error("Order can’t be updated. {} ", orderId(orderId));
            throw new IllegalStateException("Order can’t be updated. " + orderId(orderId));
        }

        log.info("Updating order in database, source = {}, target = {}", orderFromRequest, orderInDatabase);
        BeanUtils.copyProperties(orderFromRequest, orderInDatabase, "id", "total_items", "total_payments");

        orderInDatabase.getTotal_items().forEach(item -> item.setOrder(orderInDatabase));
        orderInDatabase.getTotal_payments().forEach(payment -> payment.setOrder(orderInDatabase));

        return orderRepository.save(orderInDatabase);
    }

    @Override
    public void deleteOrder(Long orderId) {
        log.info("Order deleted attempt for {}", orderId(orderId));
        var order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: ", orderId));

        if (!order.canDelete(order)) {
            log.error("Order can’t be deleted. {} ", orderId(orderId));
            throw new IllegalStateException("Order can’t be deleted. " + orderId(orderId));
        }
        orderRepository.deleteById(order.getId());
    }
}
