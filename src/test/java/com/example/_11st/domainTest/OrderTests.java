package com.example._11st.domainTest;


import com.example._11st.domain.Order;
import com.example._11st.domain.Seller;
import com.example._11st.repository.OrderRepository;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderTests {

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private Seller seller;

    @Test
    void Should_ThrowException_When_OrderAmountIsNegative() {
        Order order = Order.builder()
                .seller(seller)
                .userId("greatpeople")
                .address("용인시 수지구")
                .build();
        order.updateOrderAmount(-3L);

        assertThrows(ConstraintViolationException.class, () ->
                orderRepository.save(order)
        );

    }
}
