package com.lumierebeauty.api.product.dto;

import com.lumierebeauty.api.product.Product;

import java.math.BigDecimal;
import java.time.Instant;

/** Product JSON returned by the API, with the same field names the frontend expects. */
public record ProductResponse(
        String id,
        String name,
        String brand,
        String category,
        BigDecimal price,
        String description,
        String imageUrl,
        int stock,
        String status,
        boolean featured,
        Instant createdAt
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getCategory(),
                product.getPrice(),
                product.getDescription(),
                product.getImageUrl(),
                product.getStock(),
                product.getStatus(),
                product.isFeatured(),
                product.getCreatedAt());
    }
}
