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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    @BeforeAll
    static void setup(@Autowired SellerRepository sellerRepository,
                      @Autowired ProductRepository productRepository) {

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
    void yyyy_mm_dd_hh_mm_ss_test() {
        final String PATTERN = "^([0-9]{4})-([0-1][0-9])-([0-3][0-9])\\s([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$";
        String displayDate = "2022-01-01 00:00:00";
        assertTrue(displayDate.matches(PATTERN));
        String displayDate2 = "2000-99-00 00:00:00";
        assertFalse(displayDate2.matches(PATTERN));
    }

    @DisplayName("order취소시 orderedProduct cascading돼서 delete 되는지")
    @Test
    void Should_CascadingDelete_When_CancelOrder() {
        final List<Long> productIds = new ArrayList<>(List.of(1L, 2L, 3L));
        final List<Long> quantity = new ArrayList<>(List.of(4L, 2L, 1L));
        final String address = "경기도 용인시 수지구";
        List<Order> orders = productService.order("greatpeople", productIds, 100000000000L, address, quantity);

        Long orderId = orders.get(0).getId();
        productService.cancel("greatpeople", orderId, 10000000L);
        List<OrderedProduct> orderedProducts = orderedProductRepository.findAllByOrderId(orderId);
        assertEquals(0, orderedProducts.size());
    }

    @DisplayName("본인 혹은 운영자가 아닌 사람이 주문취소할 때")
    @Test
    void Should_ThrowException_WhenStrangerTryToDelete_Order() {
        final List<Long> productIds = new ArrayList<>(List.of(1L, 2L, 3L));
        final List<Long> quantity = new ArrayList<>(List.of(4L, 2L, 1L));
        final String address = "경기도 용인시 수지구";
        List<Order> orders = productService.order("greatpeople", productIds, 100000000000L, address, quantity);

        Long orderId = orders.get(0).getId();

        Throwable ex = assertThrows(IllegalArgumentException.class, () ->
                productService.cancel("STRANGER-PK", orderId, 10000000L)
        );
        assertEquals("본인이 아니면 취소할 수 없습니다.", ex.getMessage());
    }

    @DisplayName("운영자가 주문 삭제")
    @Test
    void Should_Delete_WhenAdminTryDelete() {
        final List<Long> productIds = new ArrayList<>(List.of(1L, 2L, 3L));
        final List<Long> quantity = new ArrayList<>(List.of(4L, 2L, 1L));
        final String address = "경기도 용인시 수지구";

        // 일반인이 주문
        List<Order> orders = productService.order("greatpeople", productIds, 100000000000L, address, quantity);

        Long orderId = orders.get(0).getId();

        // 어드민이 삭제
        productService.cancel("admin's primary-key", orderId, 10000000L);
    }

    @DisplayName("부분 취소")
    @Test
    void Should_Delete_PartOfOrder() {
        final List<Long> productIds = new ArrayList<>(List.of(1L, 2L));
        final List<Long> quantity = new ArrayList<>(List.of(1L, 1L));
        final String address = "경기도 용인시 수지구";
        List<Order> orders = productService.order("greatpeople", productIds, 100000000000L, address, quantity);

        Long orderId = orders.get(0).getId();

        productService.cancel("greatpeople", orderId, 20000L);

        List<OrderedProduct> orderedProducts = orderedProductRepository.findAllByOrderId(orderId);
        assertEquals(1, orderedProducts.size());
    }

    @DisplayName("전체 취소")
    @Test
    void Should_Delete_AllOfOrder() {
        final List<Long> productIds = new ArrayList<>(List.of(1L, 2L));
        final List<Long> quantity = new ArrayList<>(List.of(1L, 1L));
        final String address = "경기도 용인시 수지구";
        List<Order> orders = productService.order("greatpeople", productIds, 100000000000L, address, quantity);

        Long orderId = orders.get(0).getId();

        productService.cancel("greatpeople", orderId, 30000L);

        List<OrderedProduct> orderedProducts = orderedProductRepository.findAllByOrderId(orderId);
        assertEquals(0, orderedProducts.size());
    }

    @DisplayName("재고 부족")
    @Test
    void Should_ThrowException_When_OutOfStock() {
        final List<Long> productIds = new ArrayList<>(List.of(1L));
        final List<Long> quantity = new ArrayList<>(List.of(2000000L));
        final String address = "경기도 용인시 수지구";

        assertThrows(IllegalArgumentException.class, () ->
                productService.order("greatpeople", productIds, 100000000000L, address, quantity)
        );
    }

    @DisplayName("주문금액이 부족")
    @Test
    void Should_ThrowException_WhenOrderAmountIs_Insufficient() {
        final List<Long> productIds = new ArrayList<>(List.of(1L, 2L, 3L));
        final List<Long> quantity = new ArrayList<>(List.of(1L, 1L, 1L));
        final String address = "경기도 용인시 수지구";

        assertThrows(IllegalArgumentException.class, () ->
                productService.order("greatpeople", productIds, 30000L, address, quantity)
        );
    }

}
