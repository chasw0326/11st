package com.example._11st.dtoTest;

import com.example._11st.dto.request.ProductReqDTO;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductReqDTOTests {

    @Autowired
    private ValidateUtil validateUtil;

    @DisplayName("조회기간 유효성 체크")
    @Test
    void verify_displayDate_validation() {

        assertThrows(ConstraintViolationException.class, () ->
                validateUtil.validate(new ProductReqDTO.Date("2022-01-01 99:00:00"))
        );

        assertThrows(ConstraintViolationException.class, () ->
                validateUtil.validate(new ProductReqDTO.Date("2022-01-01 00:99:00"))
        );

        assertThrows(ConstraintViolationException.class, () ->
                validateUtil.validate(new ProductReqDTO.Date("2022-01-01 00:00:99"))
        );
    }
}
