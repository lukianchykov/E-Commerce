package com.gbsfo.ecommerce.mapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.dto.PaymentDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@Import(PaymentMapperImpl.class)
public class PaymentMapperTest {

    @Autowired
    private PaymentMapper paymentMapper;

    private Payment payment;

    private PaymentDto paymentDto;

    @Before
    public void setUp() {
        Instant creationDate = LocalDateTime.of(2022, 3, 27, 14, 30).toInstant(ZoneOffset.UTC);

        payment = new Payment();
        payment.setId(1L);
        payment.setSum(BigDecimal.valueOf(100.0));
        payment.setPaymentDateTime(creationDate);
        payment.setOrder(new Order());

        paymentDto = PaymentDto.builder()
            .id(1L)
            .sum(BigDecimal.valueOf(100.0))
            .paymentDateTime(creationDate)
            .build();
    }

    @Test
    public void toEntity_VerifyAllRequiredDataSet() {
        Payment payment = paymentMapper.toEntity(paymentDto);

        assertNotNull(payment);
        assertEquals(payment.getId(), paymentDto.getId());
        assertEquals(payment.getSum(), paymentDto.getSum());
        assertEquals(payment.getNumber(), paymentDto.getNumber());
        assertEquals(payment.getPaymentDateTime(), paymentDto.getPaymentDateTime());
    }

    @Test
    public void toDto_VerifyAllRequiredDataSet() {
        PaymentDto paymentDto = paymentMapper.toDto(payment);

        assertNotNull(paymentDto);
        assertEquals(payment.getId(), paymentDto.getId());
        assertEquals(payment.getSum(), paymentDto.getSum());
        assertEquals(payment.getNumber(), paymentDto.getNumber());
        assertEquals(payment.getPaymentDateTime(), paymentDto.getPaymentDateTime());
    }
}

