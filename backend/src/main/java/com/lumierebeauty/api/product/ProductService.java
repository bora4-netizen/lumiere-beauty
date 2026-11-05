package com.lumierebeauty.api.product;

import com.lumierebeauty.api.common.ResourceNotFoundException;
import com.lumierebeauty.api.product.dto.ProductRequest;
import com.lumierebeauty.api.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Business logic for the product catalogue.
 * Reads are public (browsing the shop is open), but creates, updates and
 * deletes are admin-only (checked in SecurityConfig).
 */
@Service
public class ProductService {

    private static final Pattern ID_SUFFIX = Pattern.compile("^P-(\\d+)$");

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findProducts(String brand, String category, String search,
                                              String status, Boolean featured) {
        return productRepository.search(brand, category, search, status, featured).stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(String id) {
        return ProductResponse.from(findProduct(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        product.setId(generateNextId());
        applyRequest(product, request);
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(String id, ProductRequest request) {
        Product product = findProduct(id);
        applyRequest(product, request);
        Product saved = productRepository.save(product);
        return ProductResponse.from(saved);
    }

    @Transactional
    public void delete(String id) {
        Product product = findProduct(id);
        productRepository.delete(product);
    }

    Product findProduct(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found."));
    }

    private void applyRequest(Product product, ProductRequest request) {
        product.setName(request.name().trim());
        product.setBrand(request.brand().trim());
        product.setCategory(request.category().trim());
        product.setPrice(request.price());
        product.setDescription(blankToEmpty(request.description()));
        product.setImageUrl(blankToEmpty(request.imageUrl()));
        product.setStock(request.stock());
        // Missing status defaults to Active; missing featured defaults to false.
        product.setStatus(request.status() == null ? Product.STATUS_ACTIVE : request.status());
        product.setFeatured(request.featured() != null && request.featured());
    }

    /**
     * Generates ids in the frontend's format, e.g. P-1011 once P-1001..P-1010 exist.
     */
    private String generateNextId() {
        int maxSuffix = 1000;
        for (String id : productRepository.findAllIds()) {
            Matcher matcher = ID_SUFFIX.matcher(id);
            if (matcher.matches()) {
                maxSuffix = Math.max(maxSuffix, Integer.parseInt(matcher.group(1)));
            }
        }
        return "P-" + String.format("%04d", maxSuffix + 1);
    }

    private String blankToEmpty(String value) {
        return value == null ? "" : value;
    }
}
