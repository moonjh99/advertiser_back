package com.example.advertiser.product.repository;

import com.example.advertiser.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}