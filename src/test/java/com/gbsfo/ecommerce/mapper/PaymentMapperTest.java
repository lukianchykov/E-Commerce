package com.gbsfo.ecommerce.mapper;

import java.math.BigDecimal;

import com.gbsfo.ecommerce.domain.Order;
import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.dto.PaymentDto;
import com.gbsfo.ecommerce.utils.time.TimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@Import({TimeUtils.class, PaymentMapperImpl.class})
public class PaymentMapperTest {

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private TimeUtils timeUtils;

    private Payment payment;

    private PaymentDto paymentDto;

    @Before
    public void setUp() {
        payment = new Payment();
        payment.setId(1L);
        payment.setSum(BigDecimal.valueOf(100.0));
        payment.setPaymentDateTime(timeUtils.getCurrentTime());
        payment.setOrder(new Order());

        paymentDto = PaymentDto.builder()
            .id(1L)
            .sum(BigDecimal.valueOf(100.0))
            .paymentDateTime(timeUtils.getCurrentTime())
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

