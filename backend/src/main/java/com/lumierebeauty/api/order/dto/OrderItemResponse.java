package com.lumierebeauty.api.order.dto;

import java.math.BigDecimal;

/** One order line as returned by the API. */
public record OrderItemResponse(
        String productId,
        String productName,
        BigDecimal price,
        int quantity
) {
}
