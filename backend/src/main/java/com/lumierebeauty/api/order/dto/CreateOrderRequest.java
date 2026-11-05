package com.lumierebeauty.api.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/** Body of POST /api/orders, mirroring the checkout form fields. */
public record CreateOrderRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name must be at most 100 characters")
        String customerName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        String customerEmail,

        @NotBlank(message = "Phone number is required")
        @Size(max = 50, message = "Phone number must be at most 50 characters")
        String customerPhone,

        @NotBlank(message = "Delivery address is required")
        @Size(max = 300, message = "Delivery address must be at most 300 characters")
        String address,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must be at most 100 characters")
        String city,

        @NotBlank(message = "Payment method is required")
        @Size(max = 50, message = "Payment method must be at most 50 characters")
        String paymentMethod,

        @NotEmpty(message = "The order must contain at least one item")
        @Valid
        List<OrderItemRequest> items
) {
}
