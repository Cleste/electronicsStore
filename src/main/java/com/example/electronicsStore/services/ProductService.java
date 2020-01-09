package com.example.electronicsStore.services;

import com.example.electronicsStore.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ProductService {
    Product createProduct(Product product);

    Product updateProduct(Product product);

    Product updateQuantity(Integer quantity, String uuid);

    Product findProductById(UUID id);

    Page<Product> findProductByNameContaining(String name, Pageable pageable);

    Page<Product> findAll(Pageable pageable);
}
