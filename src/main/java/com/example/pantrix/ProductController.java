package com.example.pantrix;

import com.example.pantrix.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5713", "http://localhost:8080", "https://pantrix-frontnd.onrender.com"},
             allowedHeaders = "*",
             allowCredentials = "true")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    // ===== /api/products Routes =====
    
    @GetMapping("/api/products")
    public ResponseEntity<?> getProducts(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            logger.info("GET /api/products - Auth Header: {}", authHeader != null ? "present" : "missing");

            Long userId = extractUserIdFromHeader(authHeader);
            List<Product> products = productService.getProductsByUserId(userId);

            logger.info("Produkte geladen: {} Stück", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Fehler beim Abrufen von Produkten", e);
            return ResponseEntity.badRequest().body("Fehler: " + e.getMessage());
        }
    }

    @PostMapping("/api/products")
    public ResponseEntity<?> createProduct(
        @RequestBody Product product,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        try {
            logger.info("POST /api/products - Neues Produkt: {}", product.getName());
            logger.debug("Auth Header: {}", authHeader != null ? "present" : "missing");

            Long userId = extractUserIdFromHeader(authHeader);

            logger.info("Erstelle Produkt für User ID: {}", userId);
            Product created = productService.createProduct(userId, product);

            logger.info("Produkt erfolgreich erstellt mit ID: {}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            logger.warn("Validierungsfehler beim Erstellen von Produkt", e);
            return ResponseEntity.badRequest().body("Validierungsfehler: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Fehler beim Erstellen von Produkt", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler: " + e.getMessage());
        }
    }

    @PutMapping("/api/products/{id}")
    public ResponseEntity<?> updateProduct(
        @PathVariable Long id,
        @RequestBody Product product,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        try {
            logger.info("PUT /api/products/{} - Update", id);

            Long userId = extractUserIdFromHeader(authHeader);
            Product updated = productService.updateProduct(userId, id, product);

            logger.info("Produkt {} erfolgreich aktualisiert", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Fehler beim Aktualisieren von Produkt {}", id, e);
            return ResponseEntity.badRequest().body("Fehler: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<?> deleteProduct(
        @PathVariable Long id,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        try {
            logger.info("DELETE /api/products/{}", id);

            Long userId = extractUserIdFromHeader(authHeader);
            productService.deleteProduct(userId, id);

            logger.info("Produkt {} erfolgreich gelöscht", id);
            return ResponseEntity.ok("Produkt gelöscht");
        } catch (Exception e) {
            logger.error("Fehler beim Löschen von Produkt {}", id, e);
            return ResponseEntity.badRequest().body("Fehler: " + e.getMessage());
        }
    }

    // ===== Hilfsmethoden =====

    private Long extractUserIdFromHeader(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            logger.error("Authorization Header fehlt");
            throw new RuntimeException("Authorization Header erforderlich");
        }

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        logger.debug("Token extrahiert und wird validiert");

        Long userId = jwtTokenProvider.extractUserId(token);

        if (userId == null) {
            logger.error("User ID konnte nicht aus Token extrahiert werden");
            throw new RuntimeException("Ungültiger Token - User-ID konnte nicht extrahiert werden");
        }

        logger.debug("User ID aus Token extrahiert: {}", userId);
        return userId;
    }
}

