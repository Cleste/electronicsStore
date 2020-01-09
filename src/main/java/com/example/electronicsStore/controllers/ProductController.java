package com.example.electronicsStore.controllers;


import com.example.electronicsStore.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.electronicsStore.services.ProductService;

import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable String id
    ) {
        if (id != null && !id.isEmpty()) {
            return ResponseEntity.ok(productService.findProductById(UUID.fromString(id)));
        } else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(productService.findAll(PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<Product> createBook(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> changeEdition(
            @PathVariable String id,
            @RequestBody Product product
    ) {
        if (product != null) {
            product.setId(UUID.fromString(id));
            return ResponseEntity.ok(productService.updateProduct(product));
        } else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<?> markRead(
            @PathVariable("id") String id,
            @RequestParam Integer quantity
    ) {
        if (id != null && !id.isEmpty()) {
            return ResponseEntity.ok(productService.updateQuantity(quantity, id));
        } else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Page<Product>> findByPhrase(@PathVariable String name,
                                                   @RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size
    ) {
        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(productService.findProductByNameContaining(name, PageRequest.of(page, size)));
        } else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}

