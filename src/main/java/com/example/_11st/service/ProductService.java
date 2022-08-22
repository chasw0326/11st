package com.example._11st.service;

import com.example._11st.domain.Order;
import com.example._11st.domain.Product;
import com.example._11st.dto.response.OrderRespDTO;
import com.example._11st.dto.response.ProductRespDTO;

import java.util.List;

public interface ProductService {

    List<ProductRespDTO.Inquiry> getProductDTO(String displayDate);

    Product getProduct(Long productId);

    List<Order> order(String userId, List<Long> productIds, Long amount, String address, List<Long> quantity);

    List<OrderRespDTO.History> getOrderHistoryByBetweenStartAndEnd(String userId, String startAt, String endAt);

    List<OrderRespDTO.History> getOrderHistoryByMonthPeriod(String userId, int period);

    Order cancel(String userId, Long orderId, Long price);

}
