package com.example.model;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class Cart {
    private UUID id;
    private UUID userId;
    private List<Product> products = new ArrayList<>();

    // Constructor
    public Cart() {}
    public Cart(UUID userId, List<Product> products) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.products = products;
    }
    public Cart(UUID id, UUID userId, List<Product> products) {
        this.id = id;
        this.userId = userId;
        this.products = products;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }
    public double calculateTotalPrice() {
        return products.stream()
                .mapToDouble(Product::getPrice) // Sum up the price of each product
                .sum();
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // Add a product to the cart
    public void addProduct(Product product) {
        this.products.add(product);
    }

    // Remove a product from the cart
    public void removeProduct(Product product) {
         this.products.remove(product);
    }
}
