package com.lumierebeauty.api.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * GET /api/brands — the distinct brand names in the catalogue.
 * The frontend turns this into shop filters; "All" is a frontend-only option.
 */
@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final ProductRepository productRepository;

    public BrandController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<String> listBrands() {
        return productRepository.findDistinctBrands();
    }
}
