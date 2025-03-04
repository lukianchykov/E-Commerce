package com.gbsfo.ecommerce.controller;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbsfo.ecommerce.configuration.WebFluxSecurityConfig;
import com.gbsfo.ecommerce.controller.exception.PublicApiErrorResponse;
import com.gbsfo.ecommerce.controller.exception.PublicApiErrorResponse.PublicApiError;
import com.gbsfo.ecommerce.controller.exception.PublicApiErrorResponse.PublicApiValidationError;
import com.gbsfo.ecommerce.controller.exception.ServiceValidationException;
import com.gbsfo.ecommerce.controller.exception.ServiceValidationException.Violation;
import com.gbsfo.ecommerce.dto.IterableDataResponse;
import com.gbsfo.ecommerce.dto.PaymentDto;
import com.gbsfo.ecommerce.dto.PaymentLookupPublicApiRequest;
import com.gbsfo.ecommerce.facade.PaymentFacade;
import com.gbsfo.ecommerce.utils.time.TimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.gbsfo.ecommerce.utils.Constants.API_ERROR_INTERNAL_ERROR_MESSAGE;
import static com.gbsfo.ecommerce.utils.Constants.API_LIMIT_REQUEST_PARAMETER;
import static com.gbsfo.ecommerce.utils.Constants.API_LIMIT_REQUEST_PARAMETER_MAX_VALUE;
import static com.gbsfo.ecommerce.utils.Constants.API_OFFSET_REQUEST_PARAMETER;
import static com.gbsfo.ecommerce.utils.Constants.API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT;
import static com.gbsfo.ecommerce.utils.Constants.API_VALIDATION_ERROR_MESSAGE_PAGINATION_OFFSET;
import static com.gbsfo.ecommerce.utils.Constants.API_VERSION_PREFIX_V1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebFluxTest(controllers = PaymentController.class)
@Import({TimeUtils.class, WebFluxSecurityConfig.class})
public class PaymentControllerTest {

    private static final Long PAYMENT_ID = 1L;

    private static final String REQUEST_ID = "lrbo-4hty79-f028g0v";

    private static final String NUMBER = "PAY-001";

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TimeUtils timeUtils;

    @MockBean
    private PaymentFacade paymentFacade;

    @MockBean
    private Tracer tracer;

    private PaymentDto payment;

    @BeforeEach
    public void setUp() throws Exception {
        payment = PaymentDto.builder().id(1L).number("PAY-001").sum(BigDecimal.valueOf(100.0))
            .paymentDateTime(timeUtils.getCurrentTime()).build();

        Span spanMock = Mockito.mock(Span.class);
        TraceContext spanTraceContext = Mockito.mock(TraceContext.class);
        given(spanMock.context()).willReturn(spanTraceContext);
        given(spanTraceContext.traceId()).willReturn(REQUEST_ID);
        given(tracer.currentSpan()).willReturn(spanMock);
    }

    @Test
    public void getPaymentById_PaymentFound_ExpectedOkHttpResponseStatusAndContactJsonResponse() throws Exception {
        given(paymentFacade.getPaymentById(eq(PAYMENT_ID))).willReturn(payment);
        webClient.get().uri(API_VERSION_PREFIX_V1 + "/payments/" + PAYMENT_ID)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(payment))
            .consumeWith(System.out::println);
    }

    @Test
    public void getPaymentByNumber_PaymentFound_ExpectedOkHttpResponseStatusAndContactJsonResponse() throws Exception {
        given(paymentFacade.findByNumber(eq(NUMBER))).willReturn(payment);
        webClient.get().uri(API_VERSION_PREFIX_V1 + "/payments/by-number/" + NUMBER)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(payment))
            .consumeWith(System.out::println);
    }

    @Test
    public void getPaymentById_paymentFacadeThrowsRuntimeException_ExpectedInternalServerErrorHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
        given(paymentFacade.getPaymentById(eq(PAYMENT_ID)))
            .willThrow(new RuntimeException());
        var apiError = new PublicApiError(API_ERROR_INTERNAL_ERROR_MESSAGE);
        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));

        webClient.get().uri(API_VERSION_PREFIX_V1 + "/payments/" + PAYMENT_ID)
            .exchange()
            .expectStatus().is5xxServerError()
            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
            .consumeWith(System.out::println);
    }

//    @Test
//    public void searchPayments_WithNumberPaymentsFound_ExpectedOkHttpResponseStatusAndPaymentJsonResponseAndVerifyDefaultOffsetAndLimitParametersSet() throws Exception {
//        var paymentLookupRequest = PaymentLookupPublicApiRequest.builder()
//            .number(NUMBER)
//            //default parameters
//            .offset(0)
//            .limit(20)
//            .build();
//        var iterableDataResponse = new IterableDataResponse<>(List.of(payment), true);
//        given(paymentFacade.find(eq(paymentLookupRequest))).willReturn(iterableDataResponse);
//
//        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/payments")
//                .queryParam("number", paymentLookupRequest.getNumber())
//                .build())
//            .accept(MediaType.APPLICATION_JSON)
//            .exchange()
//            .expectStatus().isOk()
//            .expectBody().json(objectMapper.writeValueAsString(iterableDataResponse))
//            .consumeWith(System.out::println);
//    }
//
//    @Test
//    public void searchPayments_WithAllPossibleQueryParametersPaymentsFound_ExpectedOkHttpResponseStatusAndPaymentJsonResponse() throws Exception {
//        var paymentLookupRequest = PaymentLookupPublicApiRequest.builder()
//            .number(NUMBER)
//            .sum(BigDecimal.valueOf(100.0))
//            .offset(30)
//            .limit(10)
//            .build();
//        var iterableDataResponse = new IterableDataResponse<>(List.of(payment), true);
//        given(paymentFacade.find(eq(paymentLookupRequest))).willReturn(iterableDataResponse);
//
//        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/payments")
//                .queryParam("number", paymentLookupRequest.getNumber())
//                .queryParam("sum", paymentLookupRequest.getSum())
//                .queryParam("offset", String.valueOf(paymentLookupRequest.getOffset()))
//                .queryParam("limit", String.valueOf(paymentLookupRequest.getLimit()))
//                .build())
//            .accept(MediaType.APPLICATION_JSON)
//            .exchange()
//            .expectStatus().isOk()
//            .expectBody().json(objectMapper.writeValueAsString(iterableDataResponse))
//            .consumeWith(System.out::println);
//    }
//
//    @Test
//    public void searchPayments_WithNegativeOffset_ExpectedBadRequestHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
//        given(paymentFacade.find(any())).willThrow(
//            new ServiceValidationException(
//                "Invalid Payment lookup request",
//                List.of(new Violation(API_VALIDATION_ERROR_MESSAGE_PAGINATION_OFFSET, API_OFFSET_REQUEST_PARAMETER))
//            )
//        );
//        var apiError = new PublicApiValidationError(API_VALIDATION_ERROR_MESSAGE_PAGINATION_OFFSET, API_OFFSET_REQUEST_PARAMETER);
//        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));
//
//        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/payments")
//                .queryParam(API_OFFSET_REQUEST_PARAMETER, "-5")
//                .build())
//            .accept(MediaType.APPLICATION_JSON)
//            .exchange()
//            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
//            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
//            .consumeWith(System.out::println);
//    }
//
//    @Test
//    public void searchPayments_WithNegativeLimit_ExpectedBadRequestHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
//        given(paymentFacade.find(any())).willThrow(
//            new ServiceValidationException(
//                "Invalid contact lookup request",
//                List.of(new Violation(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER))
//            )
//        );
//
//        var apiError = new PublicApiValidationError(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER);
//        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));
//
//        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/payments")
//                .queryParam(API_LIMIT_REQUEST_PARAMETER, "-5")
//                .build())
//            .accept(MediaType.APPLICATION_JSON)
//            .exchange()
//            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
//            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
//            .consumeWith(System.out::println);
//    }
//
//    @Test
//    public void searchPayments_WithTooLargeLimit_ExpectedBadRequestHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
//        given(paymentFacade.find(any())).willThrow(
//            new ServiceValidationException(
//                "Invalid contact lookup request",
//                List.of(new Violation(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER))
//            )
//        );
//
//        var apiError = new PublicApiValidationError(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER);
//        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));
//
//        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/payments")
//                .queryParam(API_LIMIT_REQUEST_PARAMETER, String.valueOf(API_LIMIT_REQUEST_PARAMETER_MAX_VALUE + 1))
//                .build())
//            .accept(MediaType.APPLICATION_JSON)
//            .exchange()
//            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
//            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
//            .consumeWith(System.out::println);
//    }

    @Test
    public void createPayment_withValidRequest_ExpectedHttpResponseStatusOk() throws Exception {
        var newPayment = PaymentDto.builder().number("PaymentNumber").sum(BigDecimal.valueOf(100.0)).build();
        given(paymentFacade.createPayment(eq(newPayment))).willReturn(payment);

        webClient.post().uri(API_VERSION_PREFIX_V1 + "/payments")
            .body(BodyInserters.fromValue(newPayment))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isCreated()
            .expectBody().json(objectMapper.writeValueAsString(payment))
            .consumeWith(System.out::println);
    }

    @Test
    public void updatePayment_withValidRequest_ExpectedOkHttpResponseStatus() throws Exception {
        var newPayment = PaymentDto.builder().number("PaymentNumber").sum(BigDecimal.valueOf(100.0)).build();
        given(paymentFacade.updatePayment(eq(PAYMENT_ID), eq(newPayment))).willReturn(payment);

        webClient.put().uri(API_VERSION_PREFIX_V1 + "/payments/" + PAYMENT_ID)
            .body(BodyInserters.fromValue(newPayment))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(payment))
            .consumeWith(System.out::println);
    }

    @Test
    public void deletePayment_ExistingPayment_ExpectedOkHttpResponseStatusAndPaymentJsonResponse() throws Exception {
        webClient.delete().uri(API_VERSION_PREFIX_V1 + "/payments/" + PAYMENT_ID)
            .exchange()
            .expectStatus().isOk();

        verify(paymentFacade).deletePayment(eq(PAYMENT_ID));
    }
}
