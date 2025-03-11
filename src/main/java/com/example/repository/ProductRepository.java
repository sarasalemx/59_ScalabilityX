package com.example.repository;

import com.example.model.Order;
import com.example.model.Product;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository extends MainRepository<Product> {
    public ProductRepository() {}
    public static List<Product> products = new ArrayList<>();

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/products.json"; // JSON file path for products
    }

    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class; // Deserialize JSON into Product array
    }

    // Retrieve all products
    public ArrayList<Product> getProducts() {
        return findAll();
    }

    // Retrieve product by ID
    public Product getProductById(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        return findAll().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
    }


    // Add a new product
    public Product addProduct(Product product) {
        // Check if the product is null and throw an exception
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        // Save the product if it's not null
        save(product);
        return product;
    }


    public void deleteProduct(UUID productId) {
        ArrayList<Product> products = findAll();
        Optional<Product> productToDelete = products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();

        if (productToDelete.isPresent()) {
            products.remove(productToDelete.get());
            saveAll(products);
        } else {
            throw new IllegalArgumentException("Product not found");
        }
    }




    public Product updateProduct(UUID productId, String newName, double newPrice) {
        // Check if the new price is negative
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        // Retrieve the list of products
        ArrayList<Product> products = findAll();

        // Iterate through the products and update the product if the ID matches
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                saveAll(products);  // Save the updated list of products
                return product;
            }
        }

        // Throw an exception if the product ID is not found
        throw new IllegalArgumentException("Product with ID " + productId + " not found.");
    }



    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }

        List<Product> products = findAll();
        for (Product product : products) {
            if (productIds.contains(product.getId())) {
                double newPrice = product.getPrice() * (1 - (discount / 100));
                product.setPrice(newPrice);
            }
        }
        saveAll((ArrayList<Product>) products); // Save the updated product list
    }

    public void deleteAllProducts() {
        ArrayList<Product> products = new ArrayList<>(); // Create an empty list
        saveAll(products); // Save the empty list back to the JSON file
    }
}
