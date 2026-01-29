package com.example.advertiser.product.controller;

import com.example.advertiser.common.ApiResponse;
import com.example.advertiser.product.dto.ProductResponse;
import com.example.advertiser.product.dto.ProductUpsertRequest;
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

    private final ProductService svc;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> list() {
        var data = svc.list().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getDescription()))
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> get(@PathVariable Long id) {
        var p = svc.get(id);
        return ResponseEntity.ok(ApiResponse.ok(new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getDescription())));
    }

    // 데모에서는 관리자 기능이지만 일단 제공(보호하려면 permitAll 제거)
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductUpsertRequest req) {
        var p = svc.create(req);
        return ResponseEntity.ok(ApiResponse.ok(new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getDescription())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(@PathVariable Long id, @Valid @RequestBody ProductUpsertRequest req) {
        var p = svc.update(id, req);
        return ResponseEntity.ok(ApiResponse.ok(new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getDescription())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }


}
