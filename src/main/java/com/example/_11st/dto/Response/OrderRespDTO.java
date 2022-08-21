package com.example._11st.dto.Response;


import com.example._11st.domain.Order;
import com.example._11st.domain.OrderedProduct;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRespDTO {

    @Getter
    @Builder
    public static class History {

        private Long orderId;

        private String address;

        private List<OrderProduct> orderProducts;

        private Long orderAmount;

        public static OrderRespDTO.History from(Order order, List<OrderProduct> orderProducts) {
            return History.builder()
                    .orderId(order.getId())
                    .address(order.getAddress())
                    .orderAmount(order.getOrderAmount())
                    .orderProducts(orderProducts)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class OrderProduct {

        private String productName;

        private Long productPrice;

        private Long quantity;

        public static OrderRespDTO.OrderProduct from(OrderedProduct orderedProduct) {
            return OrderProduct.builder()
                    .productName(orderedProduct.getProduct().getName())
                    .quantity(orderedProduct.getQuantity())
                    .productPrice(orderedProduct.getProduct().getPrice())
                    .build();
        }
    }
}