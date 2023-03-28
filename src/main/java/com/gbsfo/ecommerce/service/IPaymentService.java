package com.gbsfo.ecommerce.service;

import java.util.Optional;

import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.dto.PaymentDto;
import com.gbsfo.ecommerce.dto.PaymentLookupPublicApiRequest;

import org.springframework.data.domain.Page;

public interface IPaymentService {

    Payment getPaymentById(Long paymentId);

    Page<Payment> findPayments(PaymentLookupPublicApiRequest request);

    Optional<Payment> findByNumber(String number);

    Payment createPayment(PaymentDto paymentDto);

    Payment updatePayment(Long paymentId, PaymentDto paymentDto);

    void deletePayment(Long paymentId);
}
