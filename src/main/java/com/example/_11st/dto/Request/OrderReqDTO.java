package com.example._11st.dto.Request;


import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderReqDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderInfo {

        private List<Long> productIds;

        private List<Long> quantity;

        private Long orderAmount;

        private String address;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cancel {

        private Long cancelAmount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Period {

        @NotNull
        private int period;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BetweenDates {

        @NotNull
        @Pattern(regexp = "^([0-9]{4})-([0-1][0-9])-([0-3][0-9])\\s([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$",
                message = "YYYY-MM-DD HH:MM:SS 형식으로 입력해주세요 ex) 2022-08-21 13:00:00")
        private String startAt;

        @NotNull
        @Pattern(regexp = "^([0-9]{4})-([0-1][0-9])-([0-3][0-9])\\s([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$",
                message = "YYYY-MM-DD HH:MM:SS 형식으로 입력해주세요 ex) 2022-08-21 13:00:00")
        private String endAt;
    }
}
