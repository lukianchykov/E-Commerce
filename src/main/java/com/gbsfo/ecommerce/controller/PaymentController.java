package com.gbsfo.ecommerce.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import com.gbsfo.ecommerce.dto.IterableDataResponse;
import com.gbsfo.ecommerce.dto.PaymentDto;
import com.gbsfo.ecommerce.dto.PaymentLookupPublicApiRequest;
import com.gbsfo.ecommerce.facade.PaymentFacade;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping(API_VERSION_PREFIX_V1 + "/payments")
@ApiResponses(value = {
    @ApiResponse(code = 400, message = "This is a bad request, please follow the API documentation for the proper request format"),
    @ApiResponse(code = 401, message = "Due to security constraints, your access request cannot be authorized"),
    @ApiResponse(code = 500, message = "The server is down. Please be with us."),
})
public class PaymentController {

    @Autowired
    private PaymentFacade paymentFacade;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public IterableDataResponse<List<PaymentDto>> searchPayments(
        @RequestParam(value = "number", required = false) String number,
        @RequestParam(value = "sum", required = false) BigDecimal sum,
        @RequestParam(value = API_OFFSET_REQUEST_PARAMETER, required = false, defaultValue = "0") int offset,
        @RequestParam(value = API_LIMIT_REQUEST_PARAMETER, required = false, defaultValue = "20") int limit
    ) {
        var PaymentLookupRequest = new PaymentLookupPublicApiRequest(number, sum, offset, limit);
        return paymentFacade.find(PaymentLookupRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable(value = "id") Long paymentId) {
        return ResponseEntity.ok(paymentFacade.getPaymentById(paymentId));
    }

    @GetMapping("/by-number/{number}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentDto> findByNumber(@PathVariable String number) {
        return ResponseEntity.ok(paymentFacade.findByNumber(number));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentDto paymentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentFacade.createPayment(paymentDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable(value = "id") Long paymentId, @Valid @RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok().body(paymentFacade.updatePayment(paymentId, paymentDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> deletePayment(@PathVariable(value = "id") Long paymentId) {
        paymentFacade.deletePayment(paymentId);
        return ResponseEntity.ok().build();
    }
}
