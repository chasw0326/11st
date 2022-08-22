package com.example._11st;

import com.example._11st.domain.Order;
import com.example._11st.domain.Product;
import com.example._11st.domain.ProductState;
import com.example._11st.domain.Seller;
import com.example._11st.repository.OrderedProductRepository;
import com.example._11st.repository.ProductRepository;
import com.example._11st.repository.SellerRepository;
import com.example._11st.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class InsertDummyDataByCommandLineRunner implements CommandLineRunner {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final OrderedProductRepository orderedProductRepository;
    private final ProductService productService;

    @Override
    public void run(String... args) throws Exception {

        this.insertSellers();

    }

    private void insertSellers() {

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

        final List<Long> productIds1 = new ArrayList<>(List.of(1L, 2L, 3L));
        final List<Long> quantity1 = new ArrayList<>(List.of(1L, 1L, 1L));
        final String address1 = "경기도 용인시 수지구";
        productService.order("greatpeople", productIds1, 1000000000000L, address1, quantity1);

        final List<Long> productIds2 = new ArrayList<>(List.of(4L, 5L, 6L, 7L));
        final List<Long> quantity2 = new ArrayList<>(List.of(3L, 5L, 4L, 2L));
        final String address2 = "경기도 용인시 수지구";
        productService.order("greatpeople", productIds2, 1000000000000L, address2, quantity2);

        final List<Long> productIds3 = new ArrayList<>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
        final List<Long> quantity3 = new ArrayList<>(List.of(1L, 1L, 1L, 1L, 1L, 1L, 1L));
        final String address3 = "경기도 용인시 수지구";
        productService.order("badpeople", productIds3, 1000000000000L, address3, quantity3);

        final List<Long> productIds4 = new ArrayList<>(List.of(1L, 2L, 3L));
        final List<Long> quantity4 = new ArrayList<>(List.of(3L, 1L, 2L));
        final String address4 = "경기도 용인시 수지구";
        productService.order("greatpeople", productIds4, 1000000000000L, address4, quantity4);

    }
}
