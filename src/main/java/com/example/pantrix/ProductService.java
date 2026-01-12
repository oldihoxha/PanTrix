package com.example.pantrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public Product save(Product product) {
        return productRepository.save(product);
    }
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
    }

    public List<Product> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false).toList();
    }

}
