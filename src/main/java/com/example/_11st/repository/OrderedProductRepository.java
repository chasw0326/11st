package com.example._11st.repository;

import com.example._11st.domain.OrderedProduct;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {

    @EntityGraph(attributePaths = {"product"}, type = EntityGraph.EntityGraphType.LOAD)
    List<OrderedProduct> findAllByOrderId(Long orderId);

    void deleteByProductIdAndOrderId(Long productId, Long orderId);
}
