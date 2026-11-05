package com.lumierebeauty.api.order;

import com.lumierebeauty.api.order.dto.CreateOrderRequest;
import com.lumierebeauty.api.order.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Customer order endpoints. Both require a signed-in user (JWT); the Bearer
 * token identifies whose order is created and whose history is returned.
 *
 * POST /api/orders     place an order from the checkout form
 * GET /api/orders/my   order history of the signed-in user
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(Authentication authentication, @Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(authentication.getName(), request);
    }

    @GetMapping("/my")
    public List<OrderResponse> myOrders(Authentication authentication) {
        return orderService.findOrdersForUser(authentication.getName());
    }
}
