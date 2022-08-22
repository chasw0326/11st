package com.example._11st.domainTest;


import com.example._11st.domain.Product;
import com.example._11st.domain.ProductState;
import com.example._11st.domain.Seller;
import com.example._11st.repository.ProductRepository;
import com.example._11st.repository.SellerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductTests {

    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private Seller seller;

    @DisplayName("너무 긴 상품명")
    @Test
    void ProductName_Validation_Test() {
        String testName = "너무너무너무긴상품명너무너무너무긴상품명너무너" +
                "무너무긴상품명너무너무너무긴상품명" +
                "너무너무너무긴상품명너무너무너무긴상" +
                "품명너무너무너무긴상품명너무너무너무긴상품명" +
                "너무너무너무긴상품명";

    assertThrows(ConstraintViolationException.class, () ->
            productRepository.save(Product.builder()
                    .productState(ProductState.SALE)
                    .seller(seller)
                    .name(testName)
                    .price(20000L)
                    .stock(100000L)
                    .displayStartAt(LocalDateTime.now().minusMonths(2))
                    .displayEndAt(LocalDateTime.now().plusMonths(2))
                    .build()));
    }


}
