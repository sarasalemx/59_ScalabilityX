package com.example.controller;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;
    CartService cartService;
    ProductService productService;

    // Constructor-based Dependency Injection
    @Autowired
    public UserController(UserService userService , CartService cartService,ProductService productService) {
        this.userService = userService;
        this.cartService = cartService;
        this.productService  = productService;
    }

    // Get all users
    @GetMapping("/")
    public ArrayList<User> getUsers() {
        return userService.getUsers();
    }

    // Get a user by ID
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId){
        return  userService.getUserById(userId);
    }


    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId){
        return  userService.getOrdersByUserId(userId);
    }


    @PostMapping("/")
    public User addUser(@RequestBody User user)
    {
        return userService.addUser(user);
    }

    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId){
        userService.addOrderToUser(userId);
        return "Order added successfully";
    }

    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId){
        userService.emptyCart(userId);
        return "Cart emptied successfully";
    }

    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId)
    {
        cartService.addProductToCart(userId,productService.getProductById(productId));
        return "Product added to cart";
    }
    // Update an existing user
    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId){
        userService.removeOrderFromUser(userId,orderId);
        return  "Order removed successfully";
    }


    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return "Product not found";
        }

        Cart cart = cartService.getCartByUserId(userId); // Optional: use cartRepository here
        if (cart == null || cart.getProducts().isEmpty()) {
            return "Cart is empty";
        }

        boolean removed = cart.getProducts().remove(product); // assuming List<Product> inside Cart

        if (removed) {
            cartService.updateCart(cart);
            return "Product deleted from cart";
        } else {
            return "Product deleted from cart";
        }
    }

    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        List<User> users = userService.getUsers(); // ✅ Fetch all users first

        boolean userExists = users.stream().anyMatch(user -> user.getId().equals(userId));

        if (!userExists) {
            return"User not found";
        }

        userService.deleteUserById(userId); // ✅ Now call the service method
        return "User deleted successfully";
    }

}
