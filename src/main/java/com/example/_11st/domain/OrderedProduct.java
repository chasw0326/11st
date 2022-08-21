package com.example._11st.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ordered_products")
public class OrderedProduct extends AuditingCreateUpdateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @NotNull(message = "주문수량은 필수 값입니다.")
    @PositiveOrZero(message = "주문수량은 0이상이어야 합니다.")
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @PositiveOrZero
    private Long amount;

    @Builder
    private OrderedProduct(Product product, Long quantity, Order order, Long amount){
        this.product = product;
        this.quantity = quantity;
        this.order = order;
        this.amount = amount;
    }
}
