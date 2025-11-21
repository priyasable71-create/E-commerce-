package com.ecommerce1.controller;

import com.ecommerce1.model.Product;
import com.ecommerce1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product";  // Thymeleaf template: product.html
    }
}
