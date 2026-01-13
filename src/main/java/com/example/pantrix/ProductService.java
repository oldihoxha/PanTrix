package com.example.pantrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Hole alle Produkte eines Benutzers
    public List<Product> getProductsByUserId(Long userId) {
        logger.info("Hole Produkte für User ID: {}", userId);
        List<Product> products = productRepository.findByUserId(userId);
        logger.info("Gefundene Produkte: {}", products.size());
        return products;
    }

    // Erstelle ein neues Produkt
    public Product createProduct(Long userId, Product product) {
        logger.info("Erstelle Produkt für User ID: {} mit Name: {}", userId, product.getName());
        
        // Validiere Input
        if (product.getName() == null || product.getName().isBlank()) {
            logger.error("Produktname ist leer");
            throw new IllegalArgumentException("Produktname ist erforderlich");
        }
        if (product.getCategory() == null || product.getCategory().isBlank()) {
            logger.error("Kategorie ist leer");
            throw new IllegalArgumentException("Kategorie ist erforderlich");
        }
        if (product.getQuantity() == null || product.getQuantity() <= 0) {
            logger.error("Menge ist ungültig: {}", product.getQuantity());
            throw new IllegalArgumentException("Menge muss größer als 0 sein");
        }
        if (product.getUnit() == null || product.getUnit().isBlank()) {
            logger.error("Einheit ist leer");
            throw new IllegalArgumentException("Einheit ist erforderlich");
        }
        if (product.getExpiryDate() == null) {
            logger.error("Verfallsdatum ist leer");
            throw new IllegalArgumentException("Verfallsdatum ist erforderlich");
        }
        
        // Hole Benutzer
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                logger.error("Benutzer nicht gefunden mit ID: {}", userId);
                return new RuntimeException("Benutzer nicht gefunden");
            });

        product.setUser(user);
        product.setAddedDate(LocalDate.now());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        if (product.getStatus() == null || product.getStatus().isBlank()) {
            product.setStatus("aktiv");
        }

        logger.info("Speichere Produkt: {}", product.getName());
        Product saved = productRepository.save(product);
        logger.info("Produkt erfolgreich gespeichert mit ID: {}", saved.getId());
        
        return saved;
    }

    // Aktualisiere ein Produkt
    public Product updateProduct(Long userId, Long productId, Product updates) {
        logger.info("Aktualisiere Produkt ID: {} für User ID: {}", productId, userId);
        
        Optional<Product> existing = productRepository.findById(productId);

        if (existing.isPresent()) {
            Product product = existing.get();

            // Sicherheit: Prüfe ob Produkt dem User gehört
            if (!product.getUser().getId().equals(userId)) {
                logger.error("User {} hat keine Berechtigung für Produkt {}", userId, productId);
                throw new RuntimeException("Sie haben keine Berechtigung dieses Produkt zu bearbeiten");
            }

            if (updates.getName() != null && !updates.getName().isBlank()) product.setName(updates.getName());
            if (updates.getCategory() != null && !updates.getCategory().isBlank()) product.setCategory(updates.getCategory());
            if (updates.getExpiryDate() != null) product.setExpiryDate(updates.getExpiryDate());
            if (updates.getQuantity() != null && updates.getQuantity() > 0) product.setQuantity(updates.getQuantity());
            if (updates.getUnit() != null && !updates.getUnit().isBlank()) product.setUnit(updates.getUnit());
            if (updates.getStatus() != null && !updates.getStatus().isBlank()) product.setStatus(updates.getStatus());
            if (updates.getNotes() != null) product.setNotes(updates.getNotes());

            product.setUpdatedAt(LocalDateTime.now());

            logger.info("Produkt erfolgreich aktualisiert: {}", productId);
            return productRepository.save(product);
        }

        logger.error("Produkt nicht gefunden: {}", productId);
        throw new RuntimeException("Produkt nicht gefunden");
    }

    // Lösche ein Produkt
    public void deleteProduct(Long userId, Long productId) {
        logger.info("Lösche Produkt ID: {} für User ID: {}", productId, userId);
        
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            // Sicherheit: Prüfe ob Produkt dem User gehört
            if (!product.get().getUser().getId().equals(userId)) {
                logger.error("User {} hat keine Berechtigung Produkt {} zu löschen", userId, productId);
                throw new RuntimeException("Sie haben keine Berechtigung dieses Produkt zu löschen");
            }
            productRepository.deleteById(productId);
            logger.info("Produkt erfolgreich gelöscht: {}", productId);
        } else {
            logger.error("Produkt nicht gefunden zum Löschen: {}", productId);
            throw new RuntimeException("Produkt nicht gefunden");
        }
    }

    // Markiere Produkt als verbraucht
    public Product consumeProduct(Long productId) {
        logger.info("Markiere Produkt als verbraucht: {}", productId);
        
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            Product p = product.get();
            p.setStatus("verbraucht");
            p.setUpdatedAt(LocalDateTime.now());
            logger.info("Produkt als verbraucht markiert: {}", productId);
            return productRepository.save(p);
        }

        logger.error("Produkt nicht gefunden zum Markieren: {}", productId);
        throw new RuntimeException("Produkt nicht gefunden");
    }
}

