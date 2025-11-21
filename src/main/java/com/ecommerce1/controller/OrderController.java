package com.ecommerce1.controller;

import com.ecommerce1.model.Order;
import com.ecommerce1.model.Product;
import com.ecommerce1.model.User;
import com.ecommerce1.repository.OrderRepository;
import com.ecommerce1.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    // ====================== ADD TO CART ======================
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login"; // Only logged-in users can add

        Product product = productRepo.findById(productId).orElse(null);
        if (product == null)
            return "redirect:/products";

        // Check if product already in cart
        List<Order> existingOrders = orderRepo.findByUser(user);
        for (Order o : existingOrders) {
            if (o.getProduct().getId().equals(productId)) {
                o.setQuantity(o.getQuantity() + 1);
                orderRepo.save(o);
                return "redirect:/order/cart";
            }
        }

        // Otherwise add new cart item
        Order order = new Order();
        order.setProduct(product);
        order.setUser(user);
        order.setQuantity(1);

        orderRepo.save(order);

        return "redirect:/order/cart";
    }

    // ====================== VIEW CART ======================
    @GetMapping("/cart")
    public String cartPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        List<Order> orderItems;

        if (user != null) {
            orderItems = orderRepo.findByUser(user);
        } else {
            // Logged-out users see empty cart
            orderItems = Collections.emptyList();
        }

        model.addAttribute("orderItems", orderItems);

        double total = orderItems.stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();
        model.addAttribute("totalPrice", total);

        return "cart";   // cart.html
    }

    // ====================== REMOVE FROM CART ======================
    @PostMapping("/remove")
    public String removeItem(@RequestParam Long orderId,
                             HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login"; // Only logged-in users can remove

        Order o = orderRepo.findById(orderId).orElse(null);
        if (o != null && o.getUser().getId().equals(user.getId())) {
            orderRepo.delete(o);
        }

        return "redirect:/order/cart";
    }
}
