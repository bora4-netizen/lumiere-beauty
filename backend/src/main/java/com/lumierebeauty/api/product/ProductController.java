package com.lumierebeauty.api.product;

import com.lumierebeauty.api.product.dto.ProductRequest;
import com.lumierebeauty.api.product.dto.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Product catalogue endpoints.
 *
 * GET (browsing) is public. POST/PUT/DELETE are ADMIN-only — enforced by
 * SecurityConfig, not in this class.
 *
 * GET /api/products                         all products
 * GET /api/products?brand=Dior              filter by brand
 * GET /api/products?category=Makeup         filter by category
 * GET /api/products?search=lipstick         search in the name
 * GET /api/products?status=Draft            filter by status
 * GET /api/products?featured=true           only featured products
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> list(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean featured) {
        return productService.findProducts(brand, category, search, status, featured);
    }

    @GetMapping("/{id}")
    public ProductResponse detail(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable String id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        productService.delete(id);
    }
}
