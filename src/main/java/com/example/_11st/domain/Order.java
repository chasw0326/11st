package com.example._11st.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends AuditingCreateUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원도메인을 구현한다면, FK로 할 것
    @Column(name = "user_id", nullable = false)
    @NotNull(message = "user_id는 필수 값입니다.")
    private String user;

    @Column(nullable = false)
    @NotNull(message = "주소지는 필수 값입니다.")
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Seller seller;

    @Column(name = "order_amount")
    @PositiveOrZero
    private Long orderAmount;

    @Builder
    private Order(String userId, String address, Seller seller) {
        this.user = userId;
        this.address = address;
        this.seller = seller;
        this.orderState = OrderState.결제완료;
        this.orderAmount = 0L;
    }

    public void updateOrderAmount(Long orderAmount) {
        this.orderAmount += orderAmount;
    }

}
