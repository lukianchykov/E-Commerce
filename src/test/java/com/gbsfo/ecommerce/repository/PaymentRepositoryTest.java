package com.gbsfo.ecommerce.repository;

import java.math.BigDecimal;
import java.util.Objects;

import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Order.OrderStatus;
import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.utils.time.TimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TimeUtils.class)
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TimeUtils timeUtils;

    private Payment payment;

    @Before
    public void setUp() {
        payment = Payment.builder().number("Payment 1").sum(new BigDecimal("10.00"))
            .order(Order.builder().number("Number 1").orderStatus(OrderStatus.CREATED).build()).build();
        paymentRepository.save(payment);
    }

    @After
    public void cleanup() {
        paymentRepository.delete(payment);
    }

    @Test
    public void verify_FindAll() {
        var Payments = paymentRepository.findAll();
        assertFalse(Payments.isEmpty());
    }

    @Test
    public void verify_PaymentCreatedAndSaved() {
        assertNotNull(payment.getId());
        assertNotNull(payment.getOrder());
        assertNotNull(payment.getPaymentDateTime());
        assertEquals("Payment 1", payment.getNumber());
        assertEquals(new BigDecimal("10.00"), payment.getSum());

    }

    @Test
    public void verify_PaymentFoundByName() {
        payment = paymentRepository.findByNumber("Payment 1").orElse(null);
        assertNotNull(Objects.requireNonNull(payment).getNumber());
    }

    @Test
    public void verify_PaymentDeletedAndAllPaymentsArePaid() {
        paymentRepository.delete(payment);
        assertFalse(paymentRepository.findById(payment.getId()).isPresent());
    }
}
