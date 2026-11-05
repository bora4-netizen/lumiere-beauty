package com.lumierebeauty.api.order.dto;

import com.lumierebeauty.api.order.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/** Full order as returned by the API. */
public record OrderResponse(
        Long id,
        String status,
        String customerName,
        String customerEmail,
        String customerPhone,
        String address,
        String city,
        String paymentMethod,
        BigDecimal total,
        Instant createdAt,
        Long userId,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getCustomerPhone(),
                order.getAddress(),
                order.getCity(),
                order.getPaymentMethod(),
                order.getTotal(),
                order.getCreatedAt(),
                order.getUser().getId(),
                order.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getProductId(),
                                item.getProductName(),
                                item.getPrice(),
                                item.getQuantity()))
                        .toList());
    }
}
