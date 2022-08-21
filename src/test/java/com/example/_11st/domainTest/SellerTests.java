package com.example._11st.domainTest;

import com.example._11st.domain.Order;
import com.example._11st.domain.Product;
import com.example._11st.domain.ProductState;
import com.example._11st.domain.Seller;
import com.example._11st.repository.SellerRepository;
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
public class SellerTests {

    @Autowired
    private SellerRepository sellerRepository;

    @MockBean
    private Order order;

    @MockBean
    private Seller seller;

    @Test
    void Should_ThrowException_WhenTooLongName() {
        String testName = "" +
                "너무너무긴이름너무너무긴이름" +
                "너무너무긴이름너무너무긴이름" +
                "너무너무긴이름너무너무긴이름" +
                "너무너무긴이름너무너무긴이름" +
                "너무너무긴이름너무너무긴이름" +
                "너무너무긴이름너무너무긴이름";

        assertThrows(ConstraintViolationException.class, () ->
                sellerRepository.save(Seller.builder()
                        .name(testName)
                        .build()));
    }
}
