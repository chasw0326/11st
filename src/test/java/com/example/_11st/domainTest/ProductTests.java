package com.example._11st.domainTest;


import com.example._11st.domain.Product;
import com.example._11st.domain.ProductState;
import com.example._11st.domain.Seller;
import com.example._11st.repository.ProductRepository;
import com.example._11st.repository.SellerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;


    @DisplayName("da")
    @Test
    void ProductName_Validation_Test() {
        String testName = "너무너무너무긴상품명너무너무너무긴상품명너무너" +
                "무너무긴상품명너무너무너무긴상품명" +
                "너무너무너무긴상품명너무너무너무긴상" +
                "품명너무너무너무긴상품명너무너무너무긴상품명" +
                "너무너무너무긴상품명";

        final Seller SAMSUNG = Seller.builder()
                .name("삼성전자")
                .build();

        sellerRepository.save(SAMSUNG);
    assertThrows(ConstraintViolationException.class, () ->
            productRepository.save(Product.builder()
                    .productState(ProductState.SALE)
                    .seller(SAMSUNG)
                    .name(testName)
                    .price(20000L)
                    .stock(100000L)
                    .displayStartAt(LocalDateTime.now().minusMonths(2))
                    .displayEndAt(LocalDateTime.now().plusMonths(2))
                    .build()));
    }
}
