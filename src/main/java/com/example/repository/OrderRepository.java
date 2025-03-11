package com.example.repository;

import com.example.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("rawtypes")
public class OrderRepository extends MainRepository<Order> {

    public OrderRepository() {
    }

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/orders.json"; // JSON storage path
    }

    @Override
    protected Class<Order[]> getArrayType() {
        return Order[].class;
    }

    // ✅ Fetch all orders
    public ArrayList<Order> getOrders() {
        return findAll();
    }

    // ✅ Fetch a specific order by ID
    public Order getOrderById(UUID orderId) {
        return getOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    // ✅ Fetch orders for a specific user
    public List<Order> getOrdersByUserId(UUID userId) {
        return getOrders().stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // ✅ Save a new order
    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        // Ensure the order's total price is not negative
        if (order.getTotalPrice() < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }

        order.calculateTotalPrice(); // Ensure total price is calculated before saving
        save(order);
    }


    // ✅ Delete an order by ID
    public void deleteOrderById(UUID orderId) {
        ArrayList<Order> orders = getOrders();
        boolean removed = orders.removeIf(order -> order.getId().equals(orderId));
        if (removed) {
            saveAll(orders);
        } else {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
        }
    }
    // ✅ Delete all orders
    public void deleteAllOrders() {
        ArrayList<Order> orders = new ArrayList<>(); // Create an empty list
        saveAll(orders); // Save the empty list back to the JSON file
    }
}
