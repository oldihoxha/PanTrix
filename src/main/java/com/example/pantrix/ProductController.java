package com.example.pantrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:5713", "http://localhost:8080",
             "https://pantrix.onrender.com", "https://pantrix-frontnd.onrender.com"},
             allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
             allowCredentials = "true")
public class ProductController {
    @Autowired
    ProductService productService;


    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        Long productId = Long.parseLong(id);
        return productService.findById(productId);
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {

        return productService.save(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

}
