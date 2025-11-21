package com.ecommerce1.service;

import com.ecommerce1.model.Order;
import com.ecommerce1.model.Product;
import com.ecommerce1.model.User;
import com.ecommerce1.repository.OrderRepository;
import com.ecommerce1.repository.ProductRepository;
import com.ecommerce1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // Only logged-in users can add to cart
    public Order addToCart(Long userId, Long productId, int quantity) {
        if(userId == null) return null; // Not logged in

        User user = userRepository.findById(userId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        if(user != null && product != null) {
            double totalPrice = quantity * product.getPrice();
            Order order = new Order(user, product, quantity, totalPrice);
            return orderRepository.save(order);
        }
        return null;
    }

    // Get orders for logged-in user
    public List<Order> getOrdersByUser(Long userId) {
        if(userId == null) return List.of(); // Not logged in → return empty
        User user = userRepository.findById(userId).orElse(null);
        if(user != null) {
            return orderRepository.findByUser(user);
        }
        return List.of();
    }

    // Remove order
    public void removeOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
