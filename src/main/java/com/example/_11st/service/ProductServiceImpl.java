package com.example._11st.service;


import com.example._11st.domain.*;
import com.example._11st.dto.response.OrderRespDTO;
import com.example._11st.dto.response.ProductRespDTO;
import com.example._11st.repository.CancelRepository;
import com.example._11st.repository.OrderRepository;
import com.example._11st.repository.OrderedProductRepository;
import com.example._11st.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CancelRepository cancelRepository;
    private final OrderedProductRepository orderedProductRepository;
    private final SellerServiceImpl sellerService;

    private static final String ADMINISTRATOR = "admin's primary-key";


    @Override
    @Transactional(readOnly = true)
    public List<ProductRespDTO.Inquiry> getProductDTO(String displayDate) {
        checkArgument(StringUtils.isNotEmpty(displayDate), "displayDate는 필수값 입니다.");
        checkLocalDateTimeRegex(displayDate);

        LocalDateTime date = parse(displayDate);

        List<Product> products = productRepository.getAllByDisplayStartAtIsLessThanEqualAndDisplayEndAtIsGreaterThanEqual(date, date);

        return products.stream().map(ProductRespDTO.Inquiry::from).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProduct(Long productId) {
        checkArgument(ObjectUtils.isNotEmpty(productId), "productId는 필수값 입니다.");

        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("can not find product by productId: " + productId));
    }

    @Override
    @Transactional
    public List<Order> order(String userId, List<Long> productIds, Long amount, String address, List<Long> quantity) {
        checkArgument(ObjectUtils.isNotEmpty(userId), "userId는 필수값 입니다.");
        checkArgument(ObjectUtils.isNotEmpty(productIds), "productIds는 필수값 입니다.");
        checkArgument(StringUtils.isNotEmpty(address), "address는 필수값 입니다.");
        checkArgument(ObjectUtils.isNotEmpty(quantity), "quantity는 필수값 입니다.");

        Map<Long, List<Long>> sellerProductMap = new LinkedHashMap<>();
        List<Order> orders = new ArrayList<>();

        if (productIds.size() != quantity.size()) {
            throw new IllegalArgumentException("상품갯수랑 주문수량이 다릅니다.");
        }

        for (Long productId : productIds) {
            Product product = this.getProduct(productId);
            sellerProductMap.computeIfAbsent(product.getSeller().getId(), id -> new ArrayList<>()).add(productId);
        }

        int quantityIdx = 0;
        for (Map.Entry<Long, List<Long>> sellerProduct : sellerProductMap.entrySet()) {
            Long sellerId = sellerProduct.getKey();
            List<Long> productsBySeller = sellerProduct.getValue();
//        }
//        for (Long sellerId : sellerProductMap.keySet()) {
            Order order = Order.builder()
                    .userId(userId)
                    .seller(sellerService.getSeller(sellerId))
                    .address(address)
                    .build();
            orders.add(order);
            orderRepository.save(order);

            for (Long productId : productsBySeller) {

                Product product = this.getProduct(productId);

                amount -= product.getPrice();
                if (amount < 0) {
                    throw new IllegalArgumentException("주문금액이 부족합니다.");
                }

                if (product.getStock() - quantity.get(quantityIdx) < 0) {
                    throw new IllegalArgumentException("out of stock");
                }

                if (product.getProductState().equals(ProductState.STOP)) {
                    throw new IllegalArgumentException("상품 판매가 중지된 상품입니다.");
                }

                product.updateStock(quantity.get(quantityIdx));

                Long orderAmount = quantity.get(quantityIdx) * product.getPrice();

                OrderedProduct orderedProduct = OrderedProduct.builder()
                        .order(order)
                        .product(product)
                        .quantity(quantity.get(quantityIdx))
                        .amount(orderAmount)
                        .build();

                orderedProductRepository.save(orderedProduct);
                order.updateOrderAmount(orderAmount);
                quantityIdx += 1;
            }
        }

        return orders;
    }

    @Override
    @Transactional
    public Order cancel(String userId, Long orderId, Long price) {
        checkArgument(StringUtils.isNotEmpty(userId), "userId는 필수값 입니다.");
        checkArgument(ObjectUtils.isNotEmpty(orderId), "orderId는 필수값 입니다.");
        checkArgument(ObjectUtils.isNotEmpty(price), "price는 필수값 입니다.");

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("can not find order by orderId: " + orderId));

        if (!userId.equals(order.getUser()) && !userId.equals(ADMINISTRATOR)) {
            throw new IllegalArgumentException("본인이 아니면 취소할 수 없습니다.");
        }

        OrderState orderState = order.getOrderState();

        if (orderState.equals(OrderState.배송완료) || orderState.equals(OrderState.배송중)) {
            throw new IllegalArgumentException("주문 취소 불가");
            // 취소 가능한지 판매자가 확인하도록
        } else if (orderState.equals(OrderState.배송준비중)) {
            cancelRepository.save(Cancel.builder()
                    .order(order)
                    .seller(order.getSeller())
                    .build());
            return order;
        }

        List<OrderedProduct> orderedProducts = orderedProductRepository.findAllByOrderId(orderId);

        long orderAmount = orderedProducts.stream().map(orderedProduct -> orderedProduct.getProduct().getPrice()).mapToLong(i -> i).sum();
        if (orderAmount <= price) {
            for (OrderedProduct orderedProduct : orderedProducts) {
                Product product = orderedProduct.getProduct();
                product.updateStock(orderedProduct.getQuantity());
            }
            orderRepository.delete(order);
            return order;
        }

        for (OrderedProduct orderedProduct : orderedProducts) {
            Product product = orderedProduct.getProduct();
            Long partOfPrice = product.getPrice() * orderedProduct.getQuantity();
            if (partOfPrice.equals(price)) {
                orderedProductRepository.deleteByProductIdAndOrderId(product.getId(), orderedProduct.getOrder().getId());
                product.updateStock(orderedProduct.getQuantity());
            }
        }

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderRespDTO.History> getOrderHistoryByBetweenStartAndEnd(String userId, String startAt, String endAt) {
        checkArgument(ObjectUtils.isNotEmpty(userId), "userId는 필수값 입니다.");
        checkArgument(StringUtils.isNotEmpty(startAt), "startAt은 필수값 입니다.");
        checkArgument(StringUtils.isNotEmpty(endAt), "endAt은 필수값 입니다.");
        checkLocalDateTimeRegex(startAt);
        checkLocalDateTimeRegex(endAt);

        LocalDateTime fromDate = this.parse(startAt);
        LocalDateTime toDate = this.parse(endAt);

        List<OrderRespDTO.History> result = new ArrayList<>();
        List<Order> orders = orderRepository.findAllByUserAndCreatedAtIsGreaterThanEqualAndCreatedAtIsLessThanEqual(userId, fromDate, toDate);
        for (Order order : orders) {
            List<OrderedProduct> orderedProducts = orderedProductRepository.findAllByOrderId(order.getId());
            result.add(OrderRespDTO.History.from(order, orderedProducts.stream().map(OrderRespDTO.OrderProduct::from).collect(Collectors.toList())));
        }

        return result;
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderRespDTO.History> getOrderHistoryByMonthPeriod(String userId, int period) {
        checkArgument(ObjectUtils.isNotEmpty(userId), "userId는 필수값 입니다.");
        checkArgument(ObjectUtils.isNotEmpty(period), "period는 필수값 입니다.");

        if (period < 1 || period > 12) {
            throw new IllegalArgumentException("잘못된 기간입니다.");
        }

        LocalDateTime startAt = LocalDateTime.now().minusMonths(period);
        List<OrderRespDTO.History> result = new ArrayList<>();
        List<Order> orders = orderRepository.findAllByCreatedAtAfterAndUser(startAt, userId);

        for (Order order : orders) {
            List<OrderedProduct> orderedProducts = orderedProductRepository.findAllByOrderId(order.getId());
            result.add(OrderRespDTO.History.from(order, orderedProducts.stream().map(OrderRespDTO.OrderProduct::from).collect(Collectors.toList())));
        }

        return result;
    }

    private void checkLocalDateTimeRegex(String displayDate) {

        final String PATTERN = "^([0-9]{4})-([0-1][0-9])-([0-3][0-9])\\s([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$";

        if (!displayDate.matches(PATTERN)) {
            throw new IllegalArgumentException("잘못된 날짜 입니다.");
        }
    }

    private LocalDateTime parse(String date) {
        return LocalDateTime.parse(date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
