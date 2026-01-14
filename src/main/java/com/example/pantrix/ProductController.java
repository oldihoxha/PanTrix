package com.example.pantrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Extrahiere die aktuelle User-ID aus dem Security Context (JWT Token)
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String username) {
                // Username oder Email aus Token
                var user = userRepository.findByEmail(username);
                if (user.isPresent()) {
                    return user.get().getId();
                }
            }
        }
        // Fallback: User ID 1 (für Entwicklung)
        logger.warn("Keine User ID im Security Context gefunden, nutze Fallback: 1");
        return 1L;
    }

    // GET: Alle Produkte des aktuellen Benutzers
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        Long userId = getCurrentUserId();
        logger.info("GET /api/products für User {} aufgerufen", userId);
        List<Product> products = productService.getProductsByUserId(userId);
        return ResponseEntity.ok(products);
    }

    // GET: Alle Produkte eines spezifischen Benutzers (mit userId im Path)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Product>> getProductsByUser(@PathVariable Long userId) {
        logger.info("GET /api/products/user/{} aufgerufen", userId);
        List<Product> products = productService.getProductsByUserId(userId);
        return ResponseEntity.ok(products);
    }

    // POST: Neues Produkt erstellen
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Long userId = getCurrentUserId();
        logger.info("POST /api/products für User {} mit Produkt: {}", userId, product.getName());
        try {
            Product created = productService.createProduct(userId, product);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            logger.error("Fehler beim Erstellen des Produkts: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // POST: Neues Produkt erstellen mit expliziter userId
    @PostMapping("/user/{userId}")
    public ResponseEntity<Product> createProductWithUserId(@PathVariable Long userId, @RequestBody Product product) {
        logger.info("POST /api/products/user/{} mit Produkt: {}", userId, product.getName());
        try {
            Product created = productService.createProduct(userId, product);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            logger.error("Fehler beim Erstellen des Produkts: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT: Produkt aktualisieren
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product updates) {
        Long userId = getCurrentUserId();
        logger.info("PUT /api/products/{} für User {} aufgerufen", productId, userId);
        try {
            Product updated = productService.updateProduct(userId, productId, updates);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            logger.error("Fehler beim Aktualisieren des Produkts: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT: Produkt aktualisieren mit expliziter userId
    @PutMapping("/{productId}/user/{userId}")
    public ResponseEntity<Product> updateProductWithUserId(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestBody Product updates) {
        logger.info("PUT /api/products/{}/user/{}", productId, userId);
        try {
            Product updated = productService.updateProduct(userId, productId, updates);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            logger.error("Fehler beim Aktualisieren des Produkts: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE: Produkt löschen
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        logger.info("DELETE /api/products/{} für User {} aufgerufen", productId, userId);
        try {
            productService.deleteProduct(userId, productId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Fehler beim Löschen des Produkts: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE: Produkt löschen mit expliziter userId
    @DeleteMapping("/{productId}/user/{userId}")
    public ResponseEntity<Void> deleteProductWithUserId(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        logger.info("DELETE /api/products/{}/user/{}", productId, userId);
        try {
            productService.deleteProduct(userId, productId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Fehler beim Löschen des Produkts: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // POST: Produkt als verbraucht markieren
    @PostMapping("/{productId}/consume")
    public ResponseEntity<Product> consumeProduct(@PathVariable Long productId) {
        logger.info("POST /api/products/{}/consume", productId);
        try {
            Product consumed = productService.consumeProduct(productId);
            return ResponseEntity.ok(consumed);
        } catch (RuntimeException e) {
            logger.error("Fehler beim Verbrauchen des Produkts: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}




