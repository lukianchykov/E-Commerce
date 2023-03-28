package com.gbsfo.ecommerce.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import com.gbsfo.ecommerce.controller.exception.ResourceAlreadyExistException;
import com.gbsfo.ecommerce.controller.exception.ResourceNotFoundException;
import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.dto.PaymentDto;
import com.gbsfo.ecommerce.dto.PaymentLookupPublicApiRequest;
import com.gbsfo.ecommerce.mapper.PaymentMapper;
import com.gbsfo.ecommerce.repository.PaymentRepository;
import com.gbsfo.ecommerce.service.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.util.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.gbsfo.ecommerce.service.specification.PaymentSearchSpecifications.idEquals;
import static com.gbsfo.ecommerce.service.specification.PaymentSearchSpecifications.numberEquals;
import static com.gbsfo.ecommerce.service.specification.PaymentSearchSpecifications.sumEquals;
import static com.gbsfo.ecommerce.utils.LogStructuredArguments.paymentId;

@Service
@Transactional
@Slf4j
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    /**
     * Public API find payments request
     * Example:
     * total: 100
     * offset: 50, limit: 10
     * pages to skip = 5
     * 50 / 10 = offset / limit = 5
     */
    @Override
    public Page<Payment> findPayments(PaymentLookupPublicApiRequest request) {
        Specification<Payment> searchSpecification = Specification
            .where(idEquals(request.getId()))
            .and(numberEquals(request.getNumber()))
            .and(sumEquals(request.getSum()));

        return paymentRepository.findAll(searchSpecification, PageRequest.of(request.getOffset() / request.getLimit(), request.getLimit()));
    }

    @Override
    public Payment getPaymentById(Long paymentId) {
        log.info("Get Payment attempt for {}", paymentId(paymentId));
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: ", paymentId));
    }

    @Override
    public Optional<Payment> findByNumber(String number) {
        if (StringUtils.isBlank(number)) {
            log.error("Number is blank: {}", number);
            throw new IllegalStateException("Number is blank: " + number);
        }
        return paymentRepository.findByNumber(number);
    }

    @Override
    public Payment createPayment(PaymentDto paymentDto) {
        var payment = paymentMapper.toEntity(paymentDto);
        if (findByNumber(payment.getNumber()).isPresent()) {
            log.error("Payment already exists. Can’t create new {} with same number: {}", paymentId(payment.getId()), payment.getNumber());
            throw new ResourceAlreadyExistException("Payment already exists. Can’t create new Payment with same number: " + payment.getNumber());
        }
        log.info("Saving Payment in database {}", payment);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(Long paymentId, PaymentDto paymentRequest) {
        var paymentInDatabase = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: ", paymentId));
        var paymentFromRequest = paymentMapper.toEntity(paymentRequest);

        log.info("Updating Payment in database, source = {}, target = {}", paymentInDatabase, paymentFromRequest);
        BeanUtils.copyProperties(paymentInDatabase, paymentFromRequest, "id", "paymentDateTime");

        return paymentRepository.save(paymentFromRequest);
    }

    @Override
    public void deletePayment(Long paymentId) {
        log.info("Payment deleted attempt for {}", paymentId(paymentId));
        var payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: ", paymentId));

        paymentRepository.deleteById(payment.getId());
    }

}
