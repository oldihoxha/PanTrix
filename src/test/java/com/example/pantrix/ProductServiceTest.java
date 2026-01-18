package com.example.pantrix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

@DisplayName("ProductService Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductService productService;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Test User setup
        testUser = new User("test@example.com", "password123", "John", "Doe");
        testUser.setId(1L);

        // Test Product setup
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setUser(testUser);
        testProduct.setName("Milk");
        testProduct.setCategory("Dairy");
        testProduct.setQuantity(2);
        testProduct.setUnit("Liters");
        testProduct.setExpiryDate(LocalDate.now().plusDays(5));
        testProduct.setAddedDate(LocalDate.now());
        testProduct.setStatus("aktiv");
        testProduct.setCreatedAt(LocalDateTime.now());
        testProduct.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Test 1: Create Product Successfully - Positive Case")
    void testCreateProduct_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product newProduct = new Product();
        newProduct.setName("Milk");
        newProduct.setCategory("Dairy");
        newProduct.setQuantity(2);
        newProduct.setUnit("Liters");
        newProduct.setExpiryDate(LocalDate.now().plusDays(5));

        // When
        Product result = productService.createProduct(1L, newProduct);

        // Then
        assertNotNull(result);
        assertEquals("Milk", result.getName());
        assertEquals("Dairy", result.getCategory());
        assertEquals(2, result.getQuantity());
        assertEquals("aktiv", result.getStatus());
        assertEquals(testUser, result.getUser());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 2: Create Product with Blank Name - Negative Case")
    void testCreateProduct_BlankName_ThrowsException() {
        // Given
        Product newProduct = new Product();
        newProduct.setName("   ");
        newProduct.setCategory("Dairy");
        newProduct.setQuantity(2);
        newProduct.setUnit("Liters");
        newProduct.setExpiryDate(LocalDate.now().plusDays(5));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(1L, newProduct);
        }, "Produktname ist erforderlich");
    }

    @Test
    @DisplayName("Test 3: Create Product with Invalid Quantity - Edge Case")
    void testCreateProduct_InvalidQuantity_ThrowsException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Product newProduct = new Product();
        newProduct.setName("Milk");
        newProduct.setCategory("Dairy");
        newProduct.setQuantity(0);
        newProduct.setUnit("Liters");
        newProduct.setExpiryDate(LocalDate.now().plusDays(5));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(1L, newProduct);
        }, "Menge muss größer als 0 sein");
    }

    @Test
    @DisplayName("Test 4: Create Product with Non-Existent User - Negative Case")
    void testCreateProduct_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Product newProduct = new Product();
        newProduct.setName("Milk");
        newProduct.setCategory("Dairy");
        newProduct.setQuantity(2);
        newProduct.setUnit("Liters");
        newProduct.setExpiryDate(LocalDate.now().plusDays(5));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            productService.createProduct(999L, newProduct);
        }, "Benutzer nicht gefunden");
    }

    @Test
    @DisplayName("Test 5: Get Products By User - Positive Case")
    void testGetProductsByUserId_Success() {
        // Given
        List<Product> products = new ArrayList<>();
        products.add(testProduct);
        when(productRepository.findByUserId(1L)).thenReturn(products);

        // When
        List<Product> result = productService.getProductsByUserId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Milk", result.get(0).getName());
        verify(productRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("Test 6: Update Product - Positive Case")
    void testUpdateProduct_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product updates = new Product();
        updates.setName("Updated Milk");
        updates.setQuantity(3);

        testProduct.setName("Updated Milk");
        testProduct.setQuantity(3);

        // When
        Product result = productService.updateProduct(1L, 1L, updates);

        // Then
        assertNotNull(result);
        assertEquals("Updated Milk", result.getName());
        assertEquals(3, result.getQuantity());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 7: Update Product Without Authorization - Negative Case")
    void testUpdateProduct_Unauthorized_ThrowsException() {
        // Given
        User anotherUser = new User("other@example.com", "password", "Jane", "Doe");
        anotherUser.setId(2L);
        testProduct.setUser(anotherUser);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product updates = new Product();
        updates.setName("Hacked");

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(1L, 1L, updates);
        }, "Sie haben keine Berechtigung dieses Produkt zu bearbeiten");
    }

    @Test
    @DisplayName("Test 8: Delete Product Successfully - Positive Case")
    void testDeleteProduct_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).deleteById(1L);

        // When
        assertDoesNotThrow(() -> {
            productService.deleteProduct(1L, 1L);
        });

        // Then
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test 9: Consume Product Mark as Used - Positive Case")
    void testConsumeProduct_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        testProduct.setStatus("verbraucht");

        // When
        Product result = productService.consumeProduct(1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals("verbraucht", result.getStatus());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 10: Create Product with Missing Category - Edge Case")
    void testCreateProduct_MissingCategory_ThrowsException() {
        // Given
        Product newProduct = new Product();
        newProduct.setName("Milk");
        newProduct.setCategory(null);
        newProduct.setQuantity(2);
        newProduct.setUnit("Liters");
        newProduct.setExpiryDate(LocalDate.now().plusDays(5));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(1L, newProduct);
        }, "Kategorie ist erforderlich");
    }
}
