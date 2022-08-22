package com.example._11st.controller;

import com.example._11st.dto.request.OrderReqDTO;
import com.example._11st.dto.response.ProductRespDTO;
import com.example._11st.service.ProductService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProductControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService mockProductService;

    WebClient webClient = WebClient.create();

    @DisplayName("정상적인 상품목록 가져오기")
    @Test
    void Should_Return200_When_Get_Products() {

        // given
        List<ProductRespDTO.Inquiry> result = new ArrayList<>();
        result.add(ProductRespDTO.Inquiry.builder()
                .productId(1L)
                .build());
        String displayDate = "2022-09-09 00:00:00";
        given(mockProductService.getProductDTO(displayDate)).willReturn(result);

        webTestClient
                .get()
                .uri("/api/products/" + displayDate)
                .header("x-user-id", "greatpeople")
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("상품주문")
    @Test
    void Should_Return200_When_Order_Product() {
        OrderReqDTO.OrderInfo orderInfo = OrderReqDTO.OrderInfo.builder()
                .productIds(new ArrayList<>(List.of(1L, 2L, 3L)))
                .orderAmount(100000L)
                .address("용인시 수지구")
                .quantity(new ArrayList<>(List.of(1L, 1L, 1L)))
                .build();

        String userId = "greatpeople";
        given(mockProductService.order(
                userId,
                orderInfo.getProductIds(),
                orderInfo.getOrderAmount(),
                orderInfo.getAddress(),
                orderInfo.getQuantity())).willReturn(null);

        webTestClient
                .post()
                .uri("/api/order")
                .header("x-user-id", userId)
                .bodyValue(orderInfo)
                .exchange()
                .expectStatus().isOk();
    }


    @DisplayName("주문금액이 음수일때")
    @Test
    void Should_Return_BadRequest_When_OrderAmountIsNegative() throws JSONException {
        JSONArray productIds = new JSONArray();
        productIds.put(1);
        productIds.put(2);
        productIds.put(3);

        JSONArray quantity = new JSONArray();
        quantity.put(3);
        quantity.put(4);
        quantity.put(2);

        JSONObject body = new JSONObject()
                .put("orderAmount", -399)
                .put("productIds", productIds)
                .put("quantity", quantity)
                .put("address", "용인시");

        webTestClient
                .post()
                .uri("/api/order")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("x-user-id", "greatpeople")
                .bodyValue(body.toString())
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @DisplayName("배송지가 null일때")
    @Test
    void Should_Return_BadRequest_When_AddressIsNull() throws JSONException {
        JSONArray productIds = new JSONArray();
        productIds.put(1);
        productIds.put(2);
        productIds.put(3);

        JSONArray quantity = new JSONArray();
        quantity.put(3);
        quantity.put(4);
        quantity.put(2);

        JSONObject body = new JSONObject()
                .put("orderAmount", 1000000)
                .put("productIds", productIds)
                .put("quantity", quantity)
                .put("address", null);

        webTestClient
                .post()
                .uri("/api/order")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("x-user-id", "greatpeople")
                .bodyValue(body.toString())
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
