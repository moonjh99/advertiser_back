package com.example.advertiser.order.service;


import com.example.advertiser.order.dto.CreateOrderRequest;
import com.example.advertiser.order.dto.OrderItemDto;
import com.example.advertiser.order.entity.Order;
import com.example.advertiser.order.entity.OrderItem;
import com.example.advertiser.order.repository.OrderRepository;
import com.example.advertiser.product.entity.Product;
import com.example.advertiser.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repo;
    private final ProductService productService;

    public Order create(Long userId, CreateOrderRequest req) {
        Order order = new Order(userId);

        for(OrderItemDto item :  req.getItems())
        {
            Product product = productService.get(item.getProductId());
            order.addItem(new OrderItem(product.getId(),product.getPrice(), item.getQuantity()));
        }

        return repo.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> listMyOrders(Long userId) {
        return repo.findWithItemsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Order get(Long orderId) {
        return repo.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));
    }
}
