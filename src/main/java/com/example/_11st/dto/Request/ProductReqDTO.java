package com.example._11st.dto.Request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductReqDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisplayDate {

        @NotNull
        @Pattern(regexp = "^([0-9]{4})-([0-1][0-9])-([0-3][0-9])\\s([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$",
                message = "YYYY-MM-DD HH:MM:SS 형식으로 입력해주세요 ex) 2022-08-21 13:00:00")
        private String displayDate;
    }
}
