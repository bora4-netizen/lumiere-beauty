package com.lumierebeauty.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * REST API for the Lumière Beauty shop.
 *
 * Runs on port 8080 as {@code http://localhost:8080/api/...}, which is the
 * base URL the React frontend expects (see src/services/api.js).
 */
@SpringBootApplication
public class LumiereBeautyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LumiereBeautyApiApplication.class, args);
    }
}
