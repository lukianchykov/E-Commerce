package com.gbsfo.ecommerce.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Order.OrderStatus;
import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.dto.PaymentDto;
import com.gbsfo.ecommerce.dto.PaymentLookupPublicApiRequest;
import com.gbsfo.ecommerce.mapper.PaymentMapper;
import com.gbsfo.ecommerce.repository.PaymentRepository;
import com.gbsfo.ecommerce.service.impl.PaymentService;
import com.gbsfo.ecommerce.utils.time.TimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import({PaymentService.class, TimeUtils.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaymentServiceTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TimeUtils timeUtils;

    @MockBean
    private PaymentMapper paymentMapper;

    private Payment payment;

    @Before
    public void setUp() {
        payment = Payment.builder().number("Payment 1").sum(new BigDecimal("123.45"))
            .order(Order.builder().number("Number 1").orderStatus(OrderStatus.CREATED).build()).build();
        paymentRepository.save(payment);
    }

    @After
    public void cleanup() {
        paymentRepository.delete(payment);
    }

    @Test
    public void verify_findPaymentById() {
        Payment actual = paymentService.getPaymentById(payment.getId());
        assertEquals(payment, actual);
    }

    @Test
    public void findPayments_withFullSearch() {
        PaymentLookupPublicApiRequest paymentLookupPublicApiRequest = PaymentLookupPublicApiRequest.builder()
            .number("Payment 1")
            .sum(BigDecimal.valueOf(123.45))
            .offset(0)
            .limit(20)
            .build();

        Page<Payment> actual = paymentService.findPayments(paymentLookupPublicApiRequest);

        assertEquals(payment, actual.getContent().get(0));
    }

    @Test
    public void findPayments_withPartialSearch() {
        PaymentLookupPublicApiRequest paymentLookupPublicApiRequest = PaymentLookupPublicApiRequest.builder()
            .number("Payment 1")
            .offset(0)
            .limit(20)
            .build();

        Page<Payment> actual = paymentService.findPayments(paymentLookupPublicApiRequest);

        assertEquals(payment, actual.getContent().get(0));
    }

    @Test
    public void findPayments_withPartialSearchWhenThereIsWrongField_shouldNotFindEntityAndTrowAnError() {
        PaymentLookupPublicApiRequest paymentLookupPublicApiRequest = PaymentLookupPublicApiRequest.builder()
            .number("INVALID")
            .sum(BigDecimal.valueOf(0.00))
            .offset(0)
            .limit(20)
            .build();

        Page<Payment> actual = paymentService.findPayments(paymentLookupPublicApiRequest);

        assertTrue(actual.getContent().isEmpty());
    }

    @Test
    public void verify_findPaymentByNumber() {
        Optional<Payment> actual = paymentService.findByNumber(payment.getNumber());
        assertEquals(payment, actual.get());
        assertEquals(payment.getNumber(), actual.get().getNumber());
    }

    @Test
    public void createPayment() {
        Payment newPayment = Payment.builder().number("payment123").sum(new BigDecimal("10.00")).build();
        when(paymentMapper.toEntity(any(PaymentDto.class))).thenReturn(newPayment);
        PaymentDto paymentDto = PaymentDto.builder().number("paymentNumber").sum(new BigDecimal("10.00")).build();

        Payment createdPayment = paymentService.createPayment(paymentDto);

        assertNotNull(paymentRepository.findById(createdPayment.getId()));
        assertEquals(newPayment, createdPayment);
    }

    @Test
    public void updatePayment_shouldUpdatePaymentInDatabase() {
        PaymentDto paymentDto = PaymentDto.builder().id(4L).number("number123").sum(new BigDecimal("10.00")).build();
        Payment paymentInDatabase = Payment.builder().id(3L).number("Numb1").sum(new BigDecimal("20.00")).build();

        when(paymentMapper.toEntity(paymentDto)).thenReturn(Payment.builder().number(paymentDto.getNumber()).build());

        Payment savedPayment = paymentRepository.save(paymentInDatabase);
        assertNotNull(paymentRepository.findById(savedPayment.getId()));

        assertNotEquals(paymentDto.getNumber(), savedPayment.getNumber());

        Payment updatedPayment = paymentService.updatePayment(savedPayment.getId(), paymentDto);
        assertEquals(paymentDto.getNumber(), updatedPayment.getNumber());
    }

    @Test
    public void deletePayment() {
        paymentService.deletePayment(payment.getId());
        assertEquals(Optional.empty(), paymentRepository.findById(payment.getId()));
    }
}
