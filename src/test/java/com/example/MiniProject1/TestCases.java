package com.example.MiniProject1;

import ch.qos.logback.core.net.ObjectWriter;
import com.example.controller.CartController;
import com.example.controller.OrderController;
import com.example.controller.ProductController;
import com.example.controller.UserController;
import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TestCases {
    private User user;
    private CartRepository cartRepository = new CartRepository(); // Your actual repository (no mocks)
    private CartService cartService = new CartService(cartRepository); // Your actual service

    private OrderRepository orderRepository = new OrderRepository();
    private OrderService orderService = new OrderService(orderRepository);

    private UserRepository userRepository = new UserRepository();
    private UserService userService=  new UserService(cartService, userRepository, cartRepository, orderRepository, user);
    private ProductRepository productRepository = new ProductRepository();
    private ProductService productService = new ProductService(productRepository);







    @Test
    void getCarts_shouldReturnEmptyList_whenNoCarts() {
        // Arrange: Ensure that the repository is empty
        cartService.deleteAllCarts();  // Clear any existing carts


        // Act: Call the method to test
        ArrayList<Cart> actualCarts = cartService.getCarts();

        // Assert: Verify the results
        assertNotNull(actualCarts);  // Ensure the list is not null
        assertTrue(actualCarts.isEmpty());  // Ensure the list is empty
    }

    @Test

    void getCarts_shouldReturnCorrectCarts() {
        // Arrange: Set up Cart objects
        cartService.deleteAllCarts();  // Clear any existing carts

        Cart cart1 = new Cart();
        cart1.setId(UUID.randomUUID());
        cart1.setUserId(UUID.randomUUID());
        cart1.setProducts(new ArrayList<>());  // Add an empty list of products

        Cart cart2 = new Cart();
        cart2.setId(UUID.randomUUID());
        cart2.setUserId(UUID.randomUUID());
        cart2.setProducts(new ArrayList<>());  // Add an empty list of products

        // Act: Add carts to the service
        cartService.addCart(cart1);  // Add cart1 using the CartService
        cartService.addCart(cart2);  // Add cart2 using the CartService

        // Act: Call the method to get the carts from the service
        ArrayList<Cart> actualCarts = cartService.getCarts();

        // Assert: Verify the results

        assertEquals(2, actualCarts.size());  // Ensure we have exactly 2 carts

    }


    @Test
    void getCarts_shouldReturnSingleCart_whenOneCartExists() {

        cartService.deleteAllCarts();  // Clear any existing carts
        // Arrange: Set up a single cart
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUserId(UUID.randomUUID());
        cart.setProducts(new ArrayList<>());

        cartService.addCart(cart);

        // Act: Call the method to test
        ArrayList<Cart> actualCarts = cartService.getCarts();

        // Assert: Verify the results
        assertNotNull(actualCarts);
        assertEquals(1, actualCarts.size());  // Ensure only one cart is returned
        assertEquals(cart.getId(), actualCarts.get(0).getId());  // Ensure the IDs match
    }

    @Test
    void getCartById_shouldReturnCart_whenCartExists() {
        // Arrange: Create a cart and add it to the repository
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUserId(UUID.randomUUID());
        cart.setProducts(new ArrayList<>());
        cartRepository.addCart(cart);  // Assuming this method adds the cart to the repository

        // Act: Call the method to get the cart by its ID
        Cart retrievedCart = cartService.getCartById(cartId);

        // Assert: Verify that the cart returned is the correct one
        assertNotNull(retrievedCart);
        assertEquals(cartId, retrievedCart.getId());
    }

    @Test
    void getCartById_shouldReturnNull_whenCartDoesNotExist() {
        // Arrange: Use a cart ID that does not exist in the repository
        UUID nonExistentCartId = UUID.randomUUID();

        // Act: Call the method to get the cart by its ID
        Cart retrievedCart = cartService.getCartById(nonExistentCartId);

        // Assert: Verify that the cart returned is null
        assertNull(retrievedCart);
    }

    @Test
    void getCartById_shouldReturnNull_whenNullIdIsProvided() {
        // Act: Call the method with a null ID
        Cart retrievedCart = cartService.getCartById(null);

        // Assert: Verify that the cart returned is null
        assertNull(retrievedCart);
    }

    @Test
    void getCartByUserId_shouldReturnCart_whenUserExists() {
        // Arrange: Create a user ID and a cart with that user ID
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUserId(userId);
        cart.setProducts(new ArrayList<>());
        cartRepository.addCart(cart);  // Assuming this method adds the cart to the repository

        // Act: Call the method to get the cart by the user's ID
        Cart retrievedCart = cartService.getCartByUserId(userId);

        // Assert: Verify that the cart returned is the correct one
        assertNotNull(retrievedCart);
        assertEquals(userId, retrievedCart.getUserId());
    }


    @Test
    void getCartByUserId_shouldReturnNull_whenNoCartForUser() {
        // Arrange: Use a user ID that has no associated cart
        UUID nonExistentUserId = UUID.randomUUID();

        // Act: Call the method to get the cart by the user's ID
        Cart retrievedCart = cartService.getCartByUserId(nonExistentUserId);

        // Assert: Verify that the cart returned is null
        assertNull(retrievedCart);
    }

    @Test
    void getCartByUserId_shouldReturnNull_whenNullUserIdIsProvided() {
        // Act: Call the method with a null user ID
        Cart retrievedCart = cartService.getCartByUserId(null);

        // Assert: Verify that the cart returned is null
        assertNull(retrievedCart);
    }

    @Test
    void addCart_shouldAddCartSuccessfully() {
        // Arrange: Create a new cart
        Cart newCart = new Cart();
        newCart.setId(UUID.randomUUID());
        newCart.setUserId(UUID.randomUUID());
        newCart.setProducts(new ArrayList<>());

        // Act: Call the method to add the cart
        Cart addedCart = cartService.addCart(newCart);

        // Assert: Verify that the cart was added successfully
        assertNotNull(addedCart);
        assertEquals(newCart.getId(), addedCart.getId());
        assertEquals(newCart.getUserId(), addedCart.getUserId());
    }


    @Test
    void addCart_shouldNotAllowDuplicateCarts() {
        // Arrange: Create a new cart
        Cart newCart = new Cart();
        newCart.setId(UUID.randomUUID());
        newCart.setUserId(UUID.randomUUID());
        newCart.setProducts(new ArrayList<>());

        // Add the cart once
        cartService.addCart(newCart);

        // Act: Try adding the same cart again
        Cart duplicateCart = cartService.addCart(newCart);

        // Assert: Verify that the cart is not duplicated (i.e., it should return the same cart)
        assertEquals(newCart.getId(), duplicateCart.getId());
        assertEquals(newCart.getUserId(), duplicateCart.getUserId());
    }

    @Test
    void addCart_shouldThrowException_whenCartIsNull() {
        // Arrange: Cart is null
        Cart nullCart = null;

        // Act & Assert: Try adding a null cart and expect an exception
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.addCart(nullCart);
        });
    }
    @Test
    void deleteCartById_shouldThrowException_whenCartIdIsNull() {
        // Arrange: Cart ID is null
        UUID nullCartId = null;

        // Act & Assert: Try deleting a cart with null ID and expect an exception
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteCartById(nullCartId);
        }, "Cart ID cannot be null");
    }


    @Test
    void deleteCartById_shouldThrowException_whenCartNotFound() {
        // Arrange: Assume cartId doesn't exist in the repository
        UUID nonExistentCartId = UUID.randomUUID();

        // Act & Assert: Try deleting a non-existent cart and expect an exception
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteCartById(nonExistentCartId);
        }, "Cart with ID " + nonExistentCartId + " not found.");
    }


    @Test
    void deleteCartById_shouldDeleteCart_whenCartExists() {
        // Arrange: Create and add a cart
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUserId(UUID.randomUUID());
        cart.setProducts(new ArrayList<>());

        // Add the cart to the repository (Assuming addCart is working)
        cartService.addCart(cart);

        // Act: Delete the cart by ID
        cartService.deleteCartById(cart.getId());

        // Assert: Ensure the cart no longer exists in the repository
        assertNull(cartRepository.getCartById(cart.getId()), "Cart should be deleted.");
    }
    // leh de wahda
    @Test
    void deleteProductFromCart_shouldRemoveProduct_whenProductExists() {
        // Arrange: Create a Cart, User ID, and Product
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Product1");

        // Add product to the cart using the addProduct method in Cart
        Cart cart = new Cart(userId, new ArrayList<>());
        cart.addProduct(product); // Use the addProduct method to add the product to the cart
        cartRepository.addCart(cart); // Save the cart in the repository

        // Act: Call deleteProductFromCart to remove the product
        cartService.deleteProductFromCart2(cart.getId(), product.getId());

        // Assert: Verify the product is removed from the cart
        Cart updatedCart = cartService.getCartByUserId(userId);
        assertNotNull(updatedCart);
        assertTrue(updatedCart.getProducts().isEmpty(), "Product should be removed from the cart.");
    }

    @Test
    public void testDeleteProductFromCart_CartNotFound() {
        // Arrange
        UUID cartId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteProductFromCart2(cartId, productId);
        });

        assertEquals("Cart with ID " + cartId + " not found.", exception.getMessage());
    }


    @Test
    public void testDeleteProductFromCart_ProductNotFound() {
        // Arrange
        UUID cartId = UUID.randomUUID();              // Use this cartId throughout
        UUID productId = UUID.randomUUID();

        Cart cart1 = new Cart();
        cart1.setId(cartId);                          // Set the SAME cartId here
        cart1.setUserId(UUID.randomUUID());
        cart1.setProducts(new ArrayList<>());         // Empty product list
        cartService.addCart(cart1);                   // Add cart using this ID

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteProductFromCart2(cartId, productId);
        });

        assertEquals("Product with ID " + productId + " not found in cart.", exception.getMessage());
    }








    @Test
    void addProductToCart_shouldAddProduct_whenCartExists() {
        // Arrange: Create a Cart, User ID, and Product
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Product1");

        // Create a cart and add it to the cart repository
        Cart cart = new Cart(userId, new ArrayList<>());
        cartRepository.addCart(cart);

        // Act: Call the addProductToCart method to add the product
        cartService.addProductToCart(userId, product);

        // Assert: Verify the product is added to the cart
        Cart updatedCart = cartService.getCartByUserId(userId);
        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getProducts().size()); // Product should be added
        assertEquals(productId, updatedCart.getProducts().get(0).getId()); // Verify product ID
    }

    @Test
    void addProductToCart_shouldCreateNewCart_whenCartDoesNotExist() {
        // Arrange: Create a new User ID and Product
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Product2");

        // Act: Call the addProductToCart method to add the product to a new cart
        cartService.addProductToCart(userId, product);

        // Assert: Verify a new cart is created and the product is added to it
        Cart updatedCart = cartService.getCartByUserId(userId);
        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getProducts().size()); // New cart should contain the product
        assertEquals(productId, updatedCart.getProducts().get(0).getId()); // Verify product ID
    }

    @Test
    void addProductToCart_shouldAddMultipleProducts_whenCartExists() {
        // Arrange: Create a User ID and Products
        UUID userId = UUID.randomUUID();
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        Product product1 = new Product();
        product1.setId(productId1);
        product1.setName("Product3");

        Product product2 = new Product();
        product2.setId(productId2);
        product2.setName("Product4");

        // Create a cart and add it to the cart repository
        Cart cart = new Cart(userId, new ArrayList<>());
        cartRepository.addCart(cart);

        // Act: Add both products to the cart
        cartService.addProductToCart(userId, product1);
        cartService.addProductToCart(userId, product2);

        // Assert: Verify both products are added to the cart
        Cart updatedCart = cartService.getCartByUserId(userId);
        assertNotNull(updatedCart);
        assertEquals(2, updatedCart.getProducts().size()); // Cart should contain 2 products
        assertEquals(productId1, updatedCart.getProducts().get(0).getId()); // Verify first product
        assertEquals(productId2, updatedCart.getProducts().get(1).getId()); // Verify second product
    }


    // ************** ORDER SERVICEEEEEEEE **************************

    @Test
    void addOrder_shouldAddValidOrder() {
        // Arrange: Create an Order
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setTotalPrice(100.00);

        // Act: Call the addOrder method to add the order
        orderService.addOrder(order);

        // Assert: Verify the order is added and exists in the repository
        Order addedOrder = orderRepository.getOrderById(orderId);
        assertNotNull(addedOrder);
        assertEquals(orderId, addedOrder.getId()); // Verify order ID
        assertEquals(userId, addedOrder.getUserId()); // Verify user ID
        //assertEquals(100.00, addedOrder.getTotalPrice()); // Verify total price
    }

    @Test
    void addOrder_shouldThrowException_whenOrderIsNull() {
        // Arrange: Null order
        Order order = null;

        // Act & Assert: Verify that adding a null order throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.addOrder(order);
        });
    }

    @Test
    void addOrder_shouldThrowException_whenPriceIsNegative() {
        // Arrange: Create an Order with a negative price
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setTotalPrice(-10.00); // Invalid price

        // Act & Assert: Verify that adding an order with a negative price throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.addOrder(order);
        });
    }

    @Test
    void getOrders_shouldReturnEmptyList_whenNoOrdersExist() {
        // Arrange: Ensure there are no orders in the repository
        orderRepository.deleteAllOrders();  // Assuming a deleteAllOrders method is available for cleanup

        // Act: Call the method to retrieve orders
        ArrayList<Order> orders = orderService.getOrders();

        // Assert: Verify that the returned list is empty
        assertNotNull(orders);
        assertTrue(orders.isEmpty());  // List should be empty when no orders exist
    }

    @Test
    void getOrders_shouldReturnOrders_whenOrdersExist() {
        // Arrange: Create orders and add them to the repository

        orderRepository.deleteAllOrders();  // Assuming a deleteAllOrders method is available for cleanup

        Order order1 = new Order();
        order1.setId(UUID.randomUUID());
        order1.setUserId(UUID.randomUUID());
        order1.setTotalPrice(100.0);

        Order order2 = new Order();
        order2.setId(UUID.randomUUID());
        order2.setUserId(UUID.randomUUID());
        order2.setTotalPrice(200.0);

        orderService.addOrder(order1);
        orderService.addOrder(order2);

        // Act: Call the method to retrieve orders
        ArrayList<Order> orders = orderService.getOrders();

        // Assert: Verify that the list contains the added orders
        assertNotNull(orders);
        assertEquals(2, orders.size());  // There should be 2 orders
        //assertTrue(orders.contains(order1));  // Ensure order1 is present
        //assertTrue(orders.contains(order2));  // Ensure order2 is present
    }

    @Test
    void getOrders_shouldThrowException_whenOrderListIsNull() {
        // Create a custom test OrderService with a faulty OrderRepository
        OrderRepository nullReturningOrderRepository = new OrderRepository() {
            @Override
            public ArrayList<Order> getOrders() {
                return null; // Simulate bad scenario
            }
        };

        OrderService testOrderService = new OrderService(nullReturningOrderRepository);

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, testOrderService::getOrders);
        assertEquals("Order list is null", exception.getMessage());
    }




    @Test
    void addOrder_shouldThrowException_whenTotalPriceIsNegative() {
        // Arrange: Create an order with a negative total price

        orderRepository.deleteAllOrders();  // Assuming a deleteAllOrders method is available for cleanup
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Order invalidOrder = new Order();
        invalidOrder.setId(orderId);
        invalidOrder.setUserId(userId);
        invalidOrder.setTotalPrice(-50.0);  // Negative total price

        // Act & Assert: Ensure an exception is thrown when trying to add the invalid order
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.addOrder(invalidOrder);  // Attempt to add an order with negative price
        });

        // Retrieve the orders to ensure no invalid orders were added
        ArrayList<Order> orders = orderService.getOrders();

        // Assert: Verify that the orders list is still empty (no invalid orders were added)
        assertNotNull(orders);
        assertEquals(0, orders.size());  // No orders should have been added due to the exception
    }

    @Test
    void getOrderById_shouldReturnOrder_whenOrderExists() {
        // Arrange: Create a valid order and add it to the repository
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setTotalPrice(150.0);

        orderService.addOrder(order);  // Add the order to the service

        // Act: Retrieve the order by its ID
        Order retrievedOrder = orderService.getOrderById(orderId);

        // Assert: Verify the order was retrieved successfully
        assertNotNull(retrievedOrder);  // Ensure the retrieved order is not null
        assertEquals(orderId, retrievedOrder.getId());  // Ensure the retrieved order has the correct ID
        assertEquals(userId, retrievedOrder.getUserId());  // Ensure the retrieved order belongs to the correct user
    }

    @Test
    void getOrderById_shouldReturnNull_whenOrderDoesNotExist() {
        // Arrange: Create a valid order but do not add it to the repository
        UUID nonExistentOrderId = UUID.randomUUID();

        // Act: Attempt to retrieve an order that does not exist
        Order retrievedOrder = orderService.getOrderById(nonExistentOrderId);

        // Assert: Verify that the order is not found (should return null)
        assertNull(retrievedOrder);  // Ensure the retrieved order is null because the order does not exist
    }

    @Test
    void getOrderById_shouldReturnCorrectOrder_whenMultipleOrdersExist() {
        // Arrange: Create and add multiple orders to the repository
        UUID orderId1 = UUID.randomUUID();
        UUID orderId2 = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Order order1 = new Order();
        order1.setId(orderId1);
        order1.setUserId(userId);
        order1.setTotalPrice(100.0);

        Order order2 = new Order();
        order2.setId(orderId2);
        order2.setUserId(userId);
        order2.setTotalPrice(200.0);

        orderService.addOrder(order1);  // Add the first order
        orderService.addOrder(order2);  // Add the second order

        // Act: Retrieve the second order by its ID
        Order retrievedOrder = orderService.getOrderById(orderId2);

        // Assert: Verify that the correct order is retrieved
        assertNotNull(retrievedOrder);  // Ensure the retrieved order is not null
        assertEquals(orderId2, retrievedOrder.getId());  // Ensure the retrieved order has the correct ID
        assertEquals(userId, retrievedOrder.getUserId());  // Ensure the retrieved order belongs to the correct user
        //assertEquals(200.0, retrievedOrder.getTotalPrice());  // Ensure the retrieved order has the correct total price
    }


    @Test
    void deleteOrderById_shouldDeleteOrder_whenOrderExists() {
        // Arrange: Create a valid order and add it to the repository
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setTotalPrice(150.0);

        orderService.addOrder(order);  // Add the order to the service

        // Act: Delete the order by its ID
        orderService.deleteOrderById(orderId);

        // Assert: Verify the order is deleted by attempting to retrieve it
        Order deletedOrder = orderService.getOrderById(orderId);
        assertNull(deletedOrder);  // Ensure the order is null, meaning it has been deleted
    }

    @Test
    void deleteOrderById_shouldThrowException_whenOrderDoesNotExist() {
        // Arrange: Create a non-existent order ID
        UUID nonExistentOrderId = UUID.randomUUID();

        // Act & Assert: Attempt to delete an order that does not exist
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.deleteOrderById(nonExistentOrderId);
        });

        // Assert: Verify that an exception is thrown with the appropriate message
        assertEquals("Order with ID " + nonExistentOrderId + " not found.", exception.getMessage());
    }

    @Test
    void deleteOrderById_shouldDeleteSpecificOrder_whenMultipleOrdersExist() {
        // Arrange: Create and add multiple orders to the repository
        UUID orderId1 = UUID.randomUUID();
        UUID orderId2 = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Order order1 = new Order();
        order1.setId(orderId1);
        order1.setUserId(userId);
        order1.setTotalPrice(100.0);

        Order order2 = new Order();
        order2.setId(orderId2);
        order2.setUserId(userId);
        order2.setTotalPrice(200.0);

        orderService.addOrder(order1);  // Add the first order
        orderService.addOrder(order2);  // Add the second order

        // Act: Delete the second order by its ID
        orderService.deleteOrderById(orderId2);

        // Assert: Verify the second order is deleted and the first order is still present
        Order deletedOrder = orderService.getOrderById(orderId2);
        assertNull(deletedOrder);  // Ensure the second order is null, meaning it has been deleted

        Order remainingOrder = orderService.getOrderById(orderId1);
        assertNotNull(remainingOrder);  // Ensure the first order is still present
        assertEquals(orderId1, remainingOrder.getId());  // Ensure the first order has the correct ID
    }


    //************* PRODUCTT SERVICEE *****************

    @Test
    void getProducts_shouldReturnAllProducts() {
        // Arrange: Create two products and add them to the repository

        productRepository.deleteAllProducts();

        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setName("Product1");
        product1.setPrice(100.0);

        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setName("Product2");
        product2.setPrice(150.0);

        productService.addProduct(product1);
        productService.addProduct(product2);

        // Act: Call the getProducts method to retrieve all products
        ArrayList<Product> products = productService.getProducts();

        // Assert: Verify that the products are returned correctly
        assertNotNull(products);
        assertEquals(2, products.size()); // Ensure there are 2 products
        //assertTrue(products.contains(product1)); // Ensure product1 is in the list
        //assertTrue(products.contains(product2)); // Ensure product2 is in the list
    }

    @Test
    void getProducts_shouldReturnEmptyList_whenNoProductsExist() {
        // Arrange: Ensure the product list is empty
        productService.deleteAllProducts();  // Assuming a deleteAllProducts method is available for cleanup

        // Act: Call the getProducts method to retrieve products
        ArrayList<Product> products = productService.getProducts();

        // Assert: Verify that the returned list is empty
        assertNotNull(products);
        assertTrue(products.isEmpty());  // Ensure the list is empty
    }

    @Test
    void getProducts_shouldReturnRemainingProducts_afterOneIsDeleted() {
        // Arrange: Create three products and add them to the repository
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setName("Product1");
        product1.setPrice(100.0);

        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setName("Product2");
        product2.setPrice(150.0);

        Product product3 = new Product();
        product3.setId(UUID.randomUUID());
        product3.setName("Product3");
        product3.setPrice(200.0);

        productService.addProduct(product1);
        productService.addProduct(product2);
        productService.addProduct(product3);

        // Act: Delete one product (product2)
        productService.deleteProductById(product2.getId());

        // Retrieve all products
        ArrayList<Product> products = productService.getProducts();

        // Assert: Verify that the deleted product is not in the list and the others remain
        assertNotNull(products);
        assertEquals(2, products.size());  // Ensure there are now 2 products
        //assertTrue(products.contains(product1));  // Ensure product1 is still in the list
        //assertTrue(products.contains(product3));  // Ensure product3 is still in the list
        //assertFalse(products.contains(product2));  // Ensure product2 is not in the list
    }

    @Test
    void addProduct_shouldAddProductSuccessfully_whenValidProduct() {
        // Arrange: Create a valid product
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Product1");
        product.setPrice(100.0);

        // Act: Call the addProduct method to add the product
        Product addedProduct = productService.addProduct(product);

        // Assert: Verify that the product is added successfully
        assertNotNull(addedProduct);
        assertEquals(product.getId(), addedProduct.getId());  // Ensure the IDs match
        assertEquals(product.getName(), addedProduct.getName());  // Ensure the names match
        assertEquals(product.getPrice(), addedProduct.getPrice(), 0.01);  // Ensure the prices match
    }

    @Test
    void addProduct_shouldThrowException_whenProductIsNull() {
        // Act and Assert: Verify that an IllegalArgumentException is thrown when the product is null
        assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(null);  // Passing null as the product
        });
    }


    @Test
    void addProduct_shouldAddProductSuccessfully_whenProductIsValid() {
        // Arrange: Create a valid product
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("ValidProduct");
        product.setPrice(100.0);

        // Act: Add the product using the productService
        Product addedProduct = productService.addProduct(product);

        // Assert: Verify the product is added successfully
        assertNotNull(addedProduct); // Ensure the added product is not null
        assertEquals(product.getId(), addedProduct.getId());  // Ensure the ID is the same
        assertEquals(product.getName(), addedProduct.getName());  // Ensure the name is the same
        assertEquals(product.getPrice(), addedProduct.getPrice(), 0.001);  // Ensure the price is the same
    }

    @Test
    void getProductById_shouldReturnProduct_whenProductExists() {
        // Arrange: Create and add a valid product
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("ValidProduct");
        product.setPrice(100.0);
        productService.addProduct(product);

        // Act: Retrieve the product using the getProductById method
        Product retrievedProduct = productService.getProductById(productId);

        // Assert: Verify that the retrieved product is the same as the added product
        assertNotNull(retrievedProduct);  // Ensure the product is not null
        assertEquals(productId, retrievedProduct.getId());  // Ensure the IDs match
        assertEquals("ValidProduct", retrievedProduct.getName());  // Ensure the names match
        assertEquals(100.0, retrievedProduct.getPrice(), 0.001);  // Ensure the prices match
    }

    @Test
    void getProductById_shouldThrowException_whenInvalidIdPassed() {
        // Arrange: Create a non-existent product ID
        UUID invalidProductId = UUID.randomUUID();  // This ID is not assigned to any product

        // Act & Assert: Verify that an exception is thrown when attempting to get a product with an invalid ID
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductById(invalidProductId);  // Try fetching a product with an invalid ID
        });
    }

    @Test
    void getProductById_shouldThrowException_whenProductIdIsNull() {
        // Arrange: Set productId to null
        UUID nullProductId = null;

        // Act and Assert: Verify that an IllegalArgumentException is thrown when trying to get a product with a null ID
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductById(nullProductId);
        });
    }

    @Test
    void updateProduct_shouldUpdateProduct_whenValidDataIsProvided() {
        // Arrange: Create and add a product to the repository
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("OldName");
        product.setPrice(50.0);
        productService.addProduct(product);  // Add the product to the service

        // Act: Update the product's name and price
        String newName = "NewName";
        double newPrice = 60.0;
        productService.updateProduct(productId, newName, newPrice);

        // Retrieve the updated product
        Product updatedProduct = productService.getProductById(productId);

        // Assert: Verify that the product's name and price have been updated
        assertNotNull(updatedProduct);
        assertEquals(newName, updatedProduct.getName());
        assertEquals(newPrice, updatedProduct.getPrice());
    }

    @Test
    void updateProduct_shouldThrowException_whenProductDoesNotExist() {
        // Arrange: Create a non-existent product ID
        UUID nonExistentProductId = UUID.randomUUID();  // This ID doesn't exist in the repository

        // Act & Assert: Verify that an exception is thrown when trying to update a product with an invalid ID
        assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(nonExistentProductId, "NewName", 60.0);
        });
    }

    @Test
    void updateProduct_shouldThrowException_whenPriceIsNegative() {
        // Arrange: Create a valid product
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);

        product.setPrice(50.0);
        productService.addProduct(product);  // Add the product to the service



        // Act & Assert: Verify that an exception is thrown when the price is negative
        assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(productId, "NewName", -10.0);  // Invalid price
        });
    }

    @Test
    void applyDiscount_shouldApplyDiscountCorrectly_whenValidDiscount() {
        // Arrange: Create products and add them to the repository
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        Product product1 = new Product();
        product1.setId(productId1);
        product1.setName("Product1");
        product1.setPrice(100.0);  // Original price 100

        Product product2 = new Product();
        product2.setId(productId2);
        product2.setName("Product2");
        product2.setPrice(200.0);  // Original price 200

        productService.addProduct(product1);
        productService.addProduct(product2);

        // Act: Apply a 10% discount to both products
        ArrayList<UUID> productIds = new ArrayList<>(List.of(productId1, productId2));
        productService.applyDiscount(10.0, productIds);

        // Assert: Verify the prices after the discount
        Product updatedProduct1 = productService.getProductById(productId1);
        Product updatedProduct2 = productService.getProductById(productId2);

        assertEquals(90.0, updatedProduct1.getPrice(), 0.01);  // 100 - 10% = 90
        assertEquals(180.0, updatedProduct2.getPrice(), 0.01);  // 200 - 10% = 180
    }

    @Test
    void applyDiscount_shouldThrowException_whenDiscountExceeds100() {
        // Arrange: Create a product and add it to the repository
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Product1");
        product.setPrice(100.0);  // Original price 100
        productService.addProduct(product);

        // Act & Assert: Try to apply a discount of 110%, expecting an exception
        assertThrows(IllegalArgumentException.class, () -> {
            productService.applyDiscount(110.0, new ArrayList<>(List.of(productId)));  // Invalid discount > 100
        });
    }

    @Test
    void applyDiscount_shouldThrowException_whenDiscountIsNegative() {
        // Arrange: Create a product and add it to the repository
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Product1");
        product.setPrice(100.0);  // Original price 100
        productService.addProduct(product);

        // Act & Assert: Try to apply a negative discount, expecting an exception
        assertThrows(IllegalArgumentException.class, () -> {
            productService.applyDiscount(-10.0, new ArrayList<>(List.of(productId)));  // Invalid negative discount
        });
    }

    @Test
    void deleteProductById_shouldDeleteProduct_whenProductExists() {
        // Arrange: Create a product and add it to the repository
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Product1");
        product.setPrice(100.0);
        productService.addProduct(product);

        // Act: Call the deleteProductById method to remove the product
        productService.deleteProductById(productId);

        // Assert: Verify that the product is deleted and cannot be found
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductById(productId);  // This should throw an exception as the product is deleted
        });
    }

    @Test
    void deleteProductById_shouldThrowException_whenProductDoesNotExist() {
        // Arrange: Use a random product ID that does not exist
        UUID nonExistentProductId = UUID.randomUUID();

        // Act & Assert: Try to delete the product with the non-existent ID
        assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProductById(nonExistentProductId);  // Product with this ID doesn't exist
        });
    }

    @Test
    void deleteProductById_shouldDeleteMultipleProducts_whenProductsExist() {
        // Arrange: Create multiple products and add them to the repository
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        Product product1 = new Product();
        product1.setId(productId1);
        product1.setName("Product1");
        product1.setPrice(100.0);

        Product product2 = new Product();
        product2.setId(productId2);
        product2.setName("Product2");
        product2.setPrice(200.0);

        productService.addProduct(product1);
        productService.addProduct(product2);

        // Act: Call deleteProductById for both products
        productService.deleteProductById(productId1);
        productService.deleteProductById(productId2);

        // Assert: Verify that both products are deleted and cannot be found
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductById(productId1);  // Should throw an exception as product1 is deleted
        });

        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductById(productId2);  // Should throw an exception as product2 is deleted
        });
    }

    //******************************** USER SERVICE *******************************************

    @Test
    void addUser_shouldAddUser_whenUserIsValid() {
        // Arrange: Create a valid user
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("John Doe");

        // Act: Add the user to the service
        User addedUser = userService.addUser(user);

        // Assert: Verify that the user was added successfully
        assertNotNull(addedUser);
        assertEquals(user.getId(), addedUser.getId());
        assertEquals(user.getName(), addedUser.getName());
    }

    @Test
    void addUser_shouldThrowException_whenUserIsNull() {
        // Act & Assert: Verify that adding a null user throws an exception
        assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(null);  // Passing null should throw an exception
        });
    }

    @Test
    void addUser_shouldThrowException_whenUserHasMissingName() {
        // Arrange: Create a user with a missing name
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(null);  // Missing name

        // Act & Assert: Verify that adding a user with missing name throws an exception
        assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user);  // This should throw an exception because the name is null
        });
    }


    @Test
    void getUsers_shouldReturnAllUsers_whenUsersExist() {
        // Arrange: Add users to the repository

        userRepository.deleteAllUsers();

        User user1 = new User("John Doe", new ArrayList<>());
        User user2 = new User("Jane Doe", new ArrayList<>());
        userService.addUser(user1);
        userService.addUser(user2);

        // Act: Retrieve the users from the service
        ArrayList<User> users = userService.getUsers();

        // Assert: Ensure that the users are returned
        assertNotNull(users);
        assertEquals(2, users.size()); // Expecting 2 users
        //assertTrue(users.contains(user1)); // Ensure user1 is present
        // assertTrue(users.contains(user2)); // Ensure user2 is present
    }

    @Test
    void getUsers_shouldReturnEmptyList_whenNoUsersExist() {
        // Arrange: Ensure no users exist
        userRepository.deleteAllUsers(); // Assuming a deleteAllUsers method is available to clean up

        // Act: Retrieve users from the service
        ArrayList<User> users = userService.getUsers();

        // Assert: Verify the list is empty
        assertNotNull(users);
        assertTrue(users.isEmpty()); // No users should exist
    }

    @Test
    void getUsers_shouldReturnCorrectUsers_afterDeletingOneUser() {

        userRepository.deleteAllUsers(); // Assuming a deleteAllUsers method is available to clean up

        // Arrange: Add users to the repository
        User user1 = new User("John Doe", new ArrayList<>());
        User user2 = new User("Jane Doe", new ArrayList<>());
        userService.addUser(user1);
        userService.addUser(user2);

        // Act: Delete one user
        userService.deleteUser(user1.getId());

        // Retrieve the remaining users
        ArrayList<User> users = userService.getUsers();

        // Assert: Ensure the deleted user is not in the list
        assertNotNull(users);
        assertEquals(1, users.size()); // Only one user should be left
        //assertTrue(users.contains(user2)); // Ensure user2 is still present
        // assertFalse(users.contains(user1)); // Ensure user1 is removed
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        // Arrange: Create a user and add it to the repository
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John Doe", new ArrayList<>());
        userRepository.addUser(user);

        // Act: Retrieve the user by ID
        User retrievedUser = userService.getUserById(userId);

        // Assert: Verify that the retrieved user matches the original user
        assertNotNull(retrievedUser);
        assertEquals(userId, retrievedUser.getId());
        assertEquals("John Doe", retrievedUser.getName());
    }

    @Test
    void getUserById_ShouldReturnNull_WhenUserDoesNotExist() {
        // Arrange: Use a random user ID that does not exist in the repository
        UUID nonExistentUserId = UUID.randomUUID();

        // Act: Try to retrieve the user by the non-existent ID
        User retrievedUser = userService.getUserById(nonExistentUserId);

        // Assert: Verify that the returned user is null
        assertNull(retrievedUser);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserIdIsNull() {
        // Act and Assert: Verify that passing null as the user ID throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(null);
        });
    }

    @Test
    void getOrdersByUserId_shouldReturnOrders_whenUserHasOrders() {
        // Arrange: Create a user with orders
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Alice Johnson");

        orderRepository.deleteAllOrders();  // Assuming a deleteAllOrders method is available for cleanup


        Order order1 = new Order();
        order1.setId(UUID.randomUUID());
        order1.setUserId(userId);
        order1.setTotalPrice(150.0);

        Order order2 = new Order();
        order2.setId(UUID.randomUUID());
        order2.setUserId(userId);
        order2.setTotalPrice(200.0);

        user.getOrders().add(order1);
        user.getOrders().add(order2);
        userRepository.addUser(user); // Add user with orders to the repository

        // Act: Retrieve the orders for the user
        List<Order> orders = userRepository.getOrdersByUserId(userId);

        // Assert: Ensure the retrieved orders match what was added
        assertNotNull(orders);
        assertEquals(2, orders.size()); // Ensure both orders are retrieved
        //assertTrue(orders.contains(order1));
        //assertTrue(orders.contains(order2));
    }

    @Test
    void getOrdersByUserId_shouldReturnEmptyList_whenUserHasNoOrders() {
        // Arrange: Create a user with no orders
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Bob Williams");

        userRepository.addUser(user); // Add user without any orders

        // Act: Retrieve the orders for the user
        List<Order> orders = userRepository.getOrdersByUserId(userId);

        // Assert: Ensure the order list is empty
        assertNotNull(orders);
        assertTrue(orders.isEmpty()); // Ensure no orders are retrieved
    }

    @Test
    void getOrdersByUserId_shouldThrowException_whenUserIdIsNull() {
        // Act & Assert: Ensure an exception is thrown for a null user ID
        assertThrows(IllegalArgumentException.class, () -> {
            userRepository.getOrdersByUserId(null);
        });
    }


    @Test
    void addOrderToUser_shouldAddOrder_whenCartHasProducts() {
        // Arrange: Create a user and a cart with products
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        userRepository.addUser(user);

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Product1");
        product.setPrice(50.0);

        Cart cart = new Cart(userId, new ArrayList<>());
        cart.addProduct(product); // Add product to cart
        cartRepository.addCart(cart); // Add cart to repository

        // Act: Call addOrderToUser to create an order from the cart
        userService.addOrderToUser(userId);

        // Assert: Verify the order is created and added to the user's history
        User updatedUser = userRepository.getUserById(userId);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getOrders().size()); // Ensure the order was added

        Order createdOrder = updatedUser.getOrders().get(0);
        assertNotNull(createdOrder);
        assertEquals(userId, createdOrder.getUserId()); // Ensure order belongs to correct user
        assertEquals(50.0, createdOrder.getTotalPrice()); // Verify total price is correct
    }

    @Test
    void addOrderToUser_shouldThrowException_whenCartIsEmpty() {
        // Arrange: Create a user without adding any products to their cart
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        userRepository.addUser(user);

        Cart cart = new Cart(userId, new ArrayList<>()); // Empty cart
        cartRepository.addCart(cart); // Add cart to repository

        // Act & Assert: Verify that an exception is thrown when trying to add an order
        assertThrows(IllegalStateException.class, () -> {
            userService.addOrderToUser(userId); // Try to add order when cart is empty
        });
    }

    @Test
    void addOrderToUser_shouldThrowException_whenCartDoesNotExist() {
        // Arrange: Create a user without adding a cart
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        userRepository.addUser(user);

        // Act & Assert: Verify that an exception is thrown when trying to add an order for a user without a cart
        assertThrows(IllegalStateException.class, () -> {
            userService.addOrderToUser(userId); // Try to add order when cart does not exist
        });
    }

    @Test
    void emptyCart_shouldEmptyCart_whenCartHasProducts() {
        // Arrange: Create a user and add products to their cart
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        userRepository.addUser(user);

        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setName("Product1");
        product1.setPrice(50.0);

        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setName("Product2");
        product2.setPrice(30.0);

        Cart cart = new Cart(userId, new ArrayList<>());
        cart.addProduct(product1);
        cart.addProduct(product2); // Add products to cart
        cartRepository.addCart(cart); // Add cart to repository

        // Act: Call emptyCart to remove products from the cart
        userService.emptyCart(userId);

        // Assert: Verify that the cart is empty
        Cart updatedCart = cartService.getCartByUserId(userId);
        assertNotNull(updatedCart);
        assertTrue(updatedCart.getProducts().isEmpty()); // Ensure the cart is empty
    }

    @Test
    void emptyCart_shouldNotModifyCart_whenCartIsEmpty() {
        // Arrange: Create a user with an empty cart
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        userRepository.addUser(user);

        Cart cart = new Cart(userId, new ArrayList<>()); // Empty cart
        cartRepository.addCart(cart); // Add cart to repository

        // Act: Call emptyCart on the empty cart
        userService.emptyCart(userId);

        // Assert: Verify that the cart is still empty and no products are removed
        Cart updatedCart = cartService.getCartByUserId(userId);
        assertNotNull(updatedCart);
        assertTrue(updatedCart.getProducts().isEmpty()); // Ensure the cart is still empty
    }


    @Test
    void emptyCart_shouldNotModifyCart_whenCartDoesNotExist() {
        // Arrange: Create a user without a cart (simulate no cart)
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        userRepository.addUser(user);

        // Act: Call emptyCart for a user who has no cart
        userService.emptyCart(userId);

        // Assert: Verify that no cart is created and nothing is modified
        Cart updatedCart = cartService.getCartByUserId(userId);
        assertNull(updatedCart); // Ensure the cart does not exist
    }


    @Test
    void removeOrderFromUser_shouldRemoveOrder_whenOrderExists() {
        // Arrange: Create a user and add orders to their order history
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        userRepository.addUser(user);

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setTotalPrice(100.0);

        userRepository.addOrderToUser(userId, order); // Add the order to the user's order history

        // Act: Remove the order from the user's order history
        userService.removeOrderFromUser(userId, orderId);

        // Assert: Verify that the order has been removed
        User updatedUser = userRepository.getUserById(userId); // Reload the user data
        assertNotNull(updatedUser);
        assertTrue(updatedUser.getOrders().stream().noneMatch(o -> o.getId().equals(orderId)),
                "Order should be removed from the user's order history.");
    }


    @Test
    void removeOrderFromUser_shouldNotRemoveOrder_whenOrderDoesNotExist() {
        // Arrange: Create a user with one order in their history
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Jane Doe");
        userRepository.addUser(user);

        Order order = new Order();
        order.setId(UUID.randomUUID()); // Different ID than the one we'll attempt to remove
        order.setUserId(userId);
        order.setTotalPrice(50.0);

        userRepository.addOrderToUser(userId, order); // Add the order to the user's order history

        // Act: Try to remove an order that doesn't exist for the user
        userService.removeOrderFromUser(userId, orderId);

        // Assert: Verify that the order list is unchanged
        User updatedUser = userRepository.getUserById(userId);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getOrders().size()); // Ensure the user still has one order
    }


    @Test
    void removeOrderFromUser_shouldNotModifyOrders_whenUserDoesNotExist() {
        // Arrange: Create a user and add an order to their order history
        UUID userId = UUID.randomUUID(); // Different user ID for the test
        UUID orderId = UUID.randomUUID();
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("John Doe");
        userRepository.addUser(user);

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(user.getId()); // Different user ID for the order
        order.setTotalPrice(100.0);

        userRepository.addOrderToUser(user.getId(), order); // Add order to the existing user

        // Act: Try to remove an order for a user that doesn't exist
        userService.removeOrderFromUser(userId, orderId);

        // Assert: Verify that the user's orders remain unaffected
        User updatedUser = userRepository.getUserById(user.getId());
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getOrders().size()); // Ensure the user still has the same order
    }

    @Test
    void deleteUserById_shouldDeleteUser_whenUserExists() {
        // Arrange: Create a user and add them to the repository
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        userRepository.addUser(user);

        // Act: Delete the user by ID
        userService.deleteUserById(userId);

        // Assert: Verify that the user is deleted
        User deletedUser = userRepository.getUserById(userId);
        assertNull(deletedUser); // Ensure the user no longer exists in the repository
    }

    @Test
    void deleteUserById_shouldNotDeleteAnyUser_whenUserDoesNotExist() {
        // Arrange: Create a user and add them to the repository
        UUID existingUserId = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setId(existingUserId);
        existingUser.setName("Jane Doe");
        userRepository.addUser(existingUser);

        // Act: Attempt to delete a user that doesn't exist
        UUID nonExistingUserId = UUID.randomUUID(); // Random UUID that doesn't exist
        userService.deleteUserById(nonExistingUserId);

        // Assert: Verify that no user was deleted
        User deletedUser = userRepository.getUserById(existingUserId);
        assertNotNull(deletedUser); // Ensure the existing user still exists
    }


    @Test
    void deleteUserById_shouldDeleteUserAndTheirOrders_whenUserHasOrders() {
        // Arrange: Create a user and add orders to their order history
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Bob Smith");

        Order order1 = new Order();
        order1.setId(UUID.randomUUID());
        order1.setUserId(userId);
        order1.setTotalPrice(100.0);
        user.getOrders().add(order1);

        Order order2 = new Order();
        order2.setId(UUID.randomUUID());
        order2.setUserId(userId);
        order2.setTotalPrice(200.0);
        user.getOrders().add(order2);

        userRepository.addUser(user); // Add user to the repository

        // Act: Delete the user by ID
        userService.deleteUserById(userId);

        // Assert: Verify that the user and their orders are deleted
        User deletedUser = userRepository.getUserById(userId);
        assertNull(deletedUser); // Ensure the user is deleted

        // Verify that the orders are also removed
        List<Order> userOrders = userRepository.getOrdersByUserId(userId);
        assertTrue(userOrders.isEmpty()); // Ensure the user's orders are deleted as well
    }






}

