package com.example._11st.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cancels")
public class Cancel extends AuditingCreateUpdateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seller_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Seller seller;

    @NotNull
    private Boolean isCanceled;

    @Builder
    private Cancel(Order order, Seller seller){
        this.order = order;
        this.seller = seller;
        this.isCanceled = false;
    }
}
