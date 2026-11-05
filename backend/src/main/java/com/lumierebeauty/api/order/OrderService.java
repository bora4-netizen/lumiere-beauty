package com.lumierebeauty.api.order;

import com.lumierebeauty.api.common.InsufficientStockException;
import com.lumierebeauty.api.common.ProductUnavailableException;
import com.lumierebeauty.api.common.ResourceNotFoundException;
import com.lumierebeauty.api.order.dto.CreateOrderRequest;
import com.lumierebeauty.api.order.dto.OrderItemRequest;
import com.lumierebeauty.api.order.dto.OrderResponse;
import com.lumierebeauty.api.product.Product;
import com.lumierebeauty.api.product.ProductRepository;
import com.lumierebeauty.api.user.User;
import com.lumierebeauty.api.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Order lifecycle logic. When an order is created:
 *  1. every product must exist and be Active,
 *  2. stock is reduced immediately (documented decision in backend/README.md),
 *  3. prices are snapshotted into the order items.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findOrdersForUser(String email) {
        User user = findUser(email);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAllOrderByCreatedAtDesc().stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional
    public OrderResponse createOrder(String email, CreateOrderRequest request) {
        User user = findUser(email);

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(request.customerName().trim());
        order.setCustomerEmail(request.customerEmail().trim());
        order.setCustomerPhone(request.customerPhone().trim());
        order.setAddress(request.address().trim());
        order.setCity(request.city().trim());
        order.setPaymentMethod(request.paymentMethod().trim());
        order.setStatus(Order.STATUS_PENDING);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest line : request.items()) {
            Product product = productRepository.findById(line.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product " + line.productId() + " not found."));

            if (!Product.STATUS_ACTIVE.equalsIgnoreCase(product.getStatus())) {
                throw new ProductUnavailableException(
                        "Product '" + product.getName() + "' is not available for purchase.");
            }
            if (product.getStock() < line.quantity()) {
                throw new InsufficientStockException(
                        "Only " + product.getStock() + " left in stock for '" + product.getName() + "'.");
            }

            // Reduce stock now; prices are snapshotted so later changes don't rewrite history.
            product.setStock(product.getStock() - line.quantity());
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(line.quantity())));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(line.quantity());
            order.getItems().add(item);
        }
        order.setTotal(total);

        order.getUser().getEmail(); // user exists already; fetched lazily before saving
        return OrderResponse.from(orderRepository.save(order));
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No user for email " + email + " (token may have been revoked)."));
    }
}
