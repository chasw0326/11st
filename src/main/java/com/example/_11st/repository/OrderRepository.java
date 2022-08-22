package com.example._11st.repository;

import com.example._11st.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"seller"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findAllByUserAndCreatedAtIsGreaterThanEqualAndCreatedAtIsLessThanEqual(String userId, LocalDateTime startAt, LocalDateTime endAt);


    @EntityGraph(attributePaths = {"seller"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findAllByCreatedAtAfterAndUser(LocalDateTime startAt, String user);
}
