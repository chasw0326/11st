package com.example._11st.dtoTest;

import com.example._11st.dto.request.OrderReqDTO;
import com.example._11st.util.ValidateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderReqDTOTests {

    @Autowired
    private ValidateUtil validateUtil;

    @DisplayName("주문금액이 음수")
    @Test
    void verify_orderInfo_orderAmount_validation(){
        OrderReqDTO.OrderInfo orderInfo = OrderReqDTO.OrderInfo.builder()
                .productIds(new ArrayList<>(List.of(1L,2L,3L)))
                .productIds(new ArrayList<>(List.of(1L,2L,3L)))
                .orderAmount(-3L)
                .address("용인시 수지구")
                .build();

        assertThrows(ConstraintViolationException.class, () ->
                validateUtil.validate(orderInfo)
        );
    }

    @DisplayName("주소가 null")
    @Test
    void verify_orderInfo_address_validation(){
        OrderReqDTO.OrderInfo orderInfo = OrderReqDTO.OrderInfo.builder()
                .productIds(new ArrayList<>(List.of(1L,2L,3L)))
                .productIds(new ArrayList<>(List.of(1L,2L,3L)))
                .orderAmount(300000L)
                .address(null)
                .build();

        assertThrows(ConstraintViolationException.class, () ->
                validateUtil.validate(orderInfo)
        );
    }

    @DisplayName("취소금액이 null")
    @Test
    void verify_cancel_cancelAmount_validation(){
        OrderReqDTO.Cancel cancel = new OrderReqDTO.Cancel(null);

        assertThrows(ConstraintViolationException.class, () ->
                validateUtil.validate(cancel)
        );
    }

    @DisplayName("취소금액이 음수")
    @Test
    void verify_cancel_cancelAmount_negative_validation(){
        OrderReqDTO.Cancel cancel = new OrderReqDTO.Cancel(-30000L);

        assertThrows(ConstraintViolationException.class, () ->
                validateUtil.validate(cancel)
        );
    }

    @DisplayName("기간이 음수")
    @Test
    void verify_Period_validation(){
        OrderReqDTO.Period period = new OrderReqDTO.Period(0);

        assertThrows(ConstraintViolationException.class, () ->
                validateUtil.validate(period)
        );
    }

    @DisplayName("날짜형식 틀릴때")
    @Test
    void verify_BetweenDate_validation(){
        OrderReqDTO.BetweenDates betweenDates = new OrderReqDTO.BetweenDates(
                "2022-01-01 00:00:00", "2022-01-01 00:00:99"
        );
        assertThrows(ConstraintViolationException.class, () ->
                validateUtil.validate(betweenDates)
        );
    }


}
