package com.lumierebeauty.api.order;

import com.lumierebeauty.api.order.dto.OrderResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Admin area: GET /api/admin/orders — every order from every customer.
 * Requires the ADMIN role (SecurityConfig). Add more admin endpoints under
 * /api/admin/** as the shop grows (e.g. user management).
 */
@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponse> allOrders() {
        return orderService.findAllOrders();
    }
}
