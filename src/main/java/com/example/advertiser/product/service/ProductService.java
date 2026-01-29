package com.example.advertiser.product.service;

import com.example.advertiser.product.dto.ProductUpsertRequest;
import com.example.advertiser.product.entity.Product;
import com.example.advertiser.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repo;

    public List<Product> list() {
        return repo.findAll();
    }

    public Product get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("product not found"));
    }

    public Product create(ProductUpsertRequest req) {
        return repo.save(new Product(req.getName(), req.getPrice(), req.getDescription()));
    }

    public Product update(Long id, ProductUpsertRequest req) {
        var p = get(id);
        p.update(req.getName(), req.getPrice(), req.getDescription());
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
