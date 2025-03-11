package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {

    private static final List<Cart> carts = new ArrayList<>();
    public CartRepository(){
    }
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/carts.json"; // JSON file path for carts
    }

    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class; // Deserialize JSON into Cart array
    }

    // Retrieve all carts
    public ArrayList<Cart> getCarts() {
        return findAll();
    }

    // Retrieve a cart by ID
    public Cart getCartById(UUID cartId) {
        return findAll().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }

    // Retrieve a cart by User ID
    public Cart getCartByUserId(UUID userId) {
        return findAll().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // Add a new cart
    public Cart addCart(Cart cart) {
        // Check if the cart is null and throw an exception if it is
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        // Proceed to save the cart if it's not null
        save(cart);
        return cart;
    }

    // Delete a cart by ID
    public boolean deleteCart(UUID cartId) {
        // Check for null cartId and throw an exception
        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }

        // Retrieve all carts from the data source
        ArrayList<Cart> carts = findAll();

        // Remove the cart with the given ID if it exists
        boolean removed = carts.removeIf(cart -> cart.getId().equals(cartId));

        // If a cart was removed, save the updated list
        if (removed) {
            // If saving fails, you could handle the exception or log the error
            try {
                saveAll(carts);
            } catch (Exception e) {
                throw new RuntimeException("Failed to save updated carts after deletion", e);
            }
        } else {
            // If no cart was removed, throw an exception indicating the cart doesn't exist
            throw new IllegalArgumentException("Cart with ID " + cartId + " not found.");
        }

        // Return whether a cart was removed or not
        return removed;
    }


    public void deleteAllCarts() {
        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Clear the list of carts in memory (not necessary, but good for consistency)
        carts.clear();

        // Write an empty list back to the JSON file
        try {
            objectMapper.writeValue(new File(getDataPath()), new ArrayList<Cart>());
        } catch (IOException e) {
            e.printStackTrace();  // Handle the error appropriately in your application
        }
    }

    // Update an existing cart
    public boolean updateCart(Cart updatedCart) {
        ArrayList<Cart> carts = findAll();
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i).getId().equals(updatedCart.getId())) {
                carts.set(i, updatedCart);
                saveAll(carts);
                return true;
            }
        }
        return false;
    }
    public void addProductToCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.getProducts().add(product); // Add product to cart
                saveAll(carts);
                return;
            }
        }
        throw new IllegalArgumentException("Cart with ID " + cartId + " not found.");
    }


    public void deleteProductFromCart(UUID cartId, UUID productId) {
        ArrayList<Cart> carts = findAll();
        Cart cart = carts.stream()
                .filter(c -> c.getId().equals(cartId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cart with ID " + cartId + " not found."));

        boolean removed = cart.getProducts().removeIf(product -> product.getId().equals(productId));

        if (removed) {
            saveAll(carts);
        } else {
            throw new IllegalArgumentException("Product with ID " + productId + " not found in cart.");
        }
    }




}
