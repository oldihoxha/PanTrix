package com.example.pantrix.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private String expiryDate;  // Format: "YYYY-MM-DD"
    private String addedDate;   // Format: "YYYY-MM-DD"
    private Integer quantity;
    private String unit;
    private String status;      // aktiv, verbraucht, entsorgt
    private String notes;
    private String createdAt;
    private String updatedAt;

    public ProductDTO() {}

    public ProductDTO(String name, String category, String expiryDate, Integer quantity) {
        this.name = name;
        this.category = category;
        this.expiryDate = expiryDate;
        this.quantity = quantity != null ? quantity : 1;
        this.unit = "Stück";
        this.status = "fresh";
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getExpiryDate() { return expiryDate; }
    public String getAddedDate() { return addedDate; }
    public Integer getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public void setAddedDate(String addedDate) { this.addedDate = addedDate; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}

