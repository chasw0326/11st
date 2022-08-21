package com.example._11st.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sellers")
public class Seller extends AuditingCreateUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    @Length(max = 50, message = "셀러 이름은 50자 이하여야 합니다.")
    @NotBlank(message = "셀러 이름은 필수 값 입니다.")
    private String name;

    @Builder
    private Seller(String name){
        this.name = name;
    }
}

