package com.example.electronicsStore.services.implementations;

import com.example.electronicsStore.domain.Product;
import com.example.electronicsStore.exceptions.InvalidSearchRequestException;
import com.example.electronicsStore.exceptions.ProductAlreadyExistException;
import com.example.electronicsStore.exceptions.ProductNotFoundException;
import com.example.electronicsStore.repositories.ProductRepository;
import com.example.electronicsStore.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        if(product != null){
            if(productRepository.findByName(product.getName()) == null){
                productRepository.save(product);
            } else throw new ProductAlreadyExistException();
            return product;
        } else throw new ProductNotFoundException();

    }

    @Override
    public Product updateProduct(Product newProduct) {
        Product currentProduct = productRepository.findProductById(newProduct.getId());
        if (currentProduct != null) {
            if(newProduct.getQuantity() != null)
                currentProduct.setQuantity(newProduct.getQuantity());
            if(newProduct.getDescription() != null)
                currentProduct.setDescription(newProduct.getDescription());
            if(newProduct.getName() != null)
                currentProduct.setName(newProduct.getName());
            if(newProduct.getPrice() != null)
                currentProduct.setPrice(newProduct.getPrice());
            productRepository.save(currentProduct);
            return currentProduct;
        } else throw new ProductNotFoundException();
    }

    @Override
    public Product updateQuantity(Integer quantity, String uuid) {
        Product product = productRepository.findProductById(UUID.fromString(uuid));
        if (product != null) {
            if(quantity != null)
                product.setQuantity(quantity);
            productRepository.save(product);
            return product;
        } else throw new ProductNotFoundException();
    }

    @Override
    public Product findProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public Page<Product> findProductByNameContaining(String name, Pageable pageable) {
        if (name != null && !name.isEmpty())
            return productRepository.findByNameContaining(name, pageable);
        else throw new InvalidSearchRequestException();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
