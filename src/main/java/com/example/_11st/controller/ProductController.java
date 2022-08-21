package com.example._11st.controller;


import com.example._11st.dto.Request.OrderReqDTO;
import com.example._11st.dto.Response.OrderRespDTO;
import com.example._11st.dto.Response.ProductRespDTO;
import com.example._11st.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products/{displayDate}")
    public List<ProductRespDTO.Inquiry> getProducts(@PathVariable String displayDate) {

        return productService.getProductDTO(displayDate);
    }

    @PostMapping("/order")
    public void orderProducts(@RequestHeader(value = "x-user-id") String userId,
                              @RequestBody @Valid OrderReqDTO.OrderInfo orderInfo) {

        productService.order(
                userId,
                orderInfo.getProductIds(),
                orderInfo.getOrderAmount(),
                orderInfo.getAddress(),
                orderInfo.getQuantity());
    }

    @DeleteMapping("/order/{orderId}")
    public void cancelOrder(@RequestHeader(value = "x-user-id") String userId,
                            @PathVariable Long orderId,
                            @RequestBody @Valid OrderReqDTO.Cancel cancel) {
        productService.cancel(userId, orderId, cancel.getCancelAmount());
    }

    @GetMapping("/history/period")
    public List<OrderRespDTO.History> getOrderHistoryByPeriod(@RequestHeader(value = "x-user-id") String userId,
                                                              @RequestBody @Valid OrderReqDTO.Period period) {
        return productService.getOrderHistoryByMonthPeriod(userId, period.getPeriod());
    }

    @GetMapping("/history/range")
    public List<OrderRespDTO.History> getOrderHistoryByBetweenStartAndEnd(@RequestHeader(value = "x-user-id") String userId,
                                                                          @RequestBody @Valid OrderReqDTO.BetweenDates betweenDates) {
        return productService.getOrderHistoryByBetweenStartAndEnd(userId, betweenDates.getStartAt(), betweenDates.getEndAt());
    }
}
