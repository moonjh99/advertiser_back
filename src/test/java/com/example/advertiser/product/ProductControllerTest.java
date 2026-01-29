package com.example.advertiser.product;

import com.example.advertiser.auth.jwt.JwtProvider;
import com.example.advertiser.product.dto.ProductUpsertRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtProvider jwtProvider;

    private String bearerToken(){
        return "Bearer "+jwtProvider.issueAccessToken(1L);
    }

    @Test
    void list_products_success() throws Exception {
        mockMvc.perform(
                get("/api/products")
                        .header("Authorization",bearerToken())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }


    @Test
    void get_product_success() throws Exception {

        Long productId = createProduct("무선 이어폰", 32000, "테스트 상품");

        mockMvc.perform(
                get("/api/products/{id}",productId).header("Authorization", bearerToken())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(productId))
                .andExpect(jsonPath("$.data.name").value("무선 이어폰"));
    }


    @Test
    void create_product_success() throws Exception {
        ProductUpsertRequest req = new ProductUpsertRequest();
        req.setName("스마트 워치");
        req.setPrice(58000);
        req.setDescription("테스트용 스마트 워치");

        mockMvc.perform(
                        post("/api/products")
                                .header("Authorization", bearerToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.name").value("스마트 워치"))
                .andExpect(jsonPath("$.data.price").value(58000));
    }


    @Test
    void update_product_success() throws Exception {
        Long productId = createProduct("블루투스 스피커", 45000, "수정 전");

        ProductUpsertRequest req = new ProductUpsertRequest();
        req.setName("블루투스 스피커");
        req.setPrice(47000);
        req.setDescription("수정 후");

        mockMvc.perform(
                        put("/api/products/{id}", productId)
                                .header("Authorization", bearerToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.price").value(47000))
                .andExpect(jsonPath("$.data.description").value("수정 후"));
    }


    @Test
    void delete_product_success() throws Exception {
        Long productId = createProduct("삭제 상품", 10000, "삭제 테스트");

        mockMvc.perform(
                        delete("/api/products/{id}", productId)
                                .header("Authorization", bearerToken())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }


    private Long createProduct(String name, int price, String description) throws Exception {
        ProductUpsertRequest req = new ProductUpsertRequest();
        req.setName(name);
        req.setPrice(price);
        req.setDescription(description);

        String response = mockMvc.perform(
                        post("/api/products")
                                .header("Authorization", bearerToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                )
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper
                .readTree(response)
                .get("data")
                .get("id")
                .asLong();
    }
}
