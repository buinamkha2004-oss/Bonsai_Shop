package com.example.bonsai_shop.seller.controller;

import com.example.bonsai_shop.entity.Product;
import com.example.bonsai_shop.seller.service.SellerProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerDashboardController {

    private final SellerProductService sellerProductService;

    @GetMapping
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Product> products = sellerProductService.getMyProducts(userDetails.getUsername());
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("publishedProducts", products.stream()
                .filter(product -> "AVAILABLE".equals(product.getProductStatus()))
                .count());
        model.addAttribute("draftProducts", products.stream()
                .filter(product -> "DRAFT".equals(product.getProductStatus()))
                .count());
        return "seller/dashboard";
    }
}
