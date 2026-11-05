package com.lumierebeauty.api.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Body of POST /api/products and PUT /api/products/{id}.
 * All fields the admin product form sends are supported here.
 */
public record ProductRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 150, message = "Name must be at most 150 characters")
        String name,

        @NotBlank(message = "Brand is required")
        @Size(max = 100, message = "Brand must be at most 100 characters")
        String brand,

        @NotBlank(message = "Category is required")
        @Size(max = 100, message = "Category must be at most 100 characters")
        String category,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description,

        @Size(max = 500, message = "Image URL must be at most 500 characters")
        String imageUrl,

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stock,

        @Pattern(regexp = "Active|Draft", flags = Pattern.Flag.CASE_INSENSITIVE,
                 message = "Status must be Active or Draft")
        String status,

        Boolean featured
) {
}
