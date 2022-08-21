package com.example._11st.controller;

import com.example._11st.domain.Product;
import com.example._11st.dto.Request.OrderReqDTO;
import com.example._11st.dto.Response.ProductRespDTO;
import com.example._11st.service.ProductService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService mockProductService;

    @DisplayName("정상적인 상품목록 가져오기")
    @Test
    void Should_Return200_When_Get_Products(){

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
    void Should_Return200_When_Order_Product(){
        OrderReqDTO.OrderInfo orderInfo = OrderReqDTO.OrderInfo.builder()
                .productIds(new ArrayList<>(List.of(1L, 2L, 3L)))
                .orderAmount(100000L)
                .address("용인시 수지구")
                .quantity(new ArrayList<>(List.of(1L, 1L, 1L)))
                .build();

        String userId = "greatpeople";
        doNothing().when(mockProductService).order(
                userId,
                orderInfo.getProductIds(),
                orderInfo.getOrderAmount(),
                orderInfo.getAddress(),
                orderInfo.getQuantity());

        webTestClient
                .post()
                .uri("/api/order")
                .header("x-user-id", userId)
                .bodyValue(orderInfo)
                .exchange()
                .expectStatus().isOk();
    }


}
