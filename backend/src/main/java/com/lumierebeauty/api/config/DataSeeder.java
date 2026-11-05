package com.lumierebeauty.api.config;

import com.lumierebeauty.api.product.Product;
import com.lumierebeauty.api.product.ProductRepository;
import com.lumierebeauty.api.user.Role;
import com.lumierebeauty.api.user.User;
import com.lumierebeauty.api.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds the in-memory database with the same demo catalogue the frontend
 * used to keep in localStorage (src/data/products.js), plus two demo
 * accounts so login works out of the box:
 *
 *   Admin:    admin@lumiere.com / Admin123!
 *   Customer: customer@lumiere.com / Customer123!
 */
@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(ProductRepository productRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedProducts();
        seedUsers();
    }

    private void seedProducts() {
        if (productRepository.count() > 0) {
            return;
        }
        List<Product> products = List.of(
                product("P-1001", "Capture Youth Glow Booster Serum", "Dior", "Skincare",
                        "92.00", 18, Product.STATUS_ACTIVE, true, "/images/serum.svg",
                        "Sample luxury skincare serum listing for the cosmetics shop catalogue."),
                product("P-1002", "Soft Pinch Liquid Blush", "Rare Beauty", "Makeup",
                        "25.00", 34, Product.STATUS_ACTIVE, true, "/images/blush.svg",
                        "Sample cream blush listing with a lightweight, blendable finish."),
                product("P-1003", "Pro Filt'r Soft Matte Foundation", "Fenty Beauty", "Makeup",
                        "40.00", 22, Product.STATUS_ACTIVE, true, "/images/foundation.svg",
                        "Sample foundation listing for exploring shade and stock management."),
                product("P-1004", "Rouge Dior Lipstick", "Dior", "Makeup",
                        "46.00", 15, Product.STATUS_ACTIVE, false, "/images/lipstick.svg",
                        "Sample lip colour listing in the makeup collection."),
                product("P-1005", "Niacinamide 10% + Zinc 1%", "The Ordinary", "Skincare",
                        "14.00", 50, Product.STATUS_ACTIVE, false, "/images/serum.svg",
                        "Sample active-skincare product listing."),
                product("P-1006", "Hydrating Facial Cleanser", "CeraVe", "Skincare",
                        "18.00", 29, Product.STATUS_ACTIVE, false, "/images/cleanser.svg",
                        "Sample gentle facial cleanser listing."),
                product("P-1007", "Gloss Bomb Lip Gloss", "Fenty Beauty", "Makeup",
                        "23.00", 0, Product.STATUS_DRAFT, false, "/images/lipstick.svg",
                        "Sample lip gloss listing, currently saved as a draft."),
                product("P-1008", "Miss Dior Eau de Parfum", "Dior", "Fragrance",
                        "115.00", 8, Product.STATUS_ACTIVE, false, "/images/fragrance.svg",
                        "Sample fragrance listing for the beauty catalogue."),
                product("P-1009", "Relief Sun: Rice + Probiotics SPF50+", "Beauty of Joseon", "Skincare",
                        "19.00", 41, Product.STATUS_ACTIVE, true, "/images/sunscreen.svg",
                        "Sample daily sunscreen listing."),
                product("P-1010", "Lip Sleeping Mask", "Laneige", "Skincare",
                        "24.00", 26, Product.STATUS_ACTIVE, false, "/images/cream.svg",
                        "Sample overnight lip-care product listing."));
        productRepository.saveAll(products);
        log.info("Seeded {} demo products.", products.size());
    }

    private Product product(String id, String name, String brand, String category,
                            String price, int stock, String status, boolean featured,
                            String imageUrl, String description) {
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setBrand(brand);
        p.setCategory(category);
        p.setPrice(new BigDecimal(price));
        p.setStock(stock);
        p.setStatus(status);
        p.setFeatured(featured);
        p.setImageUrl(imageUrl);
        p.setDescription(description);
        return p;
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            return;
        }
        userRepository.save(new User("Lumière Admin", "admin@lumiere.com",
                passwordEncoder.encode("Admin123!"), Role.ADMIN));
        userRepository.save(new User("Customer", "customer@lumiere.com",
                passwordEncoder.encode("Customer123!"), Role.CUSTOMER));
        log.info("Seeded demo accounts: admin@lumiere.com / Admin123! and customer@lumiere.com / Customer123!");
    }
}
