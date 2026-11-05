package com.lumierebeauty.api.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    /**
     * Filter list used by GET /api/products.
     * Passing null for a parameter skips that condition, e.g.
     * GET /api/products?brand=Dior&category=Makeup is supported.
     */
    @Query("""
            SELECT p FROM Product p
            WHERE (:brand IS NULL OR LOWER(p.brand) = LOWER(:brand))
              AND (:category IS NULL OR LOWER(p.category) = LOWER(:category))
              AND (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:status IS NULL OR LOWER(p.status) = LOWER(:status))
              AND (:featured IS NULL OR p.featured = :featured)
            ORDER BY p.id
            """)
    List<Product> search(@Param("brand") String brand,
                         @Param("category") String category,
                         @Param("search") String search,
                         @Param("status") String status,
                         @Param("featured") Boolean featured);

    /** Distinct brand names for GET /api/brands. */
    @Query("SELECT DISTINCT p.brand FROM Product p ORDER BY p.brand")
    List<String> findDistinctBrands();

    /** Existing product ids, used to generate the next P-xxxx id. */
    @Query("SELECT p.id FROM Product p")
    List<String> findAllIds();
}
