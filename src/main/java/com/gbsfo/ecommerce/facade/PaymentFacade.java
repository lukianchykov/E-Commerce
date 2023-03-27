package com.gbsfo.ecommerce.facade;

import java.util.List;
import java.util.stream.Collectors;

import com.gbsfo.ecommerce.controller.exception.ResourceNotFoundException;
import com.gbsfo.ecommerce.dto.IterableDataResponse;
import com.gbsfo.ecommerce.dto.PaymentDto;
import com.gbsfo.ecommerce.dto.PaymentLookupPublicApiRequest;
import com.gbsfo.ecommerce.mapper.PaymentMapper;
import com.gbsfo.ecommerce.service.impl.PaymentService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentFacade {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentMapper paymentMapper;

    public IterableDataResponse<List<PaymentDto>> find(PaymentLookupPublicApiRequest paymetLookupRequest) {
        var page = paymentService.findPayments(paymetLookupRequest);

        var payments = page.getContent().stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toList());
        return new IterableDataResponse<>(payments, page.hasNext());
    }

    public PaymentDto getPaymentById(Long paymentId) {
        var payment = paymentService.getPaymentById(paymentId);
        return paymentService.getPaymentById(paymentId) != null ? paymentMapper.toDto(payment) : null;
    }

    public PaymentDto findByNumber(String number) {
        var paymentByNumber = paymentService.findByNumber(number)
            .orElseThrow(() -> new ResourceNotFoundException("Did not found by number", number));
        return paymentMapper.toDto(paymentByNumber);
    }

    public PaymentDto createPayment(PaymentDto paymentDto) {
        var paymentFromDto = paymentMapper.toEntity(paymentDto);
        var payment = paymentService.createPayment(paymentFromDto);
        return paymentMapper.toDto(payment);
    }

    public PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto) {
        var payment = paymentService.updatePayment(paymentId, paymentDto);
        return paymentMapper.toDto(payment);
    }

    public void deletePayment(Long paymentId) {
        paymentService.deletePayment(paymentId);
    }
}
