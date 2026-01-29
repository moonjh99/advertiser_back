package com.example.advertiser.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @Column(nullable=false)
    private Long productId;

    @Column(nullable=false)
    private int unitPrice;

    @Column(nullable=false)
    private int quantity;

    public OrderItem(Long productId, int unitPrice, int quantity) {
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    void attach(Order order) {
        this.order = order;
    }

    public int getAmount() {
        return unitPrice * quantity;
    }

}

