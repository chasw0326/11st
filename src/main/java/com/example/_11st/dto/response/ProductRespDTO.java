package com.example._11st.dto.response;

import com.example._11st.domain.Product;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductRespDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Inquiry {
        private Long productId;

        private String name;

        private Long price;

        private Long stock;

        private Long sellerId;

        private String sellerName;

        private String productState;

        public static ProductRespDTO.Inquiry from(Product product) {
            return Inquiry.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .sellerId(product.getSeller().getId())
                    .sellerName(product.getSellerName())
                    .productState(product.getProductState().toString())
                    .build();
        }
    }
}