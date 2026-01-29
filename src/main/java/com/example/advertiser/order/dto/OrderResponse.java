package com.example.advertiser.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private Long userId;
    private int totalAmount;
    private Instant createdAt;
    private List<OrderLine> items;

    @Data
    @AllArgsConstructor
    public static class OrderLine {
        private Long productId;
        private int unitPrice;
        private int quantity;
        private int amount;
    }
}

