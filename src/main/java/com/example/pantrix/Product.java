package com.example.pantrix;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "Benutzer ist erforderlich")
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    @NotBlank(message = "Produktname ist erforderlich")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Kategorie ist erforderlich")
    private String category;

    @Column(nullable = false)
    @NotNull(message = "Verfallsdatum ist erforderlich")
    private LocalDate expiryDate;

    @Column(nullable = false)
    private LocalDate addedDate;

    @Column(nullable = false)
    @NotNull(message = "Menge ist erforderlich")
    private Integer quantity;

    @Column(nullable = false)
    @NotBlank(message = "Einheit ist erforderlich")
    private String unit;

    @Column(nullable = false)
    private String status = "aktiv"; // aktiv, verbraucht, entsorgt

    @Column
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public LocalDate getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate = addedDate; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

