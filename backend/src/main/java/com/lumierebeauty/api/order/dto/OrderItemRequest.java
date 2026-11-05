package com.lumierebeauty.api.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** One cart line in POST /api/orders. */
public record OrderItemRequest(
        @NotBlank(message = "ProductId is required") String productId,
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {
}
