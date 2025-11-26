package com.example.pantrix;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

        private String name;
        private LocalDate expiryDate;


        public Product(){}

        public Product(String name, LocalDate expiryDate){
            this.name = name;
            this.expiryDate = expiryDate;
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public LocalDate getExpiryDate(){
            return expiryDate;
        }

        public void setExpiryDate(LocalDate expiryDate){
            this.expiryDate = expiryDate;
        }

}


