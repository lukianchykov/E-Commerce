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
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.ItemLookupPublicApiRequest;
import com.gbsfo.ecommerce.dto.IterableDataResponse;
import com.gbsfo.ecommerce.facade.ItemFacade;
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

@WebFluxTest(controllers = ItemController.class)
@Import(WebFluxSecurityConfig.class)

public class ItemControllerTest {

    private static final Long ITEM_ID = 1L;

    private static final String REQUEST_ID = "lrbo-4hty79-f028g0v";

    private static final String NAME = "NAME-001";

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemFacade itemFacade;

    @MockBean
    private Tracer tracer;

    private ItemDto item;

    @BeforeEach
    public void setUp() throws Exception {
        item = ItemDto.builder().id(1L).name(NAME).price(BigDecimal.valueOf(100.0)).build();

        Span spanMock = Mockito.mock(Span.class);
        TraceContext spanTraceContext = Mockito.mock(TraceContext.class);
        given(spanMock.context()).willReturn(spanTraceContext);
        given(spanTraceContext.traceId()).willReturn(REQUEST_ID);
        given(tracer.currentSpan()).willReturn(spanMock);
    }

    @Test
    public void getItemById_ItemFound_ExpectedOkHttpResponseStatusAndContactJsonResponse() throws Exception {
        given(itemFacade.getItemById(eq(ITEM_ID))).willReturn(item);
        webClient.get().uri(API_VERSION_PREFIX_V1 + "/items/" + ITEM_ID)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(item))
            .consumeWith(System.out::println);
    }

    @Test
    public void getItemByNumber_ItemFound_ExpectedOkHttpResponseStatusAndContactJsonResponse() throws Exception {
        given(itemFacade.findByName(eq(NAME))).willReturn(item);
        webClient.get().uri(API_VERSION_PREFIX_V1 + "/items/by-name/" + NAME)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(item))
            .consumeWith(System.out::println);
    }

    @Test
    public void getItemById_itemFacadeThrowsRuntimeException_ExpectedInternalServerErrorHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
        given(itemFacade.getItemById(eq(ITEM_ID)))
            .willThrow(new RuntimeException());
        var apiError = new PublicApiError(API_ERROR_INTERNAL_ERROR_MESSAGE);
        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));

        webClient.get().uri(API_VERSION_PREFIX_V1 + "/items/" + ITEM_ID)
            .exchange()
            .expectStatus().is5xxServerError()
            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void searchItems_WithNumberItemsFound_ExpectedOkHttpResponseStatusAndItemJsonResponseAndVerifyDefaultOffsetAndLimitParametersSet() throws Exception {
        var itemLookupRequest = ItemLookupPublicApiRequest.builder()
            .name(NAME)
            //default parameters
            .offset(0)
            .limit(20)
            .build();
        var iterableDataResponse = new IterableDataResponse<>(List.of(item), true);
        given(itemFacade.find(eq(itemLookupRequest))).willReturn(iterableDataResponse);

        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/items")
                .queryParam("name", itemLookupRequest.getName())
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(iterableDataResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void searchItems_WithAllPossibleQueryParametersItemsFound_ExpectedOkHttpResponseStatusAndItemJsonResponse() throws Exception {
        var itemLookupRequest = ItemLookupPublicApiRequest.builder()
            .name(NAME)
            .price(BigDecimal.valueOf(100.0))
            .offset(30)
            .limit(10)
            .build();
        var iterableDataResponse = new IterableDataResponse<>(List.of(item), true);
        given(itemFacade.find(eq(itemLookupRequest))).willReturn(iterableDataResponse);

        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/items")
                .queryParam("name", itemLookupRequest.getName())
                .queryParam("price", itemLookupRequest.getPrice())
                .queryParam("offset", String.valueOf(itemLookupRequest.getOffset()))
                .queryParam("limit", String.valueOf(itemLookupRequest.getLimit()))
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(iterableDataResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void searchItems_WithNegativeOffset_ExpectedBadRequestHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
        given(itemFacade.find(any())).willThrow(
            new ServiceValidationException(
                "Invalid Item lookup request",
                List.of(new Violation(API_VALIDATION_ERROR_MESSAGE_PAGINATION_OFFSET, API_OFFSET_REQUEST_PARAMETER))
            )
        );
        var apiError = new PublicApiValidationError(API_VALIDATION_ERROR_MESSAGE_PAGINATION_OFFSET, API_OFFSET_REQUEST_PARAMETER);
        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));

        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/items")
                .queryParam(API_OFFSET_REQUEST_PARAMETER, "-5")
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void searchItems_WithNegativeLimit_ExpectedBadRequestHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
        given(itemFacade.find(any())).willThrow(
            new ServiceValidationException(
                "Invalid contact lookup request",
                List.of(new Violation(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER))
            )
        );

        var apiError = new PublicApiValidationError(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER);
        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));

        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/items")
                .queryParam(API_LIMIT_REQUEST_PARAMETER, "-5")
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void searchItems_WithTooLargeLimit_ExpectedBadRequestHttpResponseStatusAndCorrectErrorResponseBody() throws Exception {
        given(itemFacade.find(any())).willThrow(
            new ServiceValidationException(
                "Invalid contact lookup request",
                List.of(new Violation(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER))
            )
        );

        var apiError = new PublicApiValidationError(API_VALIDATION_ERROR_MESSAGE_PAGINATION_LIMIT, API_LIMIT_REQUEST_PARAMETER);
        var publicApiErrorResponse = new PublicApiErrorResponse(REQUEST_ID, List.of(apiError));

        webClient.get().uri(uriBuilder -> uriBuilder.path(API_VERSION_PREFIX_V1 + "/items")
                .queryParam(API_LIMIT_REQUEST_PARAMETER, String.valueOf(API_LIMIT_REQUEST_PARAMETER_MAX_VALUE + 1))
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
            .expectBody().json(objectMapper.writeValueAsString(publicApiErrorResponse))
            .consumeWith(System.out::println);
    }

    @Test
    public void createItem_withValidRequest_ExpectedHttpResponseStatusOk() throws Exception {
        var newItem = ItemDto.builder().name("item").price(BigDecimal.valueOf(100.0)).build();
        given(itemFacade.createItem(eq(newItem))).willReturn(item);

        webClient.post().uri(API_VERSION_PREFIX_V1 + "/items")
            .body(BodyInserters.fromValue(newItem))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isCreated()
            .expectBody().json(objectMapper.writeValueAsString(item))
            .consumeWith(System.out::println);
    }

    @Test
    public void updateItem_withValidRequest_ExpectedOkHttpResponseStatus() throws Exception {
        var newItem = ItemDto.builder().name("item").price(BigDecimal.valueOf(100.0)).build();
        given(itemFacade.updateItem(eq(ITEM_ID), eq(newItem))).willReturn(item);

        webClient.put().uri(API_VERSION_PREFIX_V1 + "/items/" + ITEM_ID)
            .body(BodyInserters.fromValue(newItem))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(objectMapper.writeValueAsString(item))
            .consumeWith(System.out::println);
    }

    @Test
    public void deleteItem_ExistingItem_ExpectedOkHttpResponseStatusAndItemJsonResponse() throws Exception {
        webClient.delete().uri(API_VERSION_PREFIX_V1 + "/items/" + ITEM_ID)
            .exchange()
            .expectStatus().isOk();

        verify(itemFacade).deleteItem(eq(ITEM_ID));
    }
}
