package com.lumierebeauty.api.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /** Orders of one customer, including their items, newest first. */
    @Query("""
            SELECT DISTINCT o FROM Order o
            LEFT JOIN FETCH o.items
            WHERE o.user.id = :userId
            ORDER BY o.createdAt DESC
            """)
    List<Order> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    /** All orders (admin area), including their items, newest first. */
    @Query("""
            SELECT DISTINCT o FROM Order o
            LEFT JOIN FETCH o.items
            ORDER BY o.createdAt DESC
            """)
    List<Order> findAllOrderByCreatedAtDesc();
}
