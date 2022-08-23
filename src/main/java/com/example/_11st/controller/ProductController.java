package com.example._11st.controller;


import com.example._11st.dto.request.OrderReqDTO;
import com.example._11st.dto.response.OrderRespDTO;
import com.example._11st.dto.response.ProductRespDTO;
import com.example._11st.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 깃허브 위키 참조
 */
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Cacheable(value = "products", key = "#displayDate", cacheManager = "cacheManager")
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

    @Cacheable(value = "historyByPeriod", key = "#period", cacheManager = "cacheManager")
    @GetMapping("/history/period/{period}")
    public List<OrderRespDTO.History> getOrderHistoryByPeriod(@RequestHeader(value = "x-user-id") String userId,
                                                              @PathVariable @Min(1) @Max(12) int period) {
        return productService.getOrderHistoryByMonthPeriod(userId, period);
    }

    @Cacheable(value = "historyByBetweenStartAndEnd", key = "#betweenDates", cacheManager = "cacheManager")
    @GetMapping("/history/range")
    public List<OrderRespDTO.History> getOrderHistoryByBetweenStartAndEnd(@RequestHeader(value = "x-user-id") String userId,
                                                                          @RequestBody @Valid OrderReqDTO.BetweenDates betweenDates) {
        return productService.getOrderHistoryByBetweenStartAndEnd(userId, betweenDates.getStartAt(), betweenDates.getEndAt());
    }

    @Cacheable(value = "historyByBetweenStartAndEnd", key = "{#startAt, #endAt}", cacheManager = "cacheManager")
    @GetMapping("/history/range/{startAt}/{endAt}")
    public List<OrderRespDTO.History> getOrderHistoryByBetweenStartAndEnd(@RequestHeader(value = "x-user-id") String userId,
                                                                          @PathVariable String startAt,
                                                                          @PathVariable String endAt) {
        return productService.getOrderHistoryByBetweenStartAndEnd(userId, startAt, endAt);
    }

    @DeleteMapping("/order/{orderId}")
    public void cancelOrder(@RequestHeader(value = "x-user-id") String userId,
                            @PathVariable Long orderId,
                            @RequestBody @Valid OrderReqDTO.Cancel cancel) {
        productService.cancel(userId, orderId, cancel.getCancelAmount());
    }
}
