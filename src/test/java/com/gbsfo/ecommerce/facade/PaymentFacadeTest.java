package com.gbsfo.ecommerce.facade;

import java.util.List;
import java.util.Optional;

import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.dto.IterableDataResponse;
import com.gbsfo.ecommerce.dto.PaymentDto;
import com.gbsfo.ecommerce.dto.PaymentLookupPublicApiRequest;
import com.gbsfo.ecommerce.mapper.PaymentMapper;
import com.gbsfo.ecommerce.service.impl.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PaymentFacade.class)
@RunWith(SpringRunner.class)
public class PaymentFacadeTest {

    private static final Long PAYMENT_ID = 1L;

    private static final String NUMBER = "number";

    @Autowired
    private PaymentFacade paymentFacade;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private PaymentMapper paymentMappper;

    @MockBean
    private PaymentDto mockPaymentDto;

    @MockBean
    private Payment mockPayment;

//    @Test
//    public void testFind() {
//        var request = new PaymentLookupPublicApiRequest();
//        var payments = List.of(new Payment());
//        var page = new PageImpl<>(payments);
//
//        when(paymentService.findPayments(request)).thenReturn(page);
//        when(paymentMappper.toDto(mockPayment)).thenReturn(mockPaymentDto);
//
//        IterableDataResponse<List<PaymentDto>> response = paymentFacade.find(request);
//
//        assertNotNull(response);
//        assertEquals(1, response.getData().size());
//        assertFalse(response.isHasNext());
//    }

    @Test
    public void testGetPaymentById() {
        when(paymentService.getPaymentById(anyLong())).thenReturn(mockPayment);
        when(paymentMappper.toDto(mockPayment)).thenReturn(mockPaymentDto);

        PaymentDto result = paymentFacade.getPaymentById(PAYMENT_ID);

        assertNotNull(result);
        verify(paymentMappper).toDto(mockPayment);
    }

    @Test
    public void testFindByNumber() {
        when(paymentService.findByNumber(NUMBER)).thenReturn(Optional.of(mockPayment));
        when(paymentMappper.toDto(mockPayment)).thenReturn(mockPaymentDto);

        PaymentDto result = paymentFacade.findByNumber(NUMBER);

        assertNotNull(result);
        verify(paymentMappper).toDto(mockPayment);
    }

    @Test
    public void testCreatePayment() {
        when(paymentService.createPayment(any(PaymentDto.class))).thenReturn(mockPayment);
        when(paymentMappper.toDto(mockPayment)).thenReturn(mockPaymentDto);
        PaymentDto result = paymentFacade.createPayment(new PaymentDto());

        assertNotNull(result);
        verify(paymentMappper).toDto(mockPayment);
    }

    @Test
    public void testUpdatePayment() {
        when(paymentService.updatePayment(anyLong(), any(PaymentDto.class))).thenReturn(mockPayment);
        when(paymentMappper.toDto(mockPayment)).thenReturn(mockPaymentDto);
        PaymentDto result = paymentFacade.updatePayment(PAYMENT_ID, new PaymentDto());

        assertNotNull(result);
        verify(paymentMappper).toDto(mockPayment);
    }

    @Test
    public void testDeletePayment() {
        paymentFacade.deletePayment(PAYMENT_ID);

        verify(paymentService, times(1)).deletePayment(PAYMENT_ID);
    }

}

