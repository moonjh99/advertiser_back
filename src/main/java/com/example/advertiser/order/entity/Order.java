package com.example.advertiser.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private Long userId;

    @Column(nullable=false)
    private int totalAmount;

    @Column(nullable=false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public Order(Long userId) {
        this.userId = userId;
        this.totalAmount = 0;
    }

    public void addItem(OrderItem item) {
        item.attach(this);
        items.add(item);
        totalAmount += item.getAmount();
    }

}

