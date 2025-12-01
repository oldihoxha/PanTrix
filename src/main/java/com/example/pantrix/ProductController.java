package com.example.pantrix;

import java.time.LocalDate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;



    @GetMapping("/test/{id}")
    public Product getProduct(@PathVariable String id) {
        Long productId = Long.parseLong(id);
        return productService.findById(productId);
    }

    @PostMapping("/test")
    public Product addProduct(@RequestBody Product product) {

        return productService.save(product);
    }

}
