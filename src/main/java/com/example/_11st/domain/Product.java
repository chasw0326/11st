package com.example._11st.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class Product extends AuditingCreateUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    @Length(max = 50, message = "상품명은 50자 이하여야 합니다.")
    @NotBlank(message = "상품명은 필수 값 입니다.")
    private String name;

    @NotNull(message = "가격은 필수 값 입니다.")
    @PositiveOrZero(message = "가격은 0이상이어야 합니다.")
    private Long price;

    @NotNull(message = "재고는 필수 값 입니다.")
    @PositiveOrZero(message = "재고는 0이상이어야 합니다.")
    private Long stock;

    @Column(name = "product_state")
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Seller seller;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "display_start_at")
    private LocalDateTime displayStartAt;

    @Column(name = "display_end_at")
    @FutureOrPresent
    private LocalDateTime displayEndAt;

    @Builder
    private Product(String name, Long price, Long stock, ProductState productState, Seller seller, LocalDateTime displayStartAt, LocalDateTime displayEndAt) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.productState = productState;
        this.sellerName = seller.getName();
        this.seller = seller;
        this.displayStartAt = displayStartAt;
        this.displayEndAt = displayEndAt;
    }

    public void updateStock(Long quantity) {
        this.stock -= quantity;
    }

    public void updateProductState(ProductState productState) {
        this.productState = productState;
    }
}
