package com.example.advertiser.product.controller;

import com.example.advertiser.common.ApiResponse;
import com.example.advertiser.product.dto.ProductResponse;
import com.example.advertiser.product.dto.ProductUpsertRequest;
import com.example.advertiser.product.entity.Product;
import com.example.advertiser.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> list() {
        List<ProductResponse> productList = productService.list().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getDescription()))
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(productList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> get(@PathVariable Long id) {
        Product product = productService.get(id);
        return ResponseEntity.ok(ApiResponse.ok(new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getDescription())));
    }

    // 데모에서는 관리자 기능이지만 일단 제공(보호하려면 permitAll 제거)
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductUpsertRequest req) {
        Product product = productService.create(req);
        return ResponseEntity.ok(ApiResponse.ok(new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getDescription())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(@PathVariable Long id, @Valid @RequestBody ProductUpsertRequest req) {
        Product product = productService.update(id, req);
        return ResponseEntity.ok(ApiResponse.ok(new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getDescription())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }


}
