package com.gbsfo.ecommerce.controller;

import java.util.List;

import javax.validation.Valid;

import com.gbsfo.ecommerce.dto.IterableDataResponse;
import com.gbsfo.ecommerce.dto.OrderDto;
import com.gbsfo.ecommerce.dto.OrderLookupPublicApiRequest;
import com.gbsfo.ecommerce.facade.OrderFacade;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.gbsfo.ecommerce.utils.Constants.API_LIMIT_REQUEST_PARAMETER;
import static com.gbsfo.ecommerce.utils.Constants.API_OFFSET_REQUEST_PARAMETER;
import static com.gbsfo.ecommerce.utils.Constants.API_VERSION_PREFIX_V1;

@Slf4j
@RestController
@RequestMapping(API_VERSION_PREFIX_V1 + "/orders")
public class OrderController {

    @Autowired
    private OrderFacade orderFacade;

    @GetMapping
    public IterableDataResponse<List<OrderDto>> searchOrders(
        @RequestParam(value = "number", required = false) String number,
        @RequestParam(value = "order_status", required = false) String orderStatus,
        @RequestParam(value = API_OFFSET_REQUEST_PARAMETER, required = false, defaultValue = "0") int offset,
        @RequestParam(value = API_LIMIT_REQUEST_PARAMETER, required = false, defaultValue = "20") int limit
    ) {
        var orderLookupRequest = new OrderLookupPublicApiRequest(number, orderStatus, offset, limit);
        return orderFacade.find(orderLookupRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable(value = "id") Long orderId) {
        return ResponseEntity.ok(orderFacade.getOrderById(orderId));
    }

    @GetMapping("/by-number/{number}")
    public ResponseEntity<OrderDto> findByNumber(@PathVariable String number) {
        return ResponseEntity.ok(orderFacade.findByNumber(number));
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderFacade.createOrder(order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable(value = "id") Long orderId, @Valid @RequestBody OrderDto order) {
        return ResponseEntity.ok().body(orderFacade.updateOrder(orderId, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable(value = "id") Long orderId) {
        orderFacade.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
