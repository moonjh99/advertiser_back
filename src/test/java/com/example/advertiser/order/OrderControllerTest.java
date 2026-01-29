package com.example.advertiser.order;


import com.example.advertiser.auth.jwt.JwtProvider;
import com.example.advertiser.order.dto.CreateOrderRequest;
import com.example.advertiser.order.dto.OrderItemDto;
import com.example.advertiser.product.entity.Product;
import com.example.advertiser.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    ProductRepository productRepository;

    private String bearerToken() {
        return "Bearer "+jwtProvider.issueAccessToken(1L);
    }

    private Long productId;

    @BeforeEach
    void setUp(){
        Product product = new Product("무선 이어폰",32000,"테스트 상품");
        productRepository.save(product);
        productId = product.getId();
    }

    @Test
    void create_order_success() throws Exception{
        CreateOrderRequest req = new CreateOrderRequest();
        OrderItemDto item = new OrderItemDto();
        item.setProductId(productId);
        item.setQuantity(2);
        req.setItems(List.of(item));

        mockMvc.perform(
                post("/api/orders")
                        .header("Authorization",bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.totalAmount").value(64000))
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items[0].productId").value(productId))
                .andExpect(jsonPath("$.data.items[0].quantity").value(2))
                .andExpect(jsonPath("$.data.items[0].unitPrice").value(32000))
                .andExpect(jsonPath("$.data.items[0].amount").value(64000));
    }

    @Test
    void list_my_orders_success() throws Exception {
        createOrderForTest();

        mockMvc.perform(
                get("/api/orders/me")
                        .header("Authorization", bearerToken())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.data[0].userId").value(1));
    }

    @Test
    void get_order_success() throws Exception {
        Long orderId = createOrderForTest();

        mockMvc.perform(
                get("/api/orders/{id}",orderId)
                        .header("Authorization", bearerToken())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(orderId))
                .andExpect(jsonPath("$.data.items").isArray());
    }


    @Test
    void create_order_fail_without_token() throws Exception{
        CreateOrderRequest req = new CreateOrderRequest();

        mockMvc.perform(
                post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        )
        .andExpect(status().isUnauthorized());
    }


    private Long createOrderForTest() throws Exception {
        CreateOrderRequest req = new CreateOrderRequest();
        OrderItemDto item = new OrderItemDto();
        item.setProductId(productId);
        item.setQuantity(1);
        req.setItems(List.of(item));

        String response = mockMvc.perform(
                        post("/api/orders")
                                .header("Authorization", bearerToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                )
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response)
                .get("data")
                .get("id")
                .asLong();
    }

}
