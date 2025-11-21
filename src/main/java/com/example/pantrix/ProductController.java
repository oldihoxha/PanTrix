package com.example.pantrix;

import java.time.LocalDate;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ProductController {


    @GetMapping("/test")
    public List<Product> getProducts() {
        return List.of(
                new Product("Milch", LocalDate.of(2025, 10, 20)),
                new Product("Pita Brot Rewe Feiner Welt", LocalDate.of(2025, 10, 25)),
                new Product("Wudy Bratwurst", LocalDate.of(2025, 11, 5))
        );
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {

        return product;
    }

}
