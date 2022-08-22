package com.example._11st.repository;

import com.example._11st.domain.Product;
import com.example._11st.dto.response.ProductRespDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("Select p.id, p.name, p.price, p.stock, p.seller.id, p.sellerName, p.productState " +
            "from Product p where :date >= p.displayStartAt and :date <= p.displayEndAt")
    List<ProductRespDTO.Inquiry> findAll(@Param("date") LocalDateTime date);

    List<Product> getAllByDisplayStartAtIsLessThanEqualAndDisplayEndAtIsGreaterThanEqual(LocalDateTime startAt, LocalDateTime endAt);


}
