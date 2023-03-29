package com.gbsfo.ecommerce.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbsfo.ecommerce.configuration.WebFluxSecurityConfig;
import com.gbsfo.ecommerce.controller.exception.PublicApiErrorResponse;
import com.gbsfo.ecommerce.controller.exception.PublicApiErrorResponse.PublicApiError;
import com.gbsfo.ecommerce.controller.exception.PublicApiErrorResponse.PublicApiValidationError;
import com.gbsfo.ecommerce.controller.exception.ServiceValidationException;
import com.gbsfo.ecommerce.controller.exception.ServiceValidationException.Violation;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.IterableDataResponse;
import com.gbsfo.ecommerce.dto.OrderDto;
import com.gbsfo.ecommerce.dto.OrderDto.OrderStatus;
import com.gbsfo.ecommerce.dto.OrderLookupPublicApiRequest;
import com.gbsfo.ecommerce.dto.OrderUpsertRequest;
import com.gbsfo.ecommerce.dto.PaymentDto;
import com.gbsfo.ecommerce.facade.OrderFacade;
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

@WebFluxTest(controllers = OrderController.class)
@Import({TimeUtils.class, WebFluxSecurityConfig.class})
public class OrderControllerTest {

    private static final Long ORDER_ID = 1L;

    private static final String REQUEST_ID = "lrbo-4hty79-f028g0v";

    private static final String NUMBER = "ORD-001";

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TimeUtils timeUtils;

    @MockBean
    private OrderFacade orderFacade;

    @MockBean
    private Tracer tracer;

    private OrderDto order;

    @BeforeEach
    public void setUp() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(1L).name("item").price(BigDecimal.valueOf(100.0)).build();
        PaymentDto paymentDto = PaymentDto.builder().id(1L).number("PAY-001").sum(BigDecimal.valueOf(100.0))
            .paymentDateTime(timeUtils.getCurrentTime()).build();
        order = OrderDto.builder().id(ORDER_ID).number(NUMBER).orderStatus(OrderStatus.CREATED)
            .items(Collections.singletonList(itemDto)).payments(Collections.singletonList(paymentDto)).build();

        Span spanMock = Mockito.mock(Span.class);
        TraceContext spanTraceContext = Mockito.mock(TraceContext.class);
        given(spanMock.context()).willReturn(spanTraceContext);
        given(spanTraceContext.traceId()).willReturn(REQUEST_ID);
        given(tracer.currentSpan()).willReturn(spanMock);
    }

    @Test
    public void getOrderById_OrderFound_ExpectedOkHttpResponseStatusAndContactJsonResponse() throws Exception {
        given(orderFacade.getOrderById(eq(ORDER_ID))).willReturn(order);
        webClient.get().uri(API_VERSION_PREFIX_V1 + "/orders/" + ORDER_ID)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(order))
            .consumeWith(System.out::println);
    }

    @Test
    public void getOrderByNumber_OrderFound_ExpectedOkHttpResponseStatusAndContactJsonResponse() throws Exception {
        given(orderFacade.findByNumber(eq(NUMBER))).willReturn(order);
        webClient.get().uri(API_VERSION_PREFIX_V1 + "/orders/by-number/" + NUMBER)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(order))
            .consumeWith(System.out::println);
    }

    @Test
    public void getOrderById_OrderFacadeThrowsRuntimeException_ExpectedInternalServerErrorHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
        given(orderFacade.getOrderById(eq(ORDER_ID)))
            .willThrow(new RuntimeException());
        var apiError = new PublicApiError(API_ERROR_INTERNAL_ERROR_MESSAGE);
        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));

        webClient.get().uri(API_VERSION_PREFIX_V1 + "/orders/" + ORDER_ID)
            .exchange()
            .expectStatus().is5xxServerError()
            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void searchOrders_WithNumberOrdersFound_ExpectedOkHttpResponseStatusAndOrderJsonResponseAndVerifyDefaultOffsetAndLimitParametersSet() throws Exception {
        var orderLookupRequest = OrderLookupPublicApiRequest.builder()
            .number(NUMBER)
            //default parameters
            .offset(0)
            .limit(20)
            .build();
        var iterableDataResponse = new IterableDataResponse<>(List.of(order), true);
        given(orderFacade.find(eq(orderLookupRequest))).willReturn(iterableDataResponse);

        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/orders")
                .queryParam("number", orderLookupRequest.getNumber())
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(iterableDataResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void searchOrders_WithAllPossibleQueryParametersOrdersFound_ExpectedOkHttpResponseStatusAndOrderJsonResponse() throws Exception {
        var orderLookupRequest = OrderLookupPublicApiRequest.builder()
            .number(NUMBER)
            .orderStatus("CREATED")
            .offset(30)
            .limit(10)
            .build();
        var iterableDataResponse = new IterableDataResponse<>(List.of(order), true);
        given(orderFacade.find(eq(orderLookupRequest))).willReturn(iterableDataResponse);

        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/orders")
                .queryParam("number", orderLookupRequest.getNumber())
                .queryParam("order_status", orderLookupRequest.getOrderStatus())
                .queryParam("offset", String.valueOf(orderLookupRequest.getOffset()))
                .queryParam("limit", String.valueOf(orderLookupRequest.getLimit()))
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(iterableDataResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void searchOrders_WithNegativeOffset_ExpectedBadRequestHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
        given(orderFacade.find(any())).willThrow(
            new ServiceValidationException(
                "Invalid order lookup request",
                List.of(new Violation(API_VALIDATION_ERROR_MESSAGE_PAGINATION_OFFSET, API_OFFSET_REQUEST_PARAMETER))
            )
        );
        var apiError = new PublicApiValidationError(API_VALIDATION_ERROR_MESSAGE_PAGINATION_OFFSET, API_OFFSET_REQUEST_PARAMETER);
        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));

        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/orders")
                .queryParam(API_OFFSET_REQUEST_PARAMETER, "-5")
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void searchOrders_WithNegativeLimit_ExpectedBadRequestHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
        given(orderFacade.find(any())).willThrow(
            new ServiceValidationException(
                "Invalid contact lookup request",
                List.of(new Violation(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER))
            )
        );

        var apiError = new PublicApiValidationError(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER);
        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));

        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/orders")
                .queryParam(API_LIMIT_REQUEST_PARAMETER, "-5")
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void searchOrders_WithTooLargeLimit_ExpectedBadRequestHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
        given(orderFacade.find(any())).willThrow(
            new ServiceValidationException(
                "Invalid contact lookup request",
                List.of(new Violation(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER))
            )
        );

        var apiError = new PublicApiValidationError(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER);
        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));

        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/orders")
                .queryParam(API_LIMIT_REQUEST_PARAMETER, String.valueOf(API_LIMIT_REQUEST_PARAMETER_MAX_VALUE + 1))
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void createOrder_withValidRequest_ExpectedHttpResponseStatusOk() throws Exception {
        var orderUpsertRequest = OrderUpsertRequest.builder().number(NUMBER).orderStatus(OrderUpsertRequest.OrderStatus.CREATED)
            .items(List.of(ItemDto.builder().name("item").price(BigDecimal.valueOf(100.0)).build(),
                ItemDto.builder().name("item 2").price(BigDecimal.valueOf(200.0)).build()))
            .payments(List.of(
                PaymentDto.builder().number("PAY-001").sum(BigDecimal.valueOf(100.0)).build(),
                PaymentDto.builder().number("PAY-002").sum(BigDecimal.valueOf(200.0)).build())).build();

        given(orderFacade.createOrder(eq(orderUpsertRequest))).willReturn(order);

        webClient.post().uri(API_VERSION_PREFIX_V1 + "/orders")
            .body(BodyInserters.fromValue(orderUpsertRequest))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isCreated()
            .expectBody().json(objectMapper.writeValueAsString(order))
            .consumeWith(System.out::println);
    }

    @Test
    public void addItemsToOrder_withValidRequest_ExpectedOkHttpResponseStatus() throws Exception {
        ItemDto item1 = ItemDto.builder().name("item").price(BigDecimal.valueOf(100.0)).build();
        ItemDto item2 = ItemDto.builder().name("item 2").price(BigDecimal.valueOf(200.0)).build();
        var items = List.of(item1, item2);

        given(orderFacade.addItemsToOrder(eq(ORDER_ID), eq(items))).willReturn(order);

        webClient.post().uri(API_VERSION_PREFIX_V1 + "/orders/" + ORDER_ID + "/items")
            .body(BodyInserters.fromValue(items))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isCreated()
            .expectBody().json(objectMapper.writeValueAsString(order))
            .consumeWith(System.out::println);
    }

    @Test
    public void updateOrder_withValidRequest_ExpectedOkHttpResponseStatus() throws Exception {
        var orderUpsertRequest = OrderUpsertRequest.builder().number(NUMBER).orderStatus(OrderUpsertRequest.OrderStatus.CREATED)
            .items(List.of(ItemDto.builder().name("item").price(BigDecimal.valueOf(100.0)).build(),
                ItemDto.builder().name("item 2").price(BigDecimal.valueOf(200.0)).build()))
            .payments(List.of(
                PaymentDto.builder().number("PAY-001").sum(BigDecimal.valueOf(100.0)).paymentDateTime(timeUtils.getCurrentTime()).build(),
                PaymentDto.builder().number("PAY-002").sum(BigDecimal.valueOf(200.0)).paymentDateTime(timeUtils.getCurrentTime()).build())).build();

        given(orderFacade.updateOrder(eq(ORDER_ID), any(OrderUpsertRequest.class))).willReturn(order);

        webClient.put().uri(API_VERSION_PREFIX_V1 + "/orders/" + ORDER_ID)
            .body(BodyInserters.fromValue(orderUpsertRequest))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(order))
            .consumeWith(System.out::println);
    }

    @Test
    public void deleteOrder_ExistingOrder_ExpectedOkHttpResponseStatusAndOrderJsonResponse() throws Exception {
        webClient.delete().uri(API_VERSION_PREFIX_V1 + "/orders/" + ORDER_ID)
            .exchange()
            .expectStatus().isOk();

        verify(orderFacade).deleteOrder(eq(ORDER_ID));
    }
}
