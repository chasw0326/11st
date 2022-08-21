package com.example._11st.serviceTest;


import com.example._11st.domain.*;
import com.example._11st.dto.Response.OrderRespDTO;
import com.example._11st.dto.Response.ProductRespDTO;
import com.example._11st.repository.OrderedProductRepository;
import com.example._11st.repository.ProductRepository;
import com.example._11st.repository.SellerRepository;
import com.example._11st.service.ProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    @BeforeAll
    public void setup(@Autowired SellerRepository sellerRepository) {

        final Seller SAMSUNG = Seller.builder()
                .name("삼성전자")
                .build();

        final Seller GUCCI = Seller.builder()
                .name("구찌")
                .build();

        final Seller CHANEL = Seller.builder()
                .name("샤넬")
                .build();

        sellerRepository.save(SAMSUNG);
        sellerRepository.save(GUCCI);
        sellerRepository.save(CHANEL);

        // 1L
        productRepository.save(Product.builder()
                .productState(ProductState.SALE)
                .seller(SAMSUNG)
                .name("ddr4")
                .price(20000L)
                .stock(100000L)
                .displayStartAt(LocalDateTime.now().minusMonths(2))
                .displayEndAt(LocalDateTime.now().plusMonths(2))
                .build());

        // 2L
        productRepository.save(Product.builder()
                .productState(ProductState.SALE)
                .seller(SAMSUNG)
                .name("ddr3")
                .price(10000L)
                .stock(20000L)
                .displayStartAt(LocalDateTime.now().minusMonths(2))
                .displayEndAt(LocalDateTime.now().plusMonths(2))
                .build());

        // 3L
        productRepository.save(Product.builder()
                .productState(ProductState.SALE)
                .seller(SAMSUNG)
                .name("ddr5")
                .price(40000L)
                .stock(300L)
                .displayStartAt(LocalDateTime.now().minusMonths(2))
                .displayEndAt(LocalDateTime.now().plusMonths(2))
                .build());

        // 4L
        productRepository.save(Product.builder()
                .productState(ProductState.SALE)
                .seller(GUCCI)
                .name("핸드백")
                .price(20000000L)
                .stock(10L)
                .displayStartAt(LocalDateTime.now().minusMonths(2))
                .displayEndAt(LocalDateTime.now().plusMonths(2))
                .build());

        // 5L
        productRepository.save(Product.builder()
                .productState(ProductState.SALE)
                .seller(GUCCI)
                .name("코트")
                .price(45000000L)
                .stock(10000L)
                .displayStartAt(LocalDateTime.now().minusMonths(2))
                .displayEndAt(LocalDateTime.now().plusMonths(2))
                .build());

        // 6L
        productRepository.save(Product.builder()
                .productState(ProductState.SALE)
                .seller(CHANEL)
                .name("향수")
                .price(80000L)
                .stock(3000L)
                .displayStartAt(LocalDateTime.now().minusMonths(2))
                .displayEndAt(LocalDateTime.now().plusMonths(2))
                .build());

        // 7L
        productRepository.save(Product.builder()
                .productState(ProductState.SALE)
                .seller(CHANEL)
                .name("반지")
                .price(500000L)
                .stock(100L)
                .displayStartAt(LocalDateTime.now().minusMonths(2))
                .displayEndAt(LocalDateTime.now().plusMonths(2))
                .build());

        // 8L
        productRepository.save(Product.builder()
                .productState(ProductState.STOP)
                .seller(CHANEL)
                .name("2021년도 한정판 티셔츠(판매중지)")
                .price(500000L)
                .stock(100L)
                .displayStartAt(LocalDateTime.now().minusMonths(2))
                .displayEndAt(LocalDateTime.now().plusMonths(2))
                .build());

        // 9L
        productRepository.save(Product.builder()
                .productState(ProductState.SALE)
                .seller(CHANEL)
                .name("2021년도 한정판 티셔츠(10개)")
                .price(500000L)
                .stock(10L)
                .displayStartAt(LocalDateTime.now().minusMonths(2))
                .displayEndAt(LocalDateTime.now().plusMonths(2))
                .build());
    }

    @Test
    void Should_Get_OneProduct_When_NormalArgs() {
        Product product = productService.getProduct(1L);
        assertEquals(1L, product.getId());
        assertEquals("ddr4", product.getName());
    }

    @Test
    void Should_Get_ProductDTO_When_NormalArsgs() {
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());
        String day = String.valueOf(now.getDayOfMonth());

        String displayDate = year + "-" + month + "-" + day + " 00:00:00";
        List<ProductRespDTO.Inquiry> productDTO = productService.getProductDTO(displayDate);

        assertTrue(productDTO.size() > 0);
    }

    @Test
    void Should_Return_Nothing_When_No_Suitable_Product_In_DisplayDate() {
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear() - 1000);
        String month = String.format("%02d", now.getMonthValue());
        String day = String.valueOf(now.getDayOfMonth());

        String displayDate = year + "-" + month + "-" + day + " 00:00:00";
        List<ProductRespDTO.Inquiry> productDTO = productService.getProductDTO(displayDate);

        assertEquals(0, productDTO.size());
    }

    @DisplayName("상품 판매자가 한명인 주문은 한개의 주문 생성")
    @Test
    void Should_Make_One_Order_When_OneSeller() {
        final List<Long> productIds = new ArrayList<>(List.of(1L, 2L, 3L));
        final List<Long> quantity = new ArrayList<>(List.of(400L, 200L, 100L));
        final String address = "경기도 용인시 수지구";
        List<Order> orders = productService.order("greatpeople", productIds, 100000000000L, address, quantity);

        assertEquals(address, orders.get(0).getAddress());

        List<List<OrderedProduct>> orderedProducts =
                orders.stream().map(order -> orderedProductRepository.findAllByOrderId(order.getId())).collect(Collectors.toList());

        int i = 0;
        for (List<OrderedProduct> orderedProduct : orderedProducts) {
            for (OrderedProduct product : orderedProduct) {
                assertEquals(productIds.get(i), product.getId());
                i++;
            }
        }

    }

    // 3명의 판매자
    @DisplayName("상품 판매자가 N명이면 N개 주문 생성")
    @Test
    void Should_Make_N_Order_When_N_Sellers() {
        final List<Long> productIds = new ArrayList<>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
        final List<Long> quantity = new ArrayList<>(List.of(400L, 200L, 100L, 1L, 3L, 1L, 1L));
        final String address = "경기도 용인시 수지구";
        List<Order> orders = productService.order("greatpeople", productIds, 100000000000L, address, quantity);

        // 판매자가 3명이면 주문 3개 생성
        assertEquals(3, orders.size());
        assertEquals(address, orders.get(0).getAddress());

        List<List<OrderedProduct>> orderedProducts =
                orders.stream().map(order -> orderedProductRepository.findAllByOrderId(order.getId())).collect(Collectors.toList());

        int i = 0;
        for (List<OrderedProduct> orderedProduct : orderedProducts) {
            for (OrderedProduct product : orderedProduct) {
                assertEquals(productIds.get(i), product.getId());
                i++;
            }
        }
    }


    @DisplayName("판매중지된 상품 구매 시 예외 던짐")
    @Test
    void Should_ThrowException_When_Product_Is_Stop_Selling() {
        final List<Long> productIds = new ArrayList<>(List.of(8L));
        final List<Long> quantity = new ArrayList<>(List.of(1L));
        final String address = "경기도 용인시 수지구";

        Throwable ex = assertThrows(IllegalArgumentException.class, () ->
                productService.order("greatpeople", productIds, 100000000000L, address, quantity)
        );
        assertEquals("상품 판매가 중지된 상품입니다.", ex.getMessage());
    }

    @DisplayName("주문수량이 재고보다 많으면 예외 던짐")
    @Test
    void Should_ThrowException_When_OrderAmount_IsGreater_Than_Stock() {
        final List<Long> productIds = new ArrayList<>(List.of(9L));
        final List<Long> quantity = new ArrayList<>(List.of(1000L));
        final String address = "경기도 용인시 수지구";

        Throwable ex = assertThrows(IllegalArgumentException.class, () ->
                productService.order("greatpeople", productIds, 100000000000L, address, quantity)
        );
        assertEquals("out of stock", ex.getMessage());
    }

    @DisplayName("시작일, 종료일기준 주문내역 구하기")
    @Test
    void Should_Return_OrderHistoryByBetweenStartAndEnd() {

        // given
        final List<Long> productIds = new ArrayList<>(List.of(1L, 2L, 3L, 4L, 5L));
        final List<Long> quantity = new ArrayList<>(List.of(400L, 200L, 100L, 1L, 3L));
        final String address = "경기도 용인시 수지구";
        List<Order> orders = productService.order("greatpeople", productIds, 100000000000L, address, quantity);
        String startAt = "2000-01-01 01:01:01";
        String endAt = "3000-01-01 01:01:01";

        // when
        List<OrderRespDTO.History> histories = productService.getOrderHistoryByBetweenStartAndEnd("greatpeople", startAt, endAt);

        assertEquals(2, histories.size());
    }

    @DisplayName("특정기간 ex)3개월내 주문기록 구하기")
    @Test
    void Should_Return_OrderHistory_ByPeriod() {
        final List<Long> productIds = new ArrayList<>(List.of(1L, 2L, 3L));
        final List<Long> quantity = new ArrayList<>(List.of(400L, 200L, 100L));
        final String address = "경기도 용인시 수지구";
        List<Order> orders = productService.order("greatpeople", productIds, 100000000000L, address, quantity);

        int monthPeriod = 3;
        List<OrderRespDTO.History> histories = productService.getOrderHistoryByMonthPeriod("greatpeople", monthPeriod);
        assertEquals(1, histories.size());

        monthPeriod = 0;
        histories = productService.getOrderHistoryByMonthPeriod("greatpeople", monthPeriod);
        assertEquals(0, histories.size());
    }

    @Test
    void ex(){
        final String PATTERN = "([0-9]{4})-([0-1][0-9])-([0-3][0-9])\\s([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$";
        System.out.println(PATTERN.matches("2022-01-01 00:00:00"));
    }
}
