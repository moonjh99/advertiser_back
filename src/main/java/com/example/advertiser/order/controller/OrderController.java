package com.example.advertiser.order.controller;

import com.example.advertiser.auth.jwt.JwtProvider;
import com.example.advertiser.common.ApiResponse;
import com.example.advertiser.order.dto.CreateOrderRequest;
import com.example.advertiser.order.dto.OrderResponse;
import com.example.advertiser.order.entity.Order;
import com.example.advertiser.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private Long requireUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalArgumentException("unauthorized");
        }
        return (Long) auth.getPrincipal();
    }


    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(Authentication auth, @RequestBody CreateOrderRequest req){
        Long userId = requireUserId(auth);
        Order order = orderService.create(userId, req);

        List<OrderResponse.OrderLine> lines = order.getItems().stream()
                .map(i -> new OrderResponse.OrderLine(i.getProductId(), i.getUnitPrice(), i.getQuantity(), i.getAmount()))
                .toList();

        return ResponseEntity.ok(ApiResponse.ok(
                new OrderResponse(order.getId(), order.getUserId(), order.getTotalAmount(), order.getCreatedAt(), lines)
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> myOrders(
            Authentication auth
    ) {
        Long userId = requireUserId(auth);
        List<OrderResponse> data = orderService.listMyOrders(userId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> get(@PathVariable Long id) {
        Order order = orderService.get(id);
        return ResponseEntity.ok(ApiResponse.ok(toResponse(order)));
    }


    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(i -> new OrderResponse.OrderLine(
                                i.getProductId(),
                                i.getUnitPrice(),
                                i.getQuantity(),
                                i.getAmount()
                        ))
                        .toList()
        );
    }
}
