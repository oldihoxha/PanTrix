package com.example.pantrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Hole alle Produkte eines Benutzers
    public List<Product> getProductsByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    // Erstelle ein neues Produkt
    public Product createProduct(Long userId, Product product) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        product.setUser(user);
        product.setAddedDate(LocalDate.now());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        if (product.getStatus() == null) {
            product.setStatus("aktiv");
        }

        return productRepository.save(product);
    }

    // Aktualisiere ein Produkt
    public Product updateProduct(Long productId, Product updates) {
        Optional<Product> existing = productRepository.findById(productId);

        if (existing.isPresent()) {
            Product product = existing.get();

            if (updates.getName() != null) product.setName(updates.getName());
            if (updates.getCategory() != null) product.setCategory(updates.getCategory());
            if (updates.getExpiryDate() != null) product.setExpiryDate(updates.getExpiryDate());
            if (updates.getQuantity() != null) product.setQuantity(updates.getQuantity());
            if (updates.getUnit() != null) product.setUnit(updates.getUnit());
            if (updates.getStatus() != null) product.setStatus(updates.getStatus());
            if (updates.getNotes() != null) product.setNotes(updates.getNotes());

            product.setUpdatedAt(LocalDateTime.now());

            return productRepository.save(product);
        }

        throw new RuntimeException("Produkt nicht gefunden");
    }

    // Lösche ein Produkt
    public void deleteProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            productRepository.deleteById(productId);
        } else {
            throw new RuntimeException("Produkt nicht gefunden");
        }
    }

    // Markiere Produkt als verbraucht
    public Product consumeProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            Product p = product.get();
            p.setStatus("verbraucht");
            p.setUpdatedAt(LocalDateTime.now());
            return productRepository.save(p);
        }

        throw new RuntimeException("Produkt nicht gefunden");
    }
}

